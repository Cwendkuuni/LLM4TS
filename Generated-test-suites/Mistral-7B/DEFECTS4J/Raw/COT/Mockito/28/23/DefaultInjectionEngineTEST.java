package org.mockito.internal.configuration;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class DefaultInjectionEngineTest {

    

    @Test
    public void testInjectMocksOnFields() {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Class testClass = getTestClass();
        Set<Field> injectMocksFields = getInjectMocksFields(testClass);
        Set<Object> mocks = getMocks();
        Object testClassInstance = getTestClassInstance(testClass);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(0, mocks.size());

        for (Field field : injectMocksFields) {
            Object fieldInstance = Whitebox.getInternalState(testClassInstance, field.getName());
            Assert.assertNotNull(fieldInstance);
        }
    }

    @Test(expected = MockitoException.class)
    public void testCannotInitializeInjectMocksField() {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Class testClass = getTestClass();
        Set<Field> injectMocksFields = getInjectMocksFields(testClass);
        Set<Object> mocks = getMocks();
        Object testClassInstance = getTestClassInstance(testClass);

        Field injectMocksField = injectMocksFields.iterator().next();

        Field mockedField = createMockedField(testClass, injectMocksField.getType());
        mocks.add(mockedField.get(null));

        try {
            defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        } catch (Exception e) {
            throw new MockitoException("Unexpected exception", e);
        }

        Assert.fail();
    }

    @Test
    public void testInjectMockCandidateByName() {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Class testClass = getTestClass();
        Set<Object> mocks = getMocks();
        Object testClassInstance = getTestClassInstance(testClass);

        Field injectMocksField = getFieldAnnotatedWithInjectMocks(testClass, "injectMocksField1");
        Object injectMocksFieldInstance = new Object() {};

        Field mockedField = createMockedField(testClass, injectMocksField.getType());
        mocks.add(mockedField.get(null));

        try {
            defaultInjectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClassInstance);
        } catch (MockitoException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(mockedField, Whitebox.getInternalState(injectMocksFieldInstance, "mockedField"));
    }

    @Test
    public void testInjectMockCandidateByType() {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Class testClass = getTestClass();
        Set<Object> mocks = getMocks();
        Object testClassInstance = getTestClassInstance(testClass);

        Field injectMocksField = getFieldAnnotatedWithInjectMocks(testClass, "injectMocksField2");
        Object injectMocksFieldInstance = new Object() {};

        Class mockedClass = getMockedClass();
        Object mockedInstance = mock(mockedClass);
        mocks.add(mockedInstance);

        try {
            defaultInjectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClassInstance);
        } catch (MockitoException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertSame(mockedInstance, Whitebox.getInternalState(injectMocksFieldInstance, "mockedField"));
    }

    @Test
    public void testInjectMockCandidateByTypeWithMultipleCandidates() {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Class testClass = getTestClass();
        Set<Object> mocks = getMocks();
        Object testClassInstance = getTestClassInstance(testClass);

        Field injectMocksField = getFieldAnnotatedWithInjectMocks(testClass, "injectMocksField3");
        Object injectMocksFieldInstance = new Object() {};

        Class mockedClass1 = getMockedClass();
        Object mockedInstance1 = mock(mockedClass1);
        mocks.add(mockedInstance1);

        Class mockedClass2 = getMockedClass();
        Object mockedInstance2 = mock(mockedClass2);
        mocks.add(mockedInstance2);

        try {
            defaultInjectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClassInstance);
        } catch (MockitoException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertSame(mockedInstance1, Whitebox.getInternalState(injectMocksFieldInstance, "mockedField1"));
        Assert.assertSame(mockedInstance2, Whitebox.getInternalState(injectMocksFieldInstance, "mockedField2"));
    }

    @Test(expected = MockitoException.class)
    public void testInjectMockCandidateByNameWithMultipleCandidates() {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Class testClass = getTestClass();
        Set<Object> mocks = getMocks();
        Object testClassInstance = getTestClassInstance(testClass);

        Field injectMocksField = getFieldAnnotatedWithInjectMocks(testClass, "injectMocksField4");
        Object injectMocksFieldInstance = new Object() {};

        Class mockedClass1 = getMockedClass();
        Object mockedInstance1 = mock(mockedClass1);
        mocks.add(mockedInstance1);

        Class mockedClass2 = getMockedClass();
        Object mockedInstance2 = mock(mockedClass2);
        mocks.add(mockedInstance2);

        try {
            defaultInjectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClassInstance);
        } catch (MockitoException e) {
            throw new MockitoException("Unexpected exception", e);
        }

        Assert.fail();
    }

    private Class getTestClass() {
        return TestClass.class;
    }

    private Set<Field> getInjectMocksFields(Class testClass) {
        return Arrays.asList(testClass.getDeclaredFields()
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet()));
    }

    private Set<Object> getMocks() {
        return new HashSet<>();
    }

    private Object getTestClassInstance(Class testClass) {
        try {
            return testClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getFieldAnnotatedWithInjectMocks(Class testClass, String fieldName) {
        return Arrays.stream(testClass.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Field not found: " + fieldName));
    }

    private Field createMockedField(Class testClass, Class fieldType) {
        try {
            Field field = testClass.getDeclaredField("mockedField");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            Field field = new Field() {};
            field.setType(fieldType);
            field.setName("mockedField");
            return field;
        }
    }

    private Class getMockedClass() {
        return MockClass.class;
    }

    static class TestClass {
        @InjectMocks
        private Field1 field1;

        @InjectMocks
        private Field2 field2;

        @InjectMocks
        private Field3 field3;

        @InjectMocks
        private Field4 field4;

        private Field mockedField1;
        private Field mockedField2;
    }

    static class Field1 {
        private Field mockedField;
    }

    static class Field2 {
        private Field mockedField1;
        private Field mockedField2;
    }

    static class Field3 {
        @org.mockito.InjectMocks
        private Field mockedField;
    }

    static class Field4 {
        @org.mockito.InjectMocks
        private Field mockedField1;
        @org.mockito.InjectMocks
        private Field mockedField2;
    }

    static class MockClass {
    }

    
}