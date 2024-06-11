package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

public class PropertyAndSetterInjectionTest {
    private PropertyAndSetterInjection injectionStrategy;
    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };
    }

    @Test
    public void testProcessInjection() {
        // Set up mock objects
        Field injectMocksField = new Field(getClass().getDeclaredFields()[0].getName(), getClass().getDeclaredFields()[0].getType(), getClass().getDeclaredFields()[0].getGenericType());
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();

        // Invoke the method being tested
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Verify the result
        assertTrue(result);
    }

    @Test
    public void testInitializeInjectMocksField() {
        // Set up mock objects
        Field field = new Field(getClass().getDeclaredFields()[0].getName(), getClass().getDeclaredFields()[0].getType(), getClass().getDeclaredFields()[0].getGenericType());
        Object fieldOwner = new Object();

        // Invoke the method being tested
        FieldInitializationReport result = injectionStrategy.initializeInjectMocksField(field, fieldOwner);

        // Verify the result
        assertEquals(fieldOwner, result.fieldInstance());
    }

    @Test
    public void testInjectMockCandidates() {
        // Set up mock objects
        Class<?> awaitingInjectionClazz = getClass();
        Set<Object> mocks = new HashSet<Object>();
        Object instance = new Object();

        // Invoke the method being tested
        boolean result = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Verify the result
        assertFalse(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields() {
        // Set up mock objects
        Set<Object> mocks = new HashSet<Object>();
        Object instance = new Object();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<Field>();

        // Invoke the method being tested
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);

        // Verify the result
        assertFalse(result);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        // Set up mock objects
        Class<?> awaitingInjectionClazz = getClass();

        // Invoke the method being tested
        List<Field> result = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verify the result
        assertTrue(result.isEmpty());
    }
}