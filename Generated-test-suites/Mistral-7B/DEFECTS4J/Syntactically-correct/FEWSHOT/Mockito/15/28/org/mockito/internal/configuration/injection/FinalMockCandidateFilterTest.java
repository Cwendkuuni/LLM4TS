package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.MockitoConfigurationBuilder;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldTestUtils;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockitoMockito;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMockAndSuccessfulFieldSetterInjection() {
        Stubber stubber = mock(Stubber.class);
        Object mock = MockitoMockito.spy(fieldInstance, stubber);

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };

        stubber.doAnswer(answer).when(mock).set(any(Field.class), any(Object.class));

        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithOneMockAndUnsuccessfulFieldSetterInjection() {
        Stubber stubber = mock(Stubber.class);
        Object mock = MockitoMockito.spy(fieldInstance, stubber);

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());

        verify(mock).set(field, any(Object.class));

        MockitoException exception = assertThrows(MockitoException.class, () -> injecter.thenInject());
        assertEquals("Problems injecting dependency in " + field.getName(), exception.getMessage());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSuccessfulFieldSetterInjection() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndUnsuccessfulFieldSetterInjectionOnFirstMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Stubber stubber1 = Whitebox.getInternalState(mock1, Stubber.class);
        Stubber stubber2 = Whitebox.getInternalState(mock2, Stubber.class);

        mocks.add(mock1);
        mocks.add(mock2);

        stubber1.doThrow(new RuntimeException("Exception on mock1")).when(mock1).set(any(Field.class), any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSuccessfulFieldSetterInjectionOnSecondMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Stubber stubber1 = Whitebox.getInternalState(mock1, Stubber.class);
        Stubber stubber2 = Whitebox.getInternalState(mock2, Stubber.class);

        mocks.add(mock1);
        mocks.add(mock2);

        stubber2.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mock2).set(any(Field.class), any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndUnsuccessfulFieldSetterInjectionOnAllMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Stubber stubber1 = Whitebox.getInternalState(mock1, Stubber.class);
        Stubber stubber2 = Whitebox.getInternalState(mock2, Stubber.class);

        mocks.add(mock1);
        mocks.add(mock2);

        stubber1.doThrow(new RuntimeException("Exception on mock1")).when(mock1).set(any(Field.class), any(Object.class));
        stubber2.doThrow(new RuntimeException("Exception on mock2")).when(mock2).set(any(Field.class), any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        assertNull(filter.filterCandidate(null, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullField() {
        assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        assertNull(filter.filterCandidate(mocks, field, null));
    }
}