package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.SimpleField;
import org.mockito.internal.util.reflection.SimpleValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private Field testField;
    private Object testInstance;
    private FinalMockCandidateFilter filter;
    private List<Object> mocks;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        testField = new SimpleField("testField", int.class, new SimpleValue(1));
        testInstance = new Object() {
            public Field field = testField;
            public int value = 1;
        };
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_oneMock_propertySetterSuccess() {
        Object mock = mock(Object.class);
        when(mock.getClass().getDeclaredField("testField")).thenReturn(testField);

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, testField, testInstance);

        injecter.thenInject();

        verifyNoMoreInteractions(reporter);
        verifyZeroInteractions(mock);
        assertEquals(mock, testInstance.field.get(mock));
    }

    @Test
    public void testFilterCandidate_oneMock_propertySetterFailure() {
        Object mock = mock(Object.class);
        when(mock.getClass().getDeclaredField("testField")).thenReturn(testField);
        when(testField.set(mock, anyInt())).thenThrow(new RuntimeException());

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, testField, testInstance);

        injecter.thenInject();

        verify(reporter).cannotInjectDependency(eq(testField), eq(mock), any(RuntimeException.class));
        verifyZeroInteractions(mock);
        assertNull(testInstance.field.get(mock));
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSetterSuccess() {
        Object mock = mock(Object.class);
        when(mock.getClass().getDeclaredField("testField")).thenReturn(testField);
        when(testField.isAccessible()).thenReturn(true);

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, testField, testInstance);

        injecter.thenInject();

        verifyNoMoreInteractions(reporter);
        verifyZeroInteractions(mock);
        assertEquals(1, testInstance.value);
        assertEquals(mock, testInstance.field.get(mock));
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSetterFailure() {
        Object mock = mock(Object.class);
        when(mock.getClass().getDeclaredField("testField")).thenReturn(testField);
        when(testField.isAccessible()).thenReturn(true);
        when(testField.set(mock, anyInt())).thenThrow(new RuntimeException());

        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, testField, testInstance);

        injecter.thenInject();

        verify(reporter).cannotInjectDependency(eq(testField), eq(mock), any(RuntimeException.class));
        verifyZeroInteractions(mock);
        assertEquals(1, testInstance.value);
        assertNull(testInstance.field.get(mock));
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatch() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        when(mock1.getClass().getDeclaredField("testField")).thenReturn(testField);
        when(mock2.getClass().getDeclaredField("testField")).thenReturn(new SimpleField("testField2", int.class, new SimpleValue(2)));

        mocks.addAll(Collections.nCopies(2, mock1));

        OngoingInjecter injecter = filter.filterCandidate(mocks, testField, testInstance);

        injecter.thenInject();

        verifyZeroInteractions(reporter);
        verifyZeroInteractions(mock1);
        verifyZeroInteractions(mock2);
        assertEquals(1, testInstance.value);
    }

    @Test
    public void testFilterCandidate_multipleMocks_match() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        when(mock1.getClass().getDeclaredField("testField")).thenReturn(testField);
        when(mock2.getClass().getDeclaredField("testField")).thenReturn(testField);

        mocks.addAll(Collections.nCopies(2, mock1));

        when(mock1.testField.get(mock1)).thenReturn(new Object());
        when(mock2.testField.get(mock2)).thenReturn(new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, testField, testInstance);

        injecter.thenInject();

        verify(reporter).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
        verifyZeroInteractions(mock1);
        verifyZeroInteractions(mock2);
        assertEquals(mock1, testInstance.field.get(mock1));
    }
}