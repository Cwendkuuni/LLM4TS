package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private Object mock1;
    private Object mock2;
    private List<Object> mocks;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
        when(field.getName()).thenReturn("testField");
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
    }

    @Test
    public void testFilterCandidate_SingleMock_BeanPropertySetterSuccess() {
        // Given
        when(mock1.getClass()).thenReturn(FieldSetter.class.getDeclaringClass());
        when(mock1.getName()).thenReturn("Mock1");
        when(mock1.getField(field)).thenReturn(fieldInstance);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(mock1).getName();
        verify(mock1).getClass();
        verify(mock1).getField(field);
        verify(fieldInstance).getClass();
        verify(field).set(mock1, mock1);
        assertNotNull(injecter);
        assertEquals(mock1, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_SingleMock_BeanPropertySetterFailure() {
        // Given
        when(mock1.getClass()).thenReturn(BeanPropertySetter.class.getDeclaringClass());
        when(mock1.getName()).thenReturn("Mock1");
        when(mock1.getField(field)).thenReturn(fieldInstance);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(mock1).getName();
        verify(mock1).getClass();
        verify(mock1).getField(field);
        verifyZeroInteractions(fieldInstance);
        verify(new Reporter()).cannotInjectDependency(field, mock1, new RuntimeException("Test Exception"));
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_OneSuccess() {
        // Given
        when(mock1.getClass()).thenReturn(BeanPropertySetter.class.getDeclaringClass());
        when(mock1.getName()).thenReturn("Mock1");
        when(mock1.getField(field)).thenReturn(fieldInstance);

        when(mock2.getClass()).thenReturn(FieldSetter.class.getDeclaringClass());
        when(mock2.getName()).thenReturn("Mock2");
        when(mock2.getField(field)).thenReturn(fieldInstance);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(mock1).getName();
        verify(mock1).getClass();
        verify(mock1).getField(field);
        verify(mock2).getName();
        verify(mock2).getClass();
        verify(mock2).getField(field);
        verify(fieldInstance).getClass();
        verify(field).set(mock1, mock1);
        assertNotNull(injecter);
        assertEquals(mock1, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoSuccess() {
        // Given
        when(mock1.getClass()).thenReturn(BeanPropertySetter.class.getDeclaringClass());
        when(mock1.getName()).thenReturn("Mock1");
        when(mock1.getField(field)).thenReturn(fieldInstance);

        when(mock2.getClass()).thenReturn(BeanPropertySetter.class.getDeclaringClass());
        when(mock2.getName()).thenReturn("Mock2");
        when(mock2.getField(field)).thenReturn(fieldInstance);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(mock1).getName();
        verify(mock1).getClass();
        verify(mock1).getField(field);
        verify(mock2).getName();
        verify(mock2).getClass();
        verify(mock2).getField(field);
        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(new Reporter());
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
        // Given
        when(mock1.getClass()).thenReturn(FieldSetter.class.getDeclaringClass());
        when(mock1.getName()).thenReturn("Mock1");

        when(mock2.getClass()).thenReturn(BeanPropertySetter.class.getDeclaringClass());
        when(mock2.getName()).thenReturn("Mock2");

        when(mock1.getName()).thenReturn("Mock1");
        when(mock2.getName()).thenReturn("Mock2");

        when(mock1.getDeclaredField("testField")).thenReturn(field);
        when(mock2.getDeclaredField("testField")).thenReturn(field);

        when(mock1.getField(field)).thenReturn(null);
        when(mock2.getField(field)).thenReturn(fieldInstance);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(mock1).getName();
        verify(mock1).getClass();
        verify(mock1).getDeclaredField("testField");
        verify(mock2).getName();
        verify(mock2).getClass();
        verify(mock2).getDeclaredField("testField");
        verify(mock2).getField(field);
        verify(fieldInstance).getClass();
        verify(field).set(mock2, fieldInstance);
        assertNotNull(injecter);
        assertEquals(mock2, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterFailure() {
        // Given
        when(mock1.getClass()).thenReturn(FieldSetter.class.getDeclaringClass());
        when(mock1.getName()).thenReturn("Mock1");

        when(mock2.getClass()).thenReturn(FieldSetter.class.getDeclaringClass());
        when(mock2.getName()).thenReturn("Mock2");

        when(mock1.getName()).thenReturn("Mock1");
        when(mock2.getName()).thenReturn("Mock2");

        when(mock1.getDeclaredField("testField")).thenReturn(field);
        when(mock2.getDeclaredField("testField")).thenReturn(field);

        when(mock1.getField(field)).thenReturn(null);
        when(mock2.getField(field)).thenReturn(fieldInstance);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(mock1).getName();
        verify(mock1).getClass();
        verify(mock1).getDeclaredField("testField");
        verify(mock2).getName();
        verify(mock2).getClass();
        verify(mock2).getDeclaredField("testField");
        verifyZeroInteractions(mock1.getField(field));
        verifyZeroInteractions(mock2.getField(field));
        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(new Reporter());
        assertNull(injecter);
    }
}