package org.mockito.internal.configuration;

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

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {
    private DefaultInjectionEngine injectionEngine;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        when(mockCandidateFilter.filterCandidate(any(), any(), any())).thenReturn(mock(MockCandidateFilter.class));
        injectionEngine.mockCandidateFilter = mockCandidateFilter;
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockCandidateFilter, times(2)).filterCandidate(eq(mocks), any(), eq(testClassInstance));
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] orderedFields = {field1, field2};
        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(orderedFields);

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(eq(mocks), eq(field1), eq(fieldInstance));
        verify(mockCandidateFilter).filterCandidate(eq(mocks), eq(field2), eq(fieldInstance));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] declaredFields = {field1, field2};
        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(declaredFields);

        Field[] orderedFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertArrayEquals(new Field[]{field2, field1}, orderedFields);
    }
}