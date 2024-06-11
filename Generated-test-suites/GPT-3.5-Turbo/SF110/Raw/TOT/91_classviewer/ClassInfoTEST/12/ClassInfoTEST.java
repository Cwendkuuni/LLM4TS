import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {
    
    @Test
    public void testIsAbstract() {
        // Typical use case
        ClassInfo info = new ClassInfo();
        assertFalse(info.isAbstract());
        
        // Edge case
        ClassInfo info2 = new ClassInfo("Name");
        assertFalse(info2.isAbstract());
        
        // Error scenario
        info2 = null;
        try {
            info2.isAbstract();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testIsInterface() {
        // Typical use case
        ClassInfo info = new ClassInfo();
        assertFalse(info.isInterface());
        
        // Edge case
        ClassInfo info2 = new ClassInfo("Name");
        assertFalse(info2.isInterface());
        
        // Error scenario
        info2 = null;
        try {
            info2.isInterface();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testGetClassName() {
        // Typical use case
        ClassInfo info = new ClassInfo();
        assertEquals("com.jstevh.viewer.ClassInfo", info.getClassName());
        
        // Edge case
        ClassInfo info2 = new ClassInfo("Name");
        assertEquals("com.jstevh.viewer.ClassInfo", info2.getClassName());
        
        // Error scenario
        info2 = null;
        try {
            info2.getClassName();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testGetClassPackage() {
        // Typical use case
        ClassInfo info = new ClassInfo();
        assertEquals("com.jstevh.viewer", info.getClassPackage());
        
        // Edge case
        ClassInfo info2 = new ClassInfo("Name");
        assertEquals("com.jstevh.viewer", info2.getClassPackage());
        
        // Error scenario
        info2 = null;
        try {
            info2.getClassPackage();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testGetSuperClassName() {
        // Typical use case
        ClassInfo info = new ClassInfo();
        assertNull(info.getSuperClassName());
        
        // Edge case
        ClassInfo info2 = new ClassInfo("Name");
        assertNull(info2.getSuperClassName());
        
        // Error scenario
        info2 = null;
        try {
            info2.getSuperClassName();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    // Additional test methods for other public methods in ClassInfo class
    
}