
package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    // Method: filterCandidate(final Collection<Object> mocks, final Field field, final Object fieldInstance)
    @Test
    public void testFilterCandidate_WhenMocksContainsOneElement_ShouldReturnOngoingInjecterWithMatchingMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>(Arrays.asList("mock1"));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertEquals("mock1", result);
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenMocksContainsMultipleElements_ShouldReturnOngoingInjecterWithoutInjecting() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>(Arrays.asList("mock1", "mock2"));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertNull(result);
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenSetterInjectionSucceeds_ShouldReturnOngoingInjecterWithMatchingMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>(Arrays.asList("mock1"));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(new BeanPropertySetter(fieldInstance, field).set(anyObject())).thenReturn(true);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertEquals("mock1", result);
        verify(new BeanPropertySetter(fieldInstance, field)).set("mock1");
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenSetterInjectionFails_ShouldReturnOngoingInjecterWithMatchingMockAndUseFieldSetter() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>(Arrays.asList("mock1"));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(new BeanPropertySetter(fieldInstance, field).set(anyObject())).thenReturn(false);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertEquals("mock1", result);
        verify(new BeanPropertySetter(fieldInstance, field)).set("mock1");
        verify(new FieldSetter(fieldInstance, field)).set("mock1");
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenInjectionThrowsException_ShouldCallReporterAndReturnOngoingInjecterWithoutInjecting() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>(Arrays.asList("mock1"));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        doThrow(RuntimeException.class).when(new BeanPropertySetter(fieldInstance, field)).set(anyObject());

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertNull(result);
        verify(new BeanPropertySetter(fieldInstance, field)).set("mock1");
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
        verify(new Reporter()).cannotInjectDependency(field, "mock1", RuntimeException.class);
    }
}