package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.collections.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Whitebox.getInnerInstance;
import static org.mockito.internal.util.reflection.Whitebox.setInnerInstance;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setup() {
        outerClassInstance = new Object() {
        };
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertNull(constructorInstantiator.newInstance(String.class));
    }

    @Test
    public void testNoArgConstructor() {
        assertEquals(new Object(), constructorInstantiator.newInstance(Object.class));
    }

    @Test
    public void testSingleArgConstructor() {
        Class<?> argType = String.class;
        Class<?> argsType = Arrays.asList(argType).toArray(new Class[0]);

        Class<?> cls = ObjectWithStringArg.class;

        Constructor<?> constructor = Arrays.stream(cls.getConstructors())
                .filter(c -> Arrays.equals(argsType, c.getParameterTypes()))
                .findFirst()
                .orElseThrow(() -> new InstantiationException("Constructor not found."));

        Object instance = constructorInstantiator.newInstance(cls);
        assertEquals(outerClassInstance, getInnerInstance(instance, "outerClassInstance"));
        assertEquals("expectedValue", getInnerInstance(instance, "arg"));

        setInnerInstance(instance, "outerClassInstance", null);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(cls));
    }

    @Test
    public void testMultipleArgConstructors() {
        Class<?> argType1 = String.class;
        Class<?> argType2 = Integer.class;
        Class<?> argsType = ArrayUtils.asList(argType1, argType2).toArray(new Class[0]);

        Class<?> cls = ObjectWithTwoStringArgs.class;

        Constructor<?> constructor = Arrays.stream(cls.getConstructors())
                .filter(c -> Arrays.equals(argsType, c.getParameterTypes()))
                .findFirst()
                .orElseThrow(() -> new InstantiationException("Constructor not found."));

        Object instance = constructorInstantiator.newInstance(cls, "arg1", 1);
        assertEquals(outerClassInstance, getInnerInstance(instance, "outerClassInstance"));
        assertEquals("arg1", getInnerInstance(instance, "arg1"));
        assertEquals(1, getInnerInstance(instance, "arg2"));

        setInnerInstance(instance, "outerClassInstance", null);
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(cls, "arg1", 1));
    }

    @Test
    public void testConstructorNotFound() {
        Class<?> cls = ObjectWithNoSuchConstructor.class;
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(cls));
    }

    @Test
    public void testExceptionInConstructor() {
        Class<?> cls = ExceptionThrowingConstructor.class;
        assertThrows(RuntimeException.class, () -> constructorInstantiator.newInstance(cls));
    }

    private static class ObjectWithStringArg {
        private Object outerClassInstance;
        private String arg;

        public ObjectWithStringArg(Object outerClassInstance, String arg) {
            this.outerClassInstance = outerClassInstance;
            this.arg = arg;
        }
    }

    private static class ObjectWithTwoStringArgs {
        private Object outerClassInstance;
        private String arg1;
        private String arg2;

        public ObjectWithTwoStringArgs(Object outerClassInstance, String arg1, int arg2) {
            this.outerClassInstance = outerClassInstance;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }

    private static class ObjectWithNoSuchConstructor {
        private Object outerClassInstance;

        private ObjectWithNoSuchConstructor(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        private ObjectWithNoSuchConstructor(Object outerClassInstance, String arg) {
            this.outerClassInstance = outerClassInstance;
        }
    }

    private static class ExceptionThrowingConstructor {
        private Object outerClassInstance;

        public ExceptionThrowingConstructor(Object outerClassInstance) {
            throw new RuntimeException();
        }
    }
}