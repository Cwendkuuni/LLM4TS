package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
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

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        injectionEngine.mockCandidateFilter = mockCandidateFilter;
    }

    @Test
    public void testInjectMocksOnFields() {
        Field injectMocksField = mock(Field.class);
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(injectMocksField);

        Set<Object> mocks = new HashSet<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);

        Object testClassInstance = new Object();

        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        when(fieldInitializer.initialize()).thenReturn(mockObject);
        whenNew(FieldInitializer.class).withArguments(testClassInstance, injectMocksField).thenReturn(fieldInitializer);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, injectMocksField, mockObject);
        verify(fieldInitializer, times(1)).initialize();
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldInstanceInitializationFailure() {
        Field injectMocksField = mock(Field.class);
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(injectMocksField);

        Set<Object> mocks = new HashSet<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);

        Object testClassInstance = new Object();

        whenNew(FieldInitializer.class).withArguments(testClassInstance, injectMocksField).thenThrow(new MockitoException());

        Reporter reporter = mock(Reporter.class);
        new ReporterMockitoAnnotations().reporter = reporter;

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(reporter, times(1)).cannotInitializeForInjectMocksAnnotation(any(String.class), any(MockitoException.class));
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = mock(Object.class);

        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] fields = {field1, field2};

        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(fields);
        whenNew(FieldInitializer.class).withArguments(any(), any()).thenReturn(mock(FieldInitializer.class));

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(injectionEngine.mockCandidateFilter, times(1)).filterCandidate(mocks, field1, fieldInstance);
        verify(injectionEngine.mockCandidateFilter, times(1)).filterCandidate(mocks, field2, fieldInstance);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] fields = {field1, field2};

        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(fields);
        when(field1.getType()).thenReturn(Object.class);
        when(field2.getType()).thenReturn(String.class);

        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertEquals(field2, orderedFields[0]);
        assertEquals(field1, orderedFields[1]);
    }
}