package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() {
        // Mock setup
        Field field = mock(Field.class);
        Object testClassInstance = new Object();
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        // Stubbing
        when(field.getType()).thenReturn(TestClass.class);

        // Method call
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verification
        verify(field, times(1)).getType();
    }

    @Test
    public void testInjectMocksOnFieldsThrowsException() {
        // Mock setup
        Field field = mock(Field.class);
        Object testClassInstance = new Object();
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        // Stubbing
        when(field.getType()).thenThrow(new MockitoException("Exception"));

        // Method call
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verification
        verify(field, times(1)).getType();
    }

    @Test
    public void testInjectMockCandidate() {
        // Mock setup
        Class<?> awaitingInjectionClazz = TestClass.class;
        Object fieldInstance = new Object();
        Set<Object> mocks = new HashSet<>();
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);

        // Method call
        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Verification
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, any(Field.class), eq(fieldInstance));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        // Mock setup
        Class<?> awaitingInjectionClazz = TestClass.class;

        // Method call
        Field[] fields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verification
        assertEquals(2, fields.length);
        assertEquals(TestClass.class.getDeclaredFields()[0], fields[0]);
        assertEquals(TestClass.class.getDeclaredFields()[1], fields[1]);
    }

    private static class TestClass {
        private String field1;
        private int field2;
    }
}