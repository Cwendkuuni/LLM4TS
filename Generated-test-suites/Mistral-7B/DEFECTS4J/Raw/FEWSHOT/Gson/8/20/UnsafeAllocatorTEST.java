package com.google.gson.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class UnsafeAllocatorTest {

    private static final UnsafeAllocator UNSAFE = UnsafeAllocator.create();

    @Test
    public void testNewInstance_Serializable() {
        @SuppressWarnings("unchecked")
        Serializable serializable = (Serializable) UNSAFE.newInstance(Serializable.class);
    }

    @Test
    public void testNewInstance_Interface() {
        @SuppressWarnings("unchecked")
        interface TestInterface {}

        assertFalse("Interface should not be instantiated", UNSAFE instanceof TestInterface);
        assertThrows(UnsupportedOperationException.class, () -> UNSAFE.newInstance(TestInterface.class));
    }

    @Test
    public void testNewInstance_AbstractClass() {
        @SuppressWarnings("unchecked")
        abstract class TestAbstractClass {}

        assertFalse("Abstract class should not be instantiated", UNSAFE instanceof TestAbstractClass);
        assertThrows(UnsupportedOperationException.class, () -> UNSAFE.newInstance(TestAbstractClass.class));
    }

    @Test
    public void testNewInstance_ClassWithPrivateConstructor() {
        final class TestClass {
            private TestClass() {}
        }

        assertThrows(IllegalAccessException.class, () -> UNSAFE.newInstance(TestClass.class));
    }

    @Test
    public void testNewInstance_ClassWithPublicConstructor() {
        final class TestClass {
            public TestClass() {}
        }

        @SuppressWarnings("unchecked")
        TestClass testClass = (TestClass) UNSAFE.newInstance(TestClass.class);
    }

    @Test
    public void testNewInstance_ClassWithDefaultConstructor() {
        final class TestClass {}

        @SuppressWarnings("unchecked")
        TestClass testClass = (TestClass) UNSAFE.newInstance(TestClass.class);
    }

    @Test
    public void testNewInstance_ClassWithPrivateConstructorAndField() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final class TestClass {
            private int field;

            private TestClass() {}

            private TestClass(int field) {
                this.field = field;
            }

            public int getField() {
                return field;
            }
        }

        final Method constructor = TestClass.class.getDeclaredConstructor(int.class);
        constructor.setAccessible(true);

        final Field field = TestClass.class.getDeclaredField("field");
        field.setAccessible(true);

        final int fieldValue = 42;

        @SuppressWarnings("unchecked")
        TestClass testClass = (TestClass) UNSAFE.newInstance(TestClass.class);
        assertNull("Default constructor should not initialize field", testClass.getField());

        @SuppressWarnings("unchecked")
        TestClass testClassWithInit = (TestClass) UNSAFE.newInstance(TestClass.class, new Object[]{fieldValue});
        assertEquals("Field should be initialized", fieldValue, testClassWithInit.getField());
    }
}