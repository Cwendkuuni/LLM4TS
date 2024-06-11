package org.mockito.internal.configuration.injection;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockitoAnnotations;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {

    @Rule
    public TestRule mockitoRule = MockitoAnnotations.initMocks(this);

    @Test
    public void testFilterCandidateWithOneMockAndOneField() {
        final Object mock = mock(Object.class);
        final Object fieldInstance = mock(Object.class);
        final Field field = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField");
        final Object valueToInject = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock), field, fieldInstance);

        Stubber stubber = Whitebox.getInternalState(mockitoRule.getJavaClass(), "stubber");
        stubber.given(field).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return valueToInject;
            }
        });

        assertTrue(injecter.thenInject());
        assertEquals(valueToInject, Whitebox.getInternalState(fieldInstance, field.getName()));
    }

    @Test
    public void testFilterCandidateWithOneMockAndNullField() {
        final Object mock = mock(Object.class);
        final Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock), null, fieldInstance);

        assertFalse(injecter.thenInject());
        assertNull(Whitebox.getInternalState(fieldInstance, "anyField"));
    }

    @Test
    public void testFilterCandidateWithOneMockAndMultipleFields() {
        final Object mock = mock(Object.class);
        final Object fieldInstance1 = mock(Object.class);
        final Object fieldInstance2 = mock(Object.class);
        final Field field1 = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField1");
        final Field field2 = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField2");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock), field1, fieldInstance1);

        Stubber stubber = Whitebox.getInternalState(mockitoRule.getJavaClass(), "stubber");
        stubber.given(field1).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fieldInstance1;
            }
        });

        injecter = filter.filterCandidate(Arrays.asList(mock), field2, fieldInstance2);

        assertFalse(injecter.thenInject());
        assertNull(Whitebox.getInternalState(fieldInstance2, field2.getName()));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndOneField() {
        final Object mock1 = mock(Object.class);
        final Object mock2 = mock(Object.class);
        final Object fieldInstance = mock(Object.class);
        final Field field = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter1 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);
        OngoingInjecter injecter2 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        assertFalse(injecter1.thenInject());
        assertFalse(injecter2.thenInject());
        assertNull(Whitebox.getInternalState(fieldInstance, field.getName()));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndDifferentFields() {
        final Object mock1 = mock(Object.class);
        final Object mock2 = mock(Object.class);
        final Object fieldInstance1 = mock(Object.class);
        final Object fieldInstance2 = mock(Object.class);
        final Field field1 = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField1");
        final Field field2 = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField2");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter1 = filter.filterCandidate(Arrays.asList(mock1, mock2), field1, fieldInstance1);
        OngoingInjecter injecter2 = filter.filterCandidate(Arrays.asList(mock1, mock2), field2, fieldInstance2);

        assertFalse(injecter1.thenInject());
        assertFalse(injecter2.thenInject());
        assertNull(Whitebox.getInternalState(fieldInstance1, field1.getName()));
        assertNull(Whitebox.getInternalState(fieldInstance2, field2.getName()));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSameFieldOnFirstInstance() {
        final Object mock1 = mock(Object.class);
        final Object mock2 = mock(Object.class);
        final Object fieldInstance = mock(Object.class);
        final Field field = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter1 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);
        OngoingInjecter injecter2 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        Stubber stubber = Whitebox.getInternalState(mockitoRule.getJavaClass(), "stubber");
        stubber.given(mock1.getClass().getDeclaredField(field.getName())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fieldInstance;
            }
        });

        assertTrue(injecter1.thenInject());
        assertFalse(injecter2.thenInject());
        assertEquals(fieldInstance, Whitebox.getInternalState(mock1, field.getName()));
        assertNull(Whitebox.getInternalState(mock2, field.getName()));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSameFieldOnSecondInstance() {
        final Object mock1 = mock(Object.class);
        final Object mock2 = mock(Object.class);
        final Object fieldInstance = mock(Object.class);
        final Field field = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter1 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);
        OngoingInjecter injecter2 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        Stubber stubber = Whitebox.getInternalState(mockitoRule.getJavaClass(), "stubber");
        stubber.given(mock2.getClass().getDeclaredField(field.getName())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fieldInstance;
            }
        });

        assertFalse(injecter1.thenInject());
        assertTrue(injecter2.thenInject());
        assertNull(Whitebox.getInternalState(mock1, field.getName()));
        assertEquals(fieldInstance, Whitebox.getInternalState(mock2, field.getName()));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSameFieldOnBothInstances() {
        final Object mock1 = mock(Object.class);
        final Object mock2 = mock(Object.class);
        final Object fieldInstance = mock(Object.class);
        final Field field = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter1 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);
        OngoingInjecter injecter2 = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        Stubber stubber = Whitebox.getInternalState(mockitoRule.getJavaClass(), "stubber");
        stubber.given(mock1.getClass().getDeclaredField(field.getName())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fieldInstance;
            }
        });
        stubber.given(mock2.getClass().getDeclaredField(field.getName())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fieldInstance;
            }
        });

        assertTrue(injecter1.thenInject());
        assertTrue(injecter2.thenInject());
        assertEquals(fieldInstance, Whitebox.getInternalState(mock1, field.getName()));
        assertEquals(fieldInstance, Whitebox.getInternalState(mock2, field.getName()));
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        assertFalse(filter.filterCandidate(Collections.emptyList(), null, null).thenInject());
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        assertFalse(filter.filterCandidate(null, null, null).thenInject());
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        assertFalse(filter.filterCandidate(Collections.singletonList(mock(Object.class)), null, null).thenInject());
    }

    @Test
    public void testFilterCandidateWithNullField() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        assertFalse(filter.filterCandidate(Collections.singletonList(mock(Object.class)), null, null).thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithExceptionDuringInjection() {
        final Object mock = mock(Object.class);
        final Object fieldInstance = mock(Object.class);
        final Field field = Whitebox.getInternalState(FinalMockCandidateFilter.class, "anyField");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        Stubber stubber = Whitebox.getInternalState(mockitoRule.getJavaClass(), "stubber");
        stubber.given(field).thenThrow(new RuntimeException());

        injecter.thenInject();
    }
}