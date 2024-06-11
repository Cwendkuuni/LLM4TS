package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    @Rule
    public TestRule testRule = Mockito.mock(TestRule.class);

    private Reporter reporter;
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        filter = new FinalMockCandidateFilter();
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        // Given
        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);

        // When
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(actual);
        OngoingInjecter expected = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                try {
                    Whitebox.setInternalState(fieldInstance, field.getName(), "testValue");
                } catch (IllegalAccessException e) {
                    new Reporter().cannotInjectDependency(field, mocks.iterator().next(), e);
                }
                return mocks.iterator().next();
            }
        };
        verify(reporter).cannotInjectDependency(field, mocks.iterator().next(), any(RuntimeException.class));
        verify(fieldInstance).getClass();
        verify(fieldInstance).getName();
        verify(fieldInstance).getDeclaredFields();
        verify(fieldInstance).getClass().getDeclaredField("testField");
        verify(fieldInstance).getClass().getDeclaredField("testField").getType();
        verify(mocks.iterator().next()).getClass();
        verify(mocks.iterator().next()).isInstanceOf(Object.class);
        assertEquals(expected.thenInject(), actual.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Given
        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);

        // When
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(actual);
        OngoingInjecter expected = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };
        verify(reporter, Mockito.times(mocks.size())).cannotInjectDependency(field, any(Object.class), any(RuntimeException.class));
        verify(reporter, Mockito.never()).cannotInjectDependency(field, any(Object.class), any(IllegalAccessException.class));
        assertEquals(expected.thenInject(), actual.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullMocks() {
        // Given
        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);

        // When
        OngoingInjecter actual = filter.filterCandidate(null, field, fieldInstance);

        // Then
        assertNull(actual);
    }

    @Test
    public void testFilterCandidateWithNullField() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));

        // When
        OngoingInjecter actual = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertNull(actual);
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = mock(Field.class);

        // When
        OngoingInjecter actual = filter.filterCandidate(mocks, field, null);

        // Then
        assertNull(actual);
    }
}