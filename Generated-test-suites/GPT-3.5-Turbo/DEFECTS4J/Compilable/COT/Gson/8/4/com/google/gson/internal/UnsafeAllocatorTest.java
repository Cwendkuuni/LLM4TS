package com.google.gson.internal;
import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import com.google.gson.internal.UnsafeAllocator;


public class UnsafeAllocatorTest {
    
    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedNewInstance() throws Exception {
        // Given
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
                throw new UnsupportedOperationException("Cannot allocate " + c);
            }
        };
        
        // When
        allocator.newInstance(Object.class);
    }
    
    @Test
    public void testCreateJVM() throws Exception {
        // Given
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field f = unsafeClass.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Object unsafe = f.get(null);
        final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T newInstance(Class<T> c) throws Exception {
              return (T) allocateInstance.invoke(unsafe, c);
            }
        };
        
        // When
        TestClass obj = allocator.newInstance(TestClass.class);
        
        // Then
        assertNotNull(obj);
        assertEquals(TestClass.class, obj.getClass());
    }
    
    @Test
    public void testCreateDalvikPostGingerbread() throws Exception {
        // Given
        final int constructorId = 0;
        Method newInstance = ObjectStreamClass.class
          .getDeclaredMethod("newInstance", Class.class, int.class);
        newInstance.setAccessible(true);
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T newInstance(Class<T> c) throws Exception {
                return (T) newInstance.invoke(null, c, constructorId);
            }
        };
        
        // When
        TestClass obj = allocator.newInstance(TestClass.class);
        
        // Then
        assertNotNull(obj);
        assertEquals(TestClass.class, obj.getClass());
    }
    
    @Test
    public void testCreateDalvikPreGingerbread() throws Exception {
        // Given
        Method newInstance = ObjectInputStream.class
          .getDeclaredMethod("newInstance", Class.class, Class.class);
        newInstance.setAccessible(true);
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T newInstance(Class<T> c) throws Exception {
                return (T) newInstance.invoke(null, c, Object.class);
            }
        };
        
        // When
        TestClass obj = allocator.newInstance(TestClass.class);
        
        // Then
        assertNotNull(obj);
        assertEquals(TestClass.class, obj.getClass());
    }
    
    public static class TestClass {}
}