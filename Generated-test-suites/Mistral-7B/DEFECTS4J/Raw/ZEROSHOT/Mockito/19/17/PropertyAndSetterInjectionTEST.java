package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.mockitofy.MockitoCore;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Test class for PropertyAndSetterInjection.
 */
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private Class<?> clazzUnderTest;
    private Object objectUnderTest;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        clazzUnderTest = new TestClass();
        objectUnderTest = spy(clazzUnderTest);
    }

    @Test
    public void processInjection_InjectMocksAnnotatedField_NoInjectionOccurred() {
        // Given
        Field injectMocksField = Utils.getDeclaredField(clazzUnderTest, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(TypeA.class), mock(TypeB.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, objectUnderTest, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
        assertNull(injectMocksField.get(objectUnderTest));
    }

    @Test
    public void processInjection_InjectMocksAnnotatedField_TypeBasedCandidateInjected() {
        // Given
        Field injectMocksField = Utils.getDeclaredField(clazzUnderTest, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(TypeA.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, objectUnderTest, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(injectMocksField.get(objectUnderTest) instanceof TypeA);
    }

    @Test(expected = MockitoException.class)
    public void processInjection_InjectMocksAnnotatedField_MultipleCandidates_NameBasedCandidateInjected() {
        // Given
        Field injectMocksField = Utils.getDeclaredField(clazzUnderTest, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(TypeA.class), mock(TypeB.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, objectUnderTest, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(injectMocksField.get(objectUnderTest) instanceof TypeB);
    }

    @Test
    public void processInjection_InjectMocksAnnotatedField_NoCandidateFound() {
        // Given
        Field injectMocksField = Utils.getDeclaredField(clazzUnderTest, "injectMocksField");
        Set<Object> mockCandidates = Collections.emptySet();

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, objectUnderTest, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
        assertNull(injectMocksField.get(objectUnderTest));
    }

    @Test
    public void processInjection_InjectMocksAnnotatedField_InitializationReportReturned() {
        // Given
        Field injectMocksField = Utils.getDeclaredField(clazzUnderTest, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(TypeA.class));

        // When
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        // Then
        assertNull(report);
    }

    @Test(expected = MockitoException.class)
    public void processInjection_InjectMocksAnnotatedField_InitializationFailed() {
        // Given
        Field injectMocksField = Utils.getDeclaredField(clazzUnderTest, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(TypeA.class));

        // When
        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        // Then
        // An exception should be thrown
    }

    @Test
    public void injectMockCandidates_MultipleCandidates_TypeBasedCandidateInjected() {
        // Given
        Class<?> awaitingInjectionClazz = clazzUnderTest.getSuperclass();
        Set<Object> mocks = Sets.newMockSafeHashSet(mock(TypeA.class), mock(TypeB.class));
        Object instance = objectUnderTest;

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(instance instanceof TestClass);
        assertTrue(instance.getClass().isAssignableFrom(TypeA.class));
    }

    @Test
    public void injectMockCandidates_MultipleCandidates_NameBasedCandidateInjected() {
        // Given
        Class<?> awaitingInjectionClazz = clazzUnderTest.getSuperclass();
        Set<Object> mocks = Sets.newMockSafeHashSet(mock(TypeA.class), mock(TypeB.class));
        Object instance = objectUnderTest;

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(instance instanceof TestClass);
        assertTrue(instance.getClass().isAssignableFrom(TypeB.class));
    }

    @Test
    public void injectMockCandidates_NoCandidateFound() {
        // Given
        Class<?> awaitingInjectionClazz = clazzUnderTest.getSuperclass();
        Set<Object> mocks = Collections.emptySet();
        Object instance = objectUnderTest;

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void injectMockCandidatesOnFields_MultipleCandidates_TypeBasedCandidateInjected() {
        // Given
        Class<?> awaitingInjectionClazz = clazzUnderTest.getSuperclass();
        Set<Object> mocks = Sets.newMockSafeHashSet(mock(TypeA.class), mock(TypeB.class));
        Object instance = objectUnderTest;
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(instance instanceof TestClass);
        assertTrue(instance.getClass().isAssignableFrom(TypeA.class));
    }

    @Test
    public void injectMockCandidatesOnFields_MultipleCandidates_NameBasedCandidateInjected() {
        // Given
        Class<?> awaitingInjectionClazz = clazzUnderTest.getSuperclass();
        Set<Object> mocks = Sets.newMockSafeHashSet(mock(TypeA.class), mock(TypeB.class));
        Object instance = objectUnderTest;
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(instance instanceof TestClass);
        assertTrue(instance.getClass().isAssignableFrom(TypeB.class));
    }

    @Test
    public void injectMockCandidatesOnFields_NoCandidateFound() {
        // Given
        Class<?> awaitingInjectionClazz = clazzUnderTest.getSuperclass();
        Set<Object> mocks = Collections.emptySet();
        Object instance = objectUnderTest;
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertFalse(injectionOccurred);
    }

    private static List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }

    private static class TestClass {
        @InjectMocks
        private TypeA injectMocksField;
    }

    private static class TypeA {}

    private static class TypeB {}
}