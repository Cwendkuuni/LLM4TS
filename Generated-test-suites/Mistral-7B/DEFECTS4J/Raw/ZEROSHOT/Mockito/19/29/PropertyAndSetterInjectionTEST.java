package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.internal.util.reflection.Util.getField;
import static org.mockito.internal.util.reflection.Util.getFields;
import static org.mockito.internal.util.reflection.Util.setField;

/**
 * PropertyAndSetterInjectionTest class.
 */
public class PropertyAndSetterInjectionTest {

    private static final Class<PropertyAndSetterInjection> TESTED_CLASS = PropertyAndSetterInjection.class;

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private MockCandidateFilter mockCandidateFilter;

    @Rule
    public MockitoSystemRule mockitoSystemRule = new MockitoSystemRule();

    @Before
    public void init() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    }

    @Test
    public void testProcessInjection() throws Exception {
        // Given
        Class<?> targetClass = MockitoSystemRule.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));
        mockCandidates.add(mockitoSystemRule.getMock(Integer.class));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(1, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_NoMockFound() throws Exception {
        // Given
        Class<?> targetClass = MockitoSystemRule.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertFalse(result);
    }

    @Test
    public void testProcessInjection_MockNotFoundByName() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithInjectMocksAndFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertFalse(result);
    }

    @Test
    public void testProcessInjection_MockNotFoundByType_Pass1() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithInjectMocksAndFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertFalse(result);

        // When
        result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(0, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MockNotFoundByType_Pass1_MultipleCandidates() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithInjectMocksAndFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.addAll(Arrays.asList(
            mockitoSystemRule.getMock(String.class),
            mockitoSystemRule.getMock(String.class)
        ));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(1, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MockNotFoundByName_Pass1() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithInjectMocksAndFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertFalse(result);

        // When
        result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(0, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MockNotFoundByName_Pass1_MultipleCandidates() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithInjectMocksAndFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.addAll(Arrays.asList(
            mockitoSystemRule.getMock(String.class),
            mockitoSystemRule.getMock(String.class)
        ));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(1, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MockFoundByType() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithInjectMocksAndFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(0, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MockFoundByName() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithInjectMocksAndFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField = getField(target, "injectMocksField");
        Object injectMocksFieldOwner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(0, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));

        // When
        Field targetField = getField(target, "targetField");
        Object targetFieldOwner = target;
        Field otherField = getField(target, "otherField");
        Object otherFieldOwner = target;
        mockCandidates.clear();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));
        mockCandidates.add(mockitoSystemRule.getMock(Integer.class));

        // When
        result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(1, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
        assertNull(targetField.get(targetFieldOwner));
        assertNull(otherField.get(otherFieldOwner));

        // Given
        setField(targetFieldOwner, "targetField", mockitoSystemRule.getMock(String.class));

        // When
        result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(0, mockCandidates.size());
        assertNotNull(injectMocksField.get(injectMocksFieldOwner));
        assertNotNull(targetField.get(targetFieldOwner));
        assertNull(otherField.get(otherFieldOwner));
    }

    @Test
    public void testProcessInjection_MultipleFields() throws Exception {
        // Given
        Class<?> targetClass = TestClassWithMultipleInjectMocksFields.class;
        Object target = mockitoSystemRule.getMock(targetClass);
        Field injectMocksField1 = getField(target, "injectMocksField1");
        Object injectMocksField1Owner = target;
        Field injectMocksField2 = getField(target, "injectMocksField2");
        Object injectMocksField2Owner = target;
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockitoSystemRule.getMock(String.class));

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField1, injectMocksField1Owner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(0, mockCandidates.size());
        assertNotNull(injectMocksField1.get(injectMocksField1Owner));
        assertNull(injectMocksField2.get(injectMocksField2Owner));

        // Given
        setField(injectMocksField2Owner, "injectMocksField2", mockitoSystemRule.getMock(Integer.class));

        // When
        result = propertyAndSetterInjection.processInjection(injectMocksField1, injectMocksField1Owner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(0, mockCandidates.size());
        assertNotNull(injectMocksField1.get(injectMocksField1Owner));
        assertNotNull(injectMocksField2.get(injectMocksField2Owner));
    }

    private static class TestClassWithInjectMocksAndFields {
        @InjectMocks
        private String injectMocksField;

        private String targetField;
        private Integer otherField;
    }

    private static class TestClassWithMultipleInjectMocksFields {
        @InjectMocks
        private String injectMocksField1;

        @InjectMocks
        private Integer injectMocksField2;

        private String field1;
        private Integer field2;
    }
}