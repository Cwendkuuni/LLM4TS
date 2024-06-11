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
import org.mockito.internal.util.reflection.TestFields;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.util.reflection.TestFields.mockField;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private Reporter reporter;
    private Class testClass;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        reporter = new Reporter();
        testClass = TestClassWithInjectMocks.class;
    }

    @Test
    public void shouldInjectMocksUsingSettersAndFields() throws Exception {
        Object testObject = createTestObjectWithInjectMocks();

        Set<Object> mockCandidates = createMockCandidates(testClass);

        propertyAndSetterInjection.processInjection(mockField(TestClassWithInjectMocks.class, "injectMocksField"), testObject, mockCandidates);

        assertMockInstancesInjected(testObject, mockCandidates);
    }

    @Test
    public void shouldFailWhenNoMockCandidatesFound() throws Exception {
        Object testObject = createTestObjectWithInjectMocks();
        Set<Object> emptyMockCandidates = Collections.emptySet();

        try {
            propertyAndSetterInjection.processInjection(mockField(TestClassWithInjectMocks.class, "injectMocksField"), testObject, emptyMockCandidates);
            fail("Should have thrown an exception.");
        } catch (MockitoException e) {
            assertEquals("No mocking details available for class: " + TestClassWithNoMockCandidates.class.getCanonicalName(), e.getMessage());
        }
    }

    @Test
    public void shouldFailWhenMultipleMockCandidatesFound() throws Exception {
        Object testObject = createTestObjectWithInjectMocks();
        Set<Object> multipleMockCandidates = createMultipleMockCandidates(testClass);

        try {
            propertyAndSetterInjection.processInjection(mockField(TestClassWithMultipleMockCandidates.class, "injectMocksField"), testObject, multipleMockCandidates);
            fail("Should have thrown an exception.");
        } catch (MockitoException e) {
            String expectedMessage = String.format("Multiple mock candidates found for field %s in class %s.%s:\n%s%s",
                    "mockField", TestClassWithMultipleMockCandidates.class.getSimpleName(), "injectMocksField",
                    System.lineSeparator(), multipleMockCandidates.toString());
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void shouldInjectMockUsingFieldIfNoSetterFound() throws Exception {
        Object testObject = createTestObjectWithInjectMocks();
        Set<Object> mockCandidates = createMockCandidates(testClass);

        propertyAndSetterInjection.processInjection(mockField(TestClassWithNoSetterForFieldInjection.class, "injectMocksField"), testObject, mockCandidates);

        assertMockInstanceInjectedByField(testObject, TestClassWithNoSetterForFieldInjection.class, "injectedField");
    }

    @Test
    public void shouldInjectMockUsingFieldIfSetterThrowsException() throws Exception {
        Object testObject = createTestObjectWithInjectMocks();
        Set<Object> mockCandidates = createMockCandidates(testClass);

        FieldInjectorExceptionField field = new FieldInjectorExceptionField();
        Whitebox.setInternalState(propertyAndSetterInjection, "reporter", field);

        propertyAndSetterInjection.processInjection(mockField(TestClassWithSetterThrowingException.class, "injectMocksField"), testObject, mockCandidates);

        assertMockInstanceInjectedByField(testObject, TestClassWithSetterThrowingException.class, "injectedField");
    }

    @Test
    public void shouldInjectMockUsingSetterIfFieldCannotBeAccessed() throws Exception {
        Object testObject = createTestObjectWithInjectMocks();
        Set<Object> mockCandidates = createMockCandidates(testClass);

        InvocablePrivateField setterField = new InvocablePrivateField("setInjectedField", TestClassWithPrivateSetterForFieldInjection.class, TestClassWithPrivateSetterForFieldInjection.class.getDeclaredField("injectedField"));
        Whitebox.setInternalState(propertyAndSetterInjection, "reporter", new Reporter());
        Whitebox.setInternalState(propertyAndSetterInjection, "mockCandidateFilter", mockCandidateFilterWithSetterPreference());

        propertyAndSetterInjection.processInjection(mockField(TestClassWithPrivateSetterForFieldInjection.class, "injectMocksField"), testObject, mockCandidates);

        assertMockInstanceInjectedBySetter(testObject, TestClassWithPrivateSetterForFieldInjection.class, "injectedField");
    }

    @Test
    public void shouldInjectMockUsingSetterIfFieldDoesNotExist() throws Exception {
        Object testObject = createTestObjectWithInjectMocks();
        Set<Object> mockCandidates = createMockCandidates(testClass);

        Whitebox.setInternalState(propertyAndSetterInjection, "reporter", new Reporter());
        Whitebox.setInternalState(propertyAndSetterInjection, "mockCandidateFilter", mockCandidateFilterWithSetterPreference());

        propertyAndSetterInjection.processInjection(mockField(TestClassWithNoFieldForFieldInjection.class, "injectMocksField"), testObject, mockCandidates);

        assertNull(Whitebox.getInternalState(testObject, "injectedField"));
    }

    private Object createTestObjectWithInjectMocks() throws Exception {
        return Whitebox.invokeConstructor(testClass, new Object[0]);
    }

    private Set<Object> createMockCandidates(Class clazz) {
        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.add(mock(clazz.getSuperclass()));
        mockCandidates.add(mock(clazz.getDeclaredConstructor().newInstance()));
        return mockCandidates;
    }

    private Set<Object> createMultipleMockCandidates(Class clazz) {
        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.add(mock(clazz.getSuperclass()));
        mockCandidates.add(mock(clazz.getDeclaredConstructor().newInstance()));
        mockCandidates.add(mock(TestClassWithMultipleMockCandidates2.class));
        return mockCandidates;
    }

    private void assertMockInstancesInjected(Object testObject, Set<Object> mockCandidates) {
        for (Field field : testClass.getDeclaredFields()) {
            Object expected = mockCandidates.iterator().next();
            Object actual = Whitebox.getInternalState(testObject, field.getName());
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
    }

    private void assertMockInstanceInjectedByField(Object testObject, Class clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Object expected = mockCandidates(clazz).iterator().next();
        Object actual = Whitebox.getInternalState(testObject, fieldName);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    private void assertMockInstanceInjectedBySetter(Object testObject, Class clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        Object expected = mockCandidates(clazz).iterator().next();
        Object actual = Whitebox.invokeMethod(testObject, "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
        assertEquals(expected, actual);
    }

    private MockCandidateFilter mockCandidateFilterWithSetterPreference() {
        return new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Set<Object> mockCandidates, Field field, Object instance) {
                if (field.isAnnotationPresent(org.mockito.InjectMocks.class) && Whitebox.getInternalState(field, "canBeSetByReflect") != null) {
                    return mockCandidates.stream().findFirst().orElse(null);
                }
                return super.filterCandidate(mockCandidates, field, instance);
            }
        };
    }

    private static Collection<Object> mockCandidates(Class clazz) {
        return Arrays.asList(mock(clazz.getSuperclass()), mock(clazz.getDeclaredConstructor().newInstance()));
    }

    private static class TestClassWithInjectMocks {
        @InjectMocks
        private Field injectMocksField;

        @InjectMocks
        private Field injectedField;
    }

    private static class TestClassWithNoMockCandidates {
    }

    private static class TestClassWithMultipleMockCandidates {
        @InjectMocks
        private Field injectMocksField;

        @InjectMocks
        private Field injectedField1;

        @InjectMocks
        private Field injectedField2;
    }

    private static class TestClassWithMultipleMockCandidates2 {
    }

    private static class TestClassWithNoSetterForFieldInjection {
        @InjectMocks
        private Field injectMocksField;
        private Field injectedField;

        @Inject
        private void setInjectedField(Field field) {
            this.injectedField = field;
        }
    }

    private static class TestClassWithSetterThrowingException {
        @InjectMocks
        private Field injectMocksField;
        private Field injectedField;

        @Inject
        private void setInjectedField(Field field) throws Exception {
            throw new Exception();
        }
    }

    private static class TestClassWithPrivateSetterForFieldInjection {
        @InjectMocks
        private Field injectMocksField;
        private Field injectedField;

        @Inject
        private void setInjectedField(Field field) {
            this.injectedField = field;
        }

        private Field injectedField;

        private Field getInjectedField() {
            return this.injectedField;
        }
    }

    private static class TestClassWithNoFieldForFieldInjection {
        @InjectMocks
        private Field injectMocksField;
    }

    private static class FieldInjectorExceptionField extends Reporter {
        @Override
        public void fieldInitialisationThrewException(Field field, Throwable exception) {
            throw new RuntimeException(exception);
        }

        @Override
        public void cannotInitializeForInjectMocksAnnotation(String fieldName, MockitoException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static class InvocablePrivateField {
        private final String methodName;
        private final Class clazz;
        private final Field field;

        public InvocablePrivateField(String methodName, Class clazz, Field field) {
            this.methodName = methodName;
            this.clazz = clazz;
            this.field = field;
        }

        public Object invokeMethod(Object instance) throws IllegalAccessException, InvocationTargetException {
            field.setAccessible(true);
            return field.invoke(instance, new Object[0]);
        }
    }
}