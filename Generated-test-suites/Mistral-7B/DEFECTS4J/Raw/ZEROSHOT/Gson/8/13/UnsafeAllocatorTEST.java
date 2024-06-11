import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UnsafeAllocatorTest {

    private UnsafeAllocator allocator = UnsafeAllocator.create();

    @Test
    public void testNewInstanceWithSupportedClasses() throws Exception {
        Class<?> stringClass = String.class;
        String instance = allocator.newInstance(stringClass);
        Assert.assertNotNull(instance);
        Assert.assertEquals("", instance);

        Class<?> intType = Integer.TYPE;
        Integer instanceInt = allocator.newInstance(intType);
        Assert.assertNotNull(instanceInt);
        Assert.assertEquals(0, instanceInt.intValue());

        Class<?> stringArrayClass = String[].class;
        String[] array = (String[]) allocator.newInstance(stringArrayClass);
        Assert.assertNotNull(array);
        Assert.assertArrayEquals(new String[0], array);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceWithInterface() throws Exception {
        Class<?> stringListenerClass = new Class<StringConsumer>() {}.getInterfaces()[0];
        allocator.newInstance(stringListenerClass);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceWithAbstractClass() throws Exception {
        Class<?> abstractClass = AbstractClass.class;
        allocator.newInstance(abstractClass);
    }

    @Test
    public void testNewInstanceWithPrivateConstructor() throws Exception {
        Class<?> privateConstructorClass = PrivateConstructorClass.class;
        Constructor<PrivateConstructorClass> constructor = privateConstructorClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        PrivateConstructorClass instance = allocator.newInstance(privateConstructorClass);
        Assert.assertNotNull(instance);
        Assert.assertEquals(constructor.newInstance().getValue(), instance.getValue());
    }

    @Test
    public void testNewInstanceWithFinalClass() throws Exception {
        Class<?> finalClass = FinalClass.class;
        allocator.newInstance(finalClass);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceWithFinalizer() throws Exception {
        Class<?> finalizerClass = FinalizerClass.class;
        allocator.newInstance(finalizerClass);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceWithAnnotation() throws Exception {
        Class<?> annotatedClass = AnnotatedClass.class;
        allocator.newInstance(annotatedClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithException() throws Exception {
        Class<?> exceptionClass = ExceptionClass.class;
        allocator.newInstance(exceptionClass);
    }

    @Test
    public void testNewInstanceWithInvokedCtor() throws Exception {
        Class<?> stringBuilderClass = StringBuilder.class;
        StringBuilder instance = new StringBuilder();
        StringBuilder instanceThroughUnsafeAllocator = (Buildable) allocator.newInstance(stringBuilderClass);
        Assert.assertNotNull(instanceThroughUnsafeAllocator);
        Assert.assertEquals(instance.capacity(), instanceThroughUnsafeAllocator.capacity());
        Assert.assertEquals(instance.length(), instanceThroughUnsafeAllocator.length());
    }

    private static class AbstractClass {
        // abstract class
    }

    private static class PrivateConstructorClass {
        private int value;

        private PrivateConstructorClass() {
            this.value = 42;
        }

        public int getValue() {
            return value;
        }
    }

    private static class FinalClass {
        // final class
    }

    private static class FinalizerClass {
        // class with finalizer
    }

    private static class AnnotatedClass {
        // class annotated with @SuppressedWarnings("unchecked")
    }

    private static class ExceptionClass extends Exception {
        // exception class
    }

    private static interface StringConsumer {
        void accept(String s);
    }

    private static class Buildable extends StringBuilder {
        // StringBuilder with a custom name
    }
}