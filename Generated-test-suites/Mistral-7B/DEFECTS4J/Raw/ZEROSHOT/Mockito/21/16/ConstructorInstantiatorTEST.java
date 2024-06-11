package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.reflection.Whitebox.getInnerInstance;
import static org.mockito.internal.util.reflection.Whitebox.getDeclaredConstructor;
import static org.mockito.internal.util.reflection.Whitebox.setInnerInstance;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorWithNullOuterClassInstance() {
        Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
        Constructor<ConstructorInstantiator> constructor = getDeclaredConstructor(constructorInstantiatorClass, Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertNotNull(constructorInstantiator);
        assertThrows(IllegalArgumentException.class, () -> constructor.newInstance(Object.class));
    }

    @Test
    public void testConstructorWithNotNullOuterClassInstance() {
        Object outerClassInstance = new Object() {
        };
        Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
        Constructor<ConstructorInstantiator> constructor = getDeclaredConstructor(constructorInstantiatorClass, Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(constructorInstantiator);

        Class<DummyClass> dummyClass = DummyClass.class;
        DummyClass instance = constructorInstantiator.newInstance(dummyClass);
        assertNotNull(instance);
    }

    @Test
    public void testNoArgConstructor() {
        Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
        Constructor<ConstructorInstantiator> constructor = getDeclaredConstructor(constructorInstantiatorClass);
        ConstructorInstantiator constructorInstantiator = constructor.newInstance();
        assertNotNull(constructorInstantiator);

        Class<DummyClass> dummyClass = DummyClass.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(dummyClass));
        assertEquals("Unable to create mock instance of 'DummyClass'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testConstructorWithException() {
        Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
        Constructor<ConstructorInstantiator> constructor = getDeclaredConstructor(constructorInstantiatorClass, Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object() {});

        Class<DummyClassWithExceptions> dummyClassWithExceptions = DummyClassWithExceptions.class;
        try {
            constructorInstantiator.newInstance(dummyClassWithExceptions);
        } catch (InstantiationException e) {
            assertThrows(RuntimeException.class, () -> e.getCause());
            assertEquals("Unable to create mock instance of 'DummyClassWithExceptions'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getCause().getMessage());
        }
    }

    @Test
    public void testConstructorWithOuterClass() {
        Object outerClassInstance = new Object() {
        };
        Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
        Constructor<ConstructorInstantiator> constructor = getDeclaredConstructor(constructorInstantiatorClass, Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<DummyClassWithConstructor> dummyClassWithConstructor = DummyClassWithConstructor.class;
        DummyClassWithConstructor instance = constructorInstantiator.newInstance(dummyClassWithConstructor);
        assertNotNull(instance);
        DummyClassWithConstructor outerInstance = (DummyClassWithConstructor) getInnerInstance(constructorInstantiator, "outerClassInstance");
        assertEquals(outerInstance, instance.outerClassInstance);
    }

    @Test
    public void testParamsException() {
        Class<DummyClassWithExceptions> dummyClassWithExceptions = DummyClassWithExceptions.class;
        InstantiationException exception = ConstructorInstantiator.paramsException(dummyClassWithExceptions, new RuntimeException("Test exception"));
        assertEquals("Unable to create mock instance of 'DummyClassWithExceptions'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    private static class DummyClass {

    }

    private static class DummyClassWithConstructor {

        private Object outerClassInstance;

        public DummyClassWithConstructor(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
    }

    private static class DummyClassWithExceptions {

        private Object outerClassInstance;

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public DummyClassWithExceptions() {
            throw new RuntimeException("Test exception");
        }

        public DummyClassWithExceptions(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
    }
}