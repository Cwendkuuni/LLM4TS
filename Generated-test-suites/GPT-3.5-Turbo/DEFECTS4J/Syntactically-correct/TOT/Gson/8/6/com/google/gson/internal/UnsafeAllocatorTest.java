import static org.junit.Assert.*;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setUp() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @After
    public void tearDown() {
        unsafeAllocator = null;
    }
    
    @Test
    public void testCreate() {
        assertNotNull(unsafeAllocator);
    }

    @Test
    public void testNewInstance_JVM() throws Exception {
        // Typical use case
        Class<?> classType = Class.forName("java.util.ArrayList");
        assertNotNull(unsafeAllocator.newInstance(classType));
        
        // Edge case
        classType = Class.forName("java.lang.Integer");
        assertNotNull(unsafeAllocator.newInstance(classType));
        
        // Error scenario
        classType = null;
        try {
            unsafeAllocator.newInstance(classType);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    public void testNewInstance_DalvikVM_PostGingerbread() throws Exception {
        // Typical use case
        Class<?> classType = Class.forName("java.util.HashSet");
        assertNotNull(unsafeAllocator.newInstance(classType));
        
        // Edge case
        classType = Class.forName("java.lang.Double");
        assertNotNull(unsafeAllocator.newInstance(classType));
        
        // Error scenario
        classType = null;
        try {
            unsafeAllocator.newInstance(classType);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    public void testNewInstance_DalvikVM_PreGingerbread() throws Exception {
        // Typical use case
        Class<?> classType = Class.forName("java.util.HashMap");
        assertNotNull(unsafeAllocator.newInstance(classType));
        
        // Edge case
        classType = Class.forName("java.lang.String");
        assertNotNull(unsafeAllocator.newInstance(classType));
        
        // Error scenario
        classType = null;
        try {
            unsafeAllocator.newInstance(classType);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    public void testNewInstance_GiveUp() {
        // Typical use case
        Class<?> classType = Class.forName("java.util.LinkedList");
        try {
            unsafeAllocator.newInstance(classType);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate " + classType, e.getMessage());
        }
        
        // Edge case
        classType = Class.forName("java.lang.Boolean");
        try {
            unsafeAllocator.newInstance(classType);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate " + classType, e.getMessage());
        }
        
        // Error scenario
        classType = null;
        try {
            unsafeAllocator.newInstance(classType);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate " + classType, e.getMessage());
        }
    }

}