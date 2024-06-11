package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockFactory;
import org.mockito.plugins.MockitoCore;
import org.mockito.plugins.MockitoCoreProcessor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        MockFactory mockitoMockFactory = MockitoCoreProcessor.getMockFactory();
        MockitoCore mockitoCore = new MockitoCore();

        Class<?> testClass = getClassWithInjectMocks(DefaultInjectionEngineTest.class);

        Set<Field> injectMocksFields = new HashSet<>();
        Field field1 = TestUtils.getDeclaredField(testClass, "field1");
        Field field2 = TestUtils.getDeclaredField(testClass, "field2");
        Field field3 = TestUtils.getDeclaredField(testClass, "field3");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);

        Object testClassInstance = createTestClassInstance(testClass);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mockitoMockFactory.createMock(String.class, new MockCreationSettings().name("mock1"));
        Object mock2 = mockitoMockFactory.createMock(Integer.class, new MockCreationSettings().name("mock2"));
        Object mock3 = mockitoMockFactory.createMock(List.class, new MockCreationSettings().name("mock3"));
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Test field1
        Assert.assertNotNull(field1.get(testClassInstance));
        Assert.assertEquals(mock1, field1.get(testClassInstance));

        // Test field2
        Assert.assertNotNull(field2.get(testClassInstance));
        Assert.assertEquals(mock2, field2.get(testClassInstance));

        // Test field3
        Assert.assertNotNull(field3.get(testClassInstance));
        Assert.assertEquals(mock3, field3.get(testClassInstance));

        // Test that mocks are only injected once
        Assert.assertEquals(3, mocks.size());
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFields_MultipleMocksForSameType() {
        MockFactory mockitoMockFactory = MockitoCoreProcessor.getMockFactory();
        MockitoCore mockitoCore = new MockitoCore();

        Class<?> testClass = getClassWithInjectMocks(DefaultInjectionEngineTest.class);

        Set<Field> injectMocksFields = new HashSet<>();
        Field field1 = TestUtils.getDeclaredField(testClass, "field1");
        Field field1_1 = TestUtils.getDeclaredField(testClass, "field1_1");
        injectMocksFields.add(field1);
        injectMocksFields.add(field1_1);

        Object testClassInstance = createTestClassInstance(testClass);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mockitoMockFactory.createMock(String.class, new MockCreationSettings().name("mock1"));
        Object mock2 = mockitoMockFactory.createMock(String.class, new MockCreationSettings().name("mock2"));
        mocks.add(mock1);
        mocks.add(mock2);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // MockitoException is expected
    }

    @Test
    public void testInjectMocksOnFields_MultipleMethodsForSameType() {
        MockFactory mockitoMockFactory = MockitoCoreProcessor.getMockFactory();
        MockitoCore mockitoCore = new MockitoCore();

        Class<?> testClass = getClassWithInjectMocks(DefaultInjectionEngineTest.class);

        Set<Field> injectMocksFields = new HashSet<>();
        Field field1 = TestUtils.getDeclaredField(testClass, "field1");
        Field field2 = TestUtils.getDeclaredField(testClass, "field2");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);

        Object testClassInstance = createTestClassInstance(testClass);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mockitoMockFactory.createMock(String.class, new MockCreationSettings().name("mock1"));
        Object mock2 = mockitoMockFactory.createMock(String.class, new MockCreationSettings().name("mock2"));
        Object mock3 = mockitoMockFactory.createMock(Integer.class, new MockCreationSettings().name("mock3"));
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Test field1
        Assert.assertNotNull(field1.get(testClassInstance));
        Assert.assertEquals(mock1, field1.get(testClassInstance));

        // Test field2
        Assert.assertNotNull(field2.get(testClassInstance));
        Assert.assertEquals(mock2, field2.get(testClassInstance));

        // Test that mock3 is not injected
        Assert.assertNull(testClassInstance.getClass().getDeclaredField("field3").get(testClassInstance));
    }

    @Test
    public void testInjectMockCandidate() {
        MockFactory mockitoMockFactory = MockitoCoreProcessor.getMockFactory();
        MockitoCore mockitoCore = new MockitoCore();

        Class<?> testClass = getClassWithInjectMocks(DefaultInjectionEngineTest.class);

        Set<Object> mocks = new HashSet<>();
        Object mock = mockitoMockFactory.createMock(String.class, new MockCreationSettings().name("mock"));
        mocks.add(mock);

        Object testClassInstance = createTestClassInstance(testClass);

        Field field = TestUtils.getDeclaredField(testClass, "field");
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMockCandidate(testClass.getClass(), mocks, testClassInstance);

        Assert.assertEquals(mock, field.get(testClassInstance));
    }

    @Test
    public void testInjectMockCandidate_NoMatchingMock() {
        MockFactory mockitoMockFactory = MockitoCoreProcessor.getMockFactory();
        MockitoCore mockitoCore = new MockitoCore();

        Class<?> testClass = getClassWithInjectMocks(DefaultInjectionEngineTest.class);

        Set<Object> mocks = new HashSet<>();
        Object mock = mockitoMockFactory.createMock(String.class, new MockCreationSettings().name("mock"));
        mocks.add(mock);

        Object testClassInstance = createTestClassInstance(testClass);

        Field field = TestUtils.getDeclaredField(testClass, "field");

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMockCandidate(int[].class, mocks, testClassInstance);

        Assert.assertNull(field.get(testClassInstance));
    }

    private static Class<?> getClassWithInjectMocks(Class<?> clazz) {
        if (clazz.isAnnotationPresent(InjectMocks.class)) {
            return clazz;
        }
        return clazz.getSuperclass();
    }

    private static Object createTestClassInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.getDeclaredConstructor().newInstance();
    }

    private static Field getDeclaredField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("No such field: " + fieldName, e);
        }
    }

    private static Set<Field> getDeclaredFields(Class<?> clazz) {
        return new HashSet<>(Arrays.asList(clazz.getDeclaredFields()));
    }

    private static Set<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return new HashSet<>(Arrays.asList(FieldUtils.getAllFields(awaitingInjectionClazz, true)));
    }

}

class TestUtils {
    @SuppressWarnings("unchecked")
    static <T> Set<T> getAllFields(Class<T> clazz, boolean accessible) {
        Set<Field> fields = new HashSet<>();
        while (!clazz.equals(Object.class)) {
            Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.getType().isAssignableFrom(clazz)).forEach(fields::add);
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    @SuppressWarnings("unchecked")
    static <T> Field getDeclaredField(Class<?> clazz, String fieldName) {
        try {
            return (Field) clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("No such field: " + fieldName, e);
        }
    }
}

class AnnotatedElementUtils {
    static <T> AnnotatedElement getAnnotation(T annotatedElement, Class<T> annotationClass) {
        return annotatedElement.annotationPresent(annotationClass);
    }
}

class FieldUtils {
    static Set<Field> getAllFields(Class<?> clazz, boolean accessible) {
        Set<Field> fields = new HashSet<>();
        while (!clazz.equals(Object.class)) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}