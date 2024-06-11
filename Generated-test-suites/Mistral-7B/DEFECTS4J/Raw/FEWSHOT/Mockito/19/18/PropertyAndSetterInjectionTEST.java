package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Test class for PropertyAndSetterInjection.
 */
public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Field injectMocksField = testClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoTestClass();
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(Arrays.asList(new MockitoTestClass2(), new MockitoTestClass3()));

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
    }

    @Test
    public void testProcessInjection_NoInjectionOccurred() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Field injectMocksField = testClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoTestClass();
        Set<Object> mockCandidates = Collections.emptySet();

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertFalse(result);
    }

    @Test
    public void testInitializeInjectMocksField() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Field injectMocksField = testClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoTestClass();

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        FieldInitializationReport result = injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // Then
        assertNotNull(result);
        assertEquals(MockitoTestClass.class, result.fieldClass());
        assertNotNull(result.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_Exception() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Field injectMocksField = testClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoTestClass();

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // Then
        fail("Should throw an exception");
    }

    @Test
    public void testInjectMockCandidates() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(Arrays.asList(new MockitoTestClass2(), new MockitoTestClass3()));
        Object instance = new MockitoTestClass();

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.injectMockCandidates(testClass, mockCandidates, instance);

        // Then
        assertTrue(result);
    }

    @Test
    public void testInjectMockCandidates_NoInjectionOccurred() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Set<Object> mockCandidates = Collections.emptySet();
        Object instance = new MockitoTestClass();

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.injectMockCandidates(testClass, mockCandidates, instance);

        // Then
        assertFalse(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(Arrays.asList(new MockitoTestClass2(), new MockitoTestClass3()));
        Object instance = new MockitoTestClass();
        List<Field> orderedInstanceFields = Arrays.asList(testClass.getDeclaredFields());

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);

        // Then
        assertTrue(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields_NoInjectionOccurred() {
        // Given
        Class<?> testClass = MockitoTestClass.class;
        Set<Object> mockCandidates = Collections.emptySet();
        Object instance = new MockitoTestClass();
        List<Field> orderedInstanceFields = Arrays.asList(testClass.getDeclaredFields());

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);

        // Then
        assertFalse(result);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        // Given
        Class<?> testClass = MockitoTestClass.class;

        // When
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        List<Field> result = injection.orderedInstanceFieldsFrom(testClass);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    static class MockitoTestClass {
        @InjectMocks
        private PropertyMocks propertyMocks;

        private Field injectMocksField;

        {
            injectMocksField = Field.class.getDeclaredField("injectMocksField");
            injectMocksField.setAccessible(true);
        }
    }

    static class MockitoTestClass2 {}

    static class MockitoTestClass3 {}

    static class PropertyMocks {}
}