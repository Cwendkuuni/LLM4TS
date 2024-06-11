package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Test with a simple Java class
        String result = (String) allocator.newInstance(String.class);
        assertInstanceOf(String.class, result);

        // Test with an interface
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(Object.class);
        });

        // Test with an abstract class
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(AbstractClass.class);
        });

        // Test with a final class
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(FinalClass.class);
        });

        // Test with an inner class
        assertThrows(UnsupportedOperationException.class, () -> {
            Class<?> outerClass = OuterClass.class;
            Class<?> innerClass = outerClass.getDeclaredClass("InnerClass");
            innerClass.setAccessible(true);
            allocator.newInstance(innerClass);
        });

        // Test with a class with private constructor
        assertThrows(IllegalAccessException.class, () -> {
            Class<?> privateConstructorClass = PrivateConstructorClass.class;
            allocator.newInstance(privateConstructorClass);
        });

        // Test with a class with a private non-default constructor
        assertThrows(IllegalAccessException.class, () -> {
            Class<?> privateNonDefaultConstructorClass = PrivateNonDefaultConstructorClass.class;
            Class<?>[] constructors = privateNonDefaultConstructorClass.getConstructors();
            Method method = constructors[0].getDeclaredConstructor((Class[]) new Class[] {String.class, int.class});
            method.setAccessible(true);
            allocator.newInstance(privateNonDefaultConstructorClass);
        });

        // Test with a class with a protected constructor
        assertThrows(IllegalAccessException.class, () -> {
            Class<?> protectedConstructorClass = ProtectedConstructorClass.class;
            allocator.newInstance(protectedConstructorClass);
        });

        // Test with a class whose constructor throws an exception
        assertThrows(RuntimeException.class, () -> {
            Class<?> exceptionThrowingClass = ExceptionThrowingClass.class;
            allocator.newInstance(exceptionThrowingClass);
        });

        // Test with a class whose constructor has a custom access level
        assertThrows(IllegalAccessException.class, () -> {
            Class<?> customAccessClass = CustomAccessClass.class;
            Method method = customAccessClass.getDeclaredConstructor(String.class);
            method.setAccessible(true);
            allocator.newInstance(customAccessClass);
        });

        // Test with a class whose constructor has a generic type
        @SuppressWarnings("unchecked")
        Class<MyList<String>> myListClass = (Class<MyList<String>>) Class.forName("com.google.gson.internal.MyList");
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(myListClass);
        });
    }

    private static class AbstractClass {}

    private static class FinalClass {}

    private static class OuterClass {
        private static class InnerClass {}
    }

    private static class PrivateConstructorClass {
        private PrivateConstructorClass() {}
    }

    private static class PrivateNonDefaultConstructorClass {
        private PrivateNonDefaultConstructorClass(String name, int value) {}
    }

    private static class ProtectedConstructorClass {
        protected ProtectedConstructorClass() {}
    }

    private static class ExceptionThrowingClass {
        private ExceptionThrowingClass() throws RuntimeException {}
    }

    private static class CustomAccessClass {
        @SuppressWarnings("unused")
        private CustomAccessClass(String name) {
            this.name = name;
        }

        private String name;

        private CustomAccessClass(String name, int access) {
            this(name);
            this.access = access;
        }

        private int access;
    }

    private static class MyList<T> {}
}