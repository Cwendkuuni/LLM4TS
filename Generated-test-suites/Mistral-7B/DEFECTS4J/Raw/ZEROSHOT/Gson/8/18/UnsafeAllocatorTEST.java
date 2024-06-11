import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

    private UnsafeAllocator allocator;

    @Test
    @Category(org.junit.experimental.categories.IntegrationTest.class)
    public void testInstanceCreation() throws Exception {
        allocator = UnsafeAllocator.create();

        TestClass1 testClass1 = (TestClass1) allocator.newInstance(TestClass1.class);
        TestClass2 testClass2 = (TestClass2) allocator.newInstance(TestClass2.class);
        TestClass3 testClass3 = (TestClass3) allocator.newInstance(TestClass3.class);

        // Test null argument
        try {
            allocator.newInstance(null);
            org.junit.Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            org.junit.Assert.assertEquals("c cannot be null", e.getMessage());
        }

        // Test non-instantiatable classes
        try {
            allocator.newInstance(AbstractClass.class);
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            org.junit.Assert.assertNull(e.getCause());
        }

        // Test interface classes
        try {
            allocator.newInstance(Interface.class);
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            org.junit.Assert.assertNull(e.getCause());
        }

        // Test non-public constructor classes
        try {
            allocator.newInstance(NonPublicConstructorClass.class);
            org.junit.Assert.fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            org.junit.Assert.assertNull(e.getCause());
        }

        // Test exception in constructor
        try {
            allocator.newInstance(ExceptionThrowingClass.class);
            org.junit.Assert.fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            Throwable cause = e.getCause();
            org.junit.Assert.assertNotNull(cause);
            org.junit.Assert.assertInstanceOf(RuntimeException.class, cause);
        }

        // Test serialized class
        try {
            allocator.newInstance(SerializedClass.class);
            org.junit.Assert.fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            org.junit.Assert.assertEquals("Cannot deserialize", e.getCause().getMessage());
        }

        // Test arbitrary class
        try {
            allocator.newInstance(ArbitraryClass.class);
            org.junit.Assert.fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof NoSuchMethodException) {
                org.junit.Assert.assertEquals("No suitable constructor found", cause.getMessage());
            } else {
                org.junit.Assert.fail("Unexpected cause: " + cause.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestClass1 {}

    @interface Interface {}

    @SuppressWarnings("unused")
    private static class AbstractClass {}

    @SuppressWarnings("unused")
    private static class NonPublicConstructorClass {
        NonPublicConstructorClass() {}

        private NonPublicConstructorClass(int i) {}
    }

    @SuppressWarnings("unused")
    private static class ExceptionThrowingClass extends RuntimeException {}

    @SuppressWarnings("serial")
    private static class SerializedClass implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    @SuppressWarnings("serial")
    private static class ArbitraryClass {
        private static final long serialVersionUID = 1L;

        private ArbitraryClass() {}

        private ArbitraryClass(int i) {}

        public static void method1() {}
    }
}