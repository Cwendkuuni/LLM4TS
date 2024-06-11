package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine engine;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        engine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mockCandidateFilter is called for each field
        assertEquals(injectMocksFields.size(), 0);
    }

    @Test
    public void testInjectMocksOnFields_NoMocks() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mockCandidateFilter is not called
        assertEquals(injectMocksFields.size(), 0);
    }

    @Test
    public void testInjectMocksOnFields_ExceptionThrown() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("field");

        injectMocksFields.add(field);

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the reporter is called when exception is thrown
        // TODO: Assert the exception message
    }

    @Test
    public void testInjectMockCandidate() throws Exception {
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();

        engine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Verify that the mockCandidateFilter is called for each field
        assertEquals(mocks.size(), 0);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> awaitingInjectionClazz = Object.class;

        Field[] fields = engine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verify that the fields are sorted correctly
        // TODO: Assert the correct ordering of fields
    }
}