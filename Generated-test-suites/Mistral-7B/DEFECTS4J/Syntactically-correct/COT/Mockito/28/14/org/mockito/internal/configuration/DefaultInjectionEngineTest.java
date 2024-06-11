package org.mockito.internal.configuration.DefaultInjectionEngine; // adjust the package name if needed

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.reflection.FieldAccessOrder;
import org.mockito.internal.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.internal.util.reflection.Instantiator;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockInputOutput;
import org.mockito.stub.MockSettings;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = Instantiator.newInstance(MyTestClass.class);
        Set<Field> injectMocksFields = FieldUtils.getAnnotatedFields(testClassInstance.getClass(), InjectMocks.class, FIELD);

        Set<Object> mocks = new HashSet<>();
        MockSettings mockSettings = new MockSettings();
        mockSettings.setLenient(true);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object expectedMock = null;
            if (field.isAnnotationPresent(Mock.class)) {
                expectedMock = mock(field.getType(), mockSettings);
            } else if (field.isAnnotationPresent(Spy.class)) {
                expectedMock = spy(field.getType(), mockSettings);
            } else {
                Assert.assertNull(field.getType().getAnnotation(InjectMocks.class));
                continue;
            }

            Object actualMock = Whitebox.getInternalState(field, testClassInstance);
            Assert.assertEquals(expectedMock, actualMock);
        }
    }

    @Test(expected = MockitoException.class)
    public void testCannotInitializeForInjectMocksAnnotation() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = asList(new Field() {}).stream()
                .map(field -> new AnnotatedElement() {
                    @InjectMocks
                    public static final Field FIELD = field;
                }).map(Field.class::cast).collect(Collectors.toList())
                .collect(Collectors.toSet())
                .toArray(new Field[0]);

        Set<Object> mocks = new HashSet<>();

        try {
            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        } catch (MockitoException e) {
            throw e;
        }

        // This should not be reached as the test method should throw an exception
        Assert.fail("Should have thrown MockitoException");
    }

    @Test
    public void testNullInjectMocksFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = null;
        Set<Object> mocks = new HashSet<>();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // No exception is thrown and no fields are injected
    }

    @Test
    public void testEmptyInjectMocksFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // No exception is thrown and no fields are injected
    }

    @Test
    public void testInjectMocksOnEmptyClass() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // No exception is thrown and no fields are injected
    }

    @Test
    public void testInjectMocksOnFinalClass() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        injectMocksFields.add(new Field() {
            @InjectMocks
            public static final Field FIELD = new FinalField();
        });

        try {
            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
            Assert.fail("Should have thrown MockitoException");
        } catch (MockitoException e) {
            // Expected exception
        }

        // No mocks are injected
    }

    @Test
    public void testInjectMocksOnAnnotatedAndNonAnnotatedFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        injectMocksFields.add(new Field() {
            @InjectMocks
            public static final Field FIELD = new AnnotatedField();
        });

        injectMocksFields.add(new Field() {
            public static final Field FIELD = new NonAnnotatedField();
        });

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object annotatedFieldMock = Whitebox.getInternalState(injectMocksFields.iterator().next(), testClassInstance);
        Object nonAnnotatedFieldInstance = Whitebox.getInternalState(injectMocksFields.iterator().next().getType().getDeclaredFields()[0], testClassInstance);

        Assert.assertNotNull(annotatedFieldMock);
        Assert.assertNull(nonAnnotatedFieldInstance);
    }

    @Test
    public void testInjectMocksOnFieldsWithSameTypeButDifferentAnnotations() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        injectMocksFields.add(new Field() {
            @Mock
            @InjectMocks
            public static final Field FIELD1 = new Field() {};
        });

        injectMocksFields.add(new Field() {
            @Spy
            @InjectMocks
            public static final Field FIELD2 = new Field() {};
        });

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object field1Mock = Whitebox.getInternalState(injectMocksFields.iterator().next(), testClassInstance);
        Object field2Mock = Whitebox.getInternalState(injectMocksFields.iterator().next().getType().getDeclaredFields()[0], testClassInstance);

        Assert.assertNotNull(field1Mock);
        Assert.assertNotNull(field2Mock);
        Assert.assertEquals(FieldAccessOrder.ALPHABETICAL, Whitebox.getInternalState(DefaultInjectionEngine.class.getDeclaredField("supertypesLast"), injectionEngine));
    }

    @Test
    public void testInjectMocksOnFieldsWithSameTypeButSameAnnotation() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Object testClassInstance = new Object() {};
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();

        injectMocksFields.add(new Field() {
            @InjectMocks
            public static final Field FIELD1 = new Field() {};
        });

        injectMocksFields.add(new Field() {
            @InjectMocks
            public static final Field FIELD2 = new Field() {};
        });

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object field1Mock = Whitebox.getInternalState(injectMocksFields.iterator().next(), testClassInstance);
        Object field2Mock = Whitebox.getInternalState(injectMocksFields.iterator().next().getType().getDeclaredFields()[0], testClassInstance);

        Assert.assertNotNull(field1Mock);
        Assert.assertNotNull(field2Mock);
        Assert.assertEquals(FieldAccessOrder.ALPHABETICAL, Whitebox.getInternalState(DefaultInjectionEngine.class.getDeclaredField("supertypesLast"), injectionEngine));
    }

    static class MyTestClass {
        @InjectMocks
        private MyDependentClass dependentClass;

        @Mock
        private MyInterface mockInterface;
    }

    static class MyDependentClass {
        @InjectMocks
        private AnotherDependentClass anotherDependentClass;

        @Mock
        private AnotherInterface mockAnotherInterface;
    }

    static class FinalField {}

    static class AnnotatedField {
        @InjectMocks
        private AnotherDependentClass anotherDependentClass;
    }

    static class NonAnnotatedField {}

    interface MyInterface {}

    interface AnotherInterface {}

    static class AnotherDependentClass {}
}