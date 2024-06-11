
import static org.junit.Assert.*;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.lang.reflect.Method;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstanceWithUnsafeClass() {
        // Create test class
        TestClass testClass = null;
        try {
            testClass = UnsafeAllocator.create().newInstance(TestClass.class);
        } catch (Exception e) {
            fail("Exception thrown while creating instance with Unsafe: " + e.getMessage());
        }
        
        // Verify instance is not null
        assertNotNull(testClass);
        
        // Verify instance is of correct class
        assertEquals(TestClass.class, testClass.getClass());
        
        // Verify instance's fields are null
        assertNull(testClass.getField1());
        assertNull(testClass.getField2());
    }

    @Test
    public void testNewInstanceWithDalivkvmPreGingerbread() {
        // Set up mocked ObjectInputStream method
        Method newInstanceMethod = null;
        try {
            newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
            newInstanceMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            fail("Exception thrown while setting up mocked newInstance method: " + e.getMessage());
        }
        
        // Set up UnsafeAllocator with mock newInstance method
        UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T newInstance(Class<T> c) throws Exception {
                return (T) newInstanceMethod.invoke(null, c, Object.class);
            }
        };
        
        // Create test class
        TestClass testClass = null;
        try {
            testClass = unsafeAllocator.newInstance(TestClass.class);
        } catch (Exception e) {
            fail("Exception thrown while creating instance with DalvikVM pre-Gingerbread: " + e.getMessage());
        }
        
        // Verify instance is not null
        assertNotNull(testClass);
        
        // Verify instance is of correct class
        assertEquals(TestClass.class, testClass.getClass());
        
        // Verify instance's fields are null
        assertNull(testClass.getField1());
        assertNull(testClass.getField2());
    }

    @Test
    public void testNewInstanceWithDalivkvmPostGingerbread() {
        // Set up mocked ObjectStreamClass methods
        Method getConstructorIdMethod = null;
        Method newInstanceMethod = null;
        try {
            getConstructorIdMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
            getConstructorIdMethod.setAccessible(true);
            
            newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
            newInstanceMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            fail("Exception thrown while setting up mocked newInstance method: " + e.getMessage());
        }
        
        // Set up UnsafeAllocator with mock methods
        UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T newInstance(Class<T> c) throws Exception {
                int constructorId = (Integer) getConstructorIdMethod.invoke(null, Object.class);
                return (T) newInstanceMethod.invoke(null, c, constructorId);
            }
        };
        
        // Create test class
        TestClass testClass = null;
        try {
            testClass = unsafeAllocator.newInstance(TestClass.class);
        } catch (Exception e) {
            fail("Exception thrown while creating instance with DalvikVM post-Gingerbread: " + e.getMessage());
        }
        
        // Verify instance is not null
        assertNotNull(testClass);
        
        // Verify instance is of correct class
        assertEquals(TestClass.class, testClass.getClass());
        
        // Verify instance's fields are null
        assertNull(testClass.getField1());
        assertNull(testClass.getField2());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceUnsupported() {
        UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
                throw new UnsupportedOperationException("Cannot allocate " + c);
            }
        };
        
        try {
            unsafeAllocator.newInstance(TestClass.class);
        } catch (Exception e) {
            fail("Exception thrown while creating instance with unsupported allocator: " + e.getMessage());
        }
    }

    private static class TestClass {
        private String field1;
        private Integer field2;
        
        public String getField1() {
            return field1;
        }
        
        public Integer getField2() {
            return field2;
        }
    }
}
