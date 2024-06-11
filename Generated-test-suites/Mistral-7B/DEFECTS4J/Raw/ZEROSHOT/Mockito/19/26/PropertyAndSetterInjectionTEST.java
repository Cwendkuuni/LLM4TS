package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.TestUtils.getPrivateFieldValue;
import static org.mockito.internal.util.reflection.TestUtils.setPrivateFieldValue;
import static org.mockito.internal.util.reflection.TestUtils.testIsAssignableFrom;
import static org.mockito.internal.util.reflection.TestUtils.testIsInstanceOf;
import static org.mockito.internal.util.reflection.TestUtils.testIsModifiable;
import static org.mockito.internal.util.reflection.TestUtils.testSetAccessible;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    private static final Class<PropertyAndSetterInjection> CLASS_UNDER_TEST = PropertyAndSetterInjection.class;
    private static final String FIELD_NAME_TO_INJECT = "fieldToInject";
    private static final Class<?> FIELD_TYPE_TO_INJECT = String.class;
    private static final Class<?> FIELD_OWNER_CLASS = Object.class;
    private static final MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    private PropertyAndSetterInjection propertyAndSetterInjection;
    private Object fieldOwner;
    private Set<Object> mockCandidates;
    private Field fieldToInject;

    @Before
    public void setUp() throws Exception {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        fieldOwner = new Object();
        mockCandidates = newMockSafeHashSet(getMockCandidatesForClass(FIELD_OWNER_CLASS));
        fieldToInject = getDeclaredField(FIELD_OWNER_CLASS, FIELD_NAME_TO_INJECT);
        setPrivateFieldValue(fieldToInject, fieldOwner, null);
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoMockCandidates() {
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoInjectableFields() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(NonInjectableClass.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoInjectableFieldsWithSameType() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(NonInjectableClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(NonInjectableClass.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoInjectableFieldsWithSameName() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(NonInjectableClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(AnotherNonInjectableClass.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoInjectableFieldsWithNameButDifferentType() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(NonInjectableClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(AnotherNonInjectableClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(DifferentTypeClass.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoInjectableFieldsWithTypeButDifferentName() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(NonInjectableClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(DifferentTypeClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(AnotherNonInjectableClass.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoInjectableFieldsWithSameTypeAndNameButDifferentAccessModifiers() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(PrivateFieldAccessibleClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(AnotherNonInjectableClass.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noInjectionOccursWhenNoInjectableFieldsWithSameTypeAndNameButDifferentAccessModifiersAndDifferentClasses() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(PrivateFieldAccessibleClass.class));
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(AnotherPrivateFieldAccessibleClass.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_injectsFieldWithOnlyOneMockCandidateByType() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(MockCandidateByTypeClass.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
        assertNotNull(getPrivateFieldValue(fieldToInject, fieldOwner));
    }

    @Test
    public void testProcessInjection_injectsFieldWithOnlyOneMockCandidateByName() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(MockCandidateByNameClass.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
        assertNotNull(getPrivateFieldValue(fieldToInject, fieldOwner));
    }

    @Test
    public void testProcessInjection_injectsFieldWithOnlyOneMockCandidateByTypeAndByName() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(MockCandidateByTypeAndByNameClass.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
        assertNotNull(getPrivateFieldValue(fieldToInject, fieldOwner));
    }

    @Test
    public void testProcessInjection_injectsFieldWithSetter() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(MockWithSetterClass.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
        assertNotNull(getPrivateFieldValue(fieldToInject, fieldOwner));
    }

    @Test
    public void testProcessInjection_injectsFieldWithNoSetterButWithFieldInjection() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(MockWithNoSetterClass.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
        assertNotNull(getPrivateFieldValue(fieldToInject, fieldOwner));
    }

    @Test
    public void testProcessInjection_injectsFieldWithSetterAndWithFieldInjection() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(MockWithSetterAndFieldInjectionClass.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
        assertNotNull(getPrivateFieldValue(fieldToInject, fieldOwner));
    }

    @Test
    public void testProcessInjection_injectsFieldWithSetterAndWithFieldInjectionAndDifferentTypes() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(MockWithSetterAndFieldInjectionAndDifferentTypesClass.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates));
        assertNotNull(getPrivateFieldValue(fieldToInject, fieldOwner));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_throwsExceptionWhenInitializationFails() throws Exception {
        addMockCandidateToList(mockCandidates, getMockCandidateForClass(ThrowingInitializationClass.class));
        propertyAndSetterInjection.processInjection(fieldToInject, fieldOwner, mockCandidates);
    }

    private Field getDeclaredField(Class<?> clazz, String fieldName) {
        return TestUtils.getDeclaredField(clazz, fieldName);
    }

    private Object getMockCandidateForClass(Class<?> clazz) {
        return TestUtils.newInstance(clazz);
    }

    private void addMockCandidateToList(Set<Object> mockCandidates, Object mockCandidate) {
        mockCandidates.add(mockCandidate);
    }

    private List<Class<?>> getSuperTypesLast(Class<?> clazz) {
        List<Class<?>> superTypes = new ArrayList<>();
        while (clazz != null) {
            superTypes.add(0, clazz);
            clazz = clazz.getSuperclass();
        }
        return superTypes;
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
        List<Field> orderedFields = new ArrayList<>();
        for (Field field : declaredFields) {
            if (testIsModifiable(field)) {
                if (testIsAssignableFrom(FIELD_TYPE_TO_INJECT, field.getType())) {
                    orderedFields.add(field);
                }
            }
        }
        Collections.sort(orderedFields, new SuperTypesLastSorter());
        return orderedFields;
    }

    private static class NonInjectableClass {}

    private static class AnotherNonInjectableClass {}

    private static class DifferentTypeClass {}

    private static class PrivateFieldAccessibleClass {
        private String field;

        @InjectMocks
        private AnotherPrivateFieldAccessibleClass another;
    }

    private static class AnotherPrivateFieldAccessibleClass {
        private String field;
    }

    private static class MockCandidateByTypeClass {
        @InjectMocks
        private String field;
    }

    private static class MockCandidateByNameClass {
        @InjectMocks
        private String name;
    }

    private static class MockCandidateByTypeAndByNameClass {
        @InjectMocks
        private String name;

        @Inject
        private String type;
    }

    private static class MockWithSetterClass {
        @InjectMocks
        private String field;

        @Inject
        public void setField(String field) {
            this.field = field;
        }
    }

    private static class MockWithNoSetterClass {
        @InjectMocks
        private String field;
    }

    private static class MockWithSetterAndFieldInjectionClass {
        @InjectMocks
        private String field;

        @Inject
        private String fieldToInject;

        public void setField(String field) {
            this.field = field;
        }
    }

    private static class MockWithSetterAndFieldInjectionAndDifferentTypesClass {
        @InjectMocks
        private String field;

        @Inject
        private List<String> list;

        public void setField(String field) {
            this.field = field;
        }
    }

    private static class ThrowingInitializationClass {
        @InjectMocks
        private Object field;

        public Object getField() {
            throw new RuntimeException("Initialization failed.");
        }
    }
}