package org.mockito.internal.configuration;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Setup mocks
        Field field = mock(Field.class);
        Object testClassInstance = new Object();
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        mocks.add(new Object());

        // Ensure that mockCandidateFilter calls are correct
        MockCandidateFilter mockCandidateFilterMock = mock(MockCandidateFilter.class);
        when(mockCandidateFilterMock.filterCandidate(anySet(), any(Field.class), any(Object.class))).thenReturn(mockCandidateFilterMock);
        when(mockCandidateFilterMock.thenInject()).thenReturn(mockCandidateFilterMock);

        // Inject mocks
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that mockCandidateFilter calls were made
        verify(mockCandidateFilterMock, times(1)).filterCandidate(anySet(), any(Field.class), any(Object.class));
        verify(mockCandidateFilterMock, times(1)).thenInject();
    }

    @Test
    public void testInjectMocksOnFields_invalidFieldInitialization() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Setup mocks
        Field field = mock(Field.class);
        Object testClassInstance = new Object();
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Set<Object> mocks = new HashSet<>();
        mocks.add(new Object());

        // Mock the FieldInitializer to throw a MockitoException
        FieldInitializer fieldInitializerMock = mock(FieldInitializer.class);
        when(fieldInitializerMock.initialize()).thenThrow(MockitoException.class);

        // Inject mocks
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the Reporter called cannotInitializeForInjectMocksAnnotation
        verify(new Reporter()).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
    }

    @Test
    public void testInjectMockCandidate() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Setup mocks
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(new Object());
        Object fieldInstance = new Object();

        // Ensure that mockCandidateFilter calls are correct
        MockCandidateFilter mockCandidateFilterMock = mock(MockCandidateFilter.class);
        when(mockCandidateFilterMock.filterCandidate(anySet(), any(Field.class), any(Object.class))).thenReturn(mockCandidateFilterMock);
        when(mockCandidateFilterMock.thenInject()).thenReturn(mockCandidateFilterMock);

        // Inject mock candidate
        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Verify that mockCandidateFilter calls were made
        verify(mockCandidateFilterMock, times(1)).filterCandidate(anySet(), any(Field.class), any(Object.class));
        verify(mockCandidateFilterMock, times(1)).thenInject();
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Setup mocks
        Class<?> awaitingInjectionClazz = Object.class;
        Field field1 = mock(Field.class);
        when(field1.getType()).thenReturn(Object.class);
        Field field2 = mock(Field.class);
        when(field2.getType()).thenReturn(String.class);
        Field field3 = mock(Field.class);
        when(field3.getType()).thenReturn(Integer.class);
        Field[] declaredFields = {field1, field2, field3};

        // Get ordered instance fields
        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verify that fields are sorted correctly
        assertEquals(3, orderedFields.length);
        assertEquals(field2, orderedFields[0]);
        assertEquals(field3, orderedFields[1]);
        assertEquals(field1, orderedFields[2]);
    }

    @Test
    public void testOrderedInstanceFieldsFrom_emptyFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Setup mocks
        Class<?> awaitingInjectionClazz = Object.class;

        // Get ordered instance fields from empty fields
        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verify that no fields are returned
        assertEquals(0, orderedFields.length);
    }

}