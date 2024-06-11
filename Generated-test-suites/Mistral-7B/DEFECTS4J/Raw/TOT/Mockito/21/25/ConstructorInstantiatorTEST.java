import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConstructorInstantiatorTest {

    @Test(expected = InstantiationException.class)
    public <T> void testNoArgConstructor_NullOuterInstance() {
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(String.class);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testNoArgConstructor_ClassWithoutNoArgConstructor() {
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(ClassWithNoNoArgConstructor.class);
        } catch (InstantiationException e) {
            throw new AssertionError("Unexpected InstantiationException", e);
        }
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_NullOuterInstance() {
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithOneArgConstructor.class);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_ClassWithoutConstructor() {
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(ClassWithNoConstructor.class);
        } catch (InstantiationException e) {
            throw new AssertionError("Unexpected InstantiationException", e);
        }
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_ClassWithTwoArgsConstructor() {
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(ClassWithTwoArgsConstructor.class);
        } catch (InstantiationException e) {
            throw new AssertionError("Unexpected InstantiationException", e);
        }
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_ExceptionThrown() {
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(ClassWithThrowingConstructor.class);
        } catch (InstantiationException e) {
            throw new AssertionError("Unexpected InstantiationException", e);
        }
    }

    @Test
    public <T> void testNoArgConstructor_Success() {
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator(new Object());
        T instance = instantiator.newInstance(String.class);
        Assert.assertNotNull(instance);
    }

    @Test
    public <T> void testWithOuterClass_Success() {
        Object outerInstance = new Object();
        ConstructorInstantiator<T> instantiator = new ConstructorInstantiator<>(outerInstance);
        T instance = instantiator.newInstance(ClassWithOneArgConstructor.class);
        Assert.assertNotNull(instance);
    }

    private static class ClassWithNoArgConstructor {
        ClassWithNoArgConstructor() {
        }
    }

    private static class ClassWithOneArgConstructor<S> {
        ClassWithOneArgConstructor(S arg) {
        }
    }

    private static class ClassWithNoConstructor {
        private ClassWithNoConstructor() {
            throw new IllegalStateException();
        }
    }

    private static class ClassWithTwoArgsConstructor<S, I> {
        ClassWithTwoArgsConstructor(S arg1, I arg2) {
        }
    }

    private static class ClassWithThrowingConstructor<S> {
        ClassWithThrowingConstructor(S arg) throws Exception {
        }
    }
}