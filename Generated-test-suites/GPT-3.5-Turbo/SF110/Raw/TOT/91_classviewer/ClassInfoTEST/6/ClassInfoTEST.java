import org.junit.*;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        
        assertFalse(classInfo.isAbstract());
    }
    
    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        
        assertFalse(classInfo.isInterface());
    }
    
    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        
        assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());
    }
    
    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        
        assertNull(classInfo.getClassPackage());
    }
    
    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        
        assertNull(classInfo.getSuperClassName());
    }
    
    @Test
    public void testGetClassInfoWithValidClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("javax.swing.JFrame");
            
            assertEquals("javax.swing.JFrame", classInfo.getClassName());
            assertNull(classInfo.getClassPackage());
            assertEquals("javax.swing.WindowConstants", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException should not be thrown");
        }
    }
    
    @Test(expected = ClassNotFoundException.class)
    public void testGetClassInfoWithInvalidClassName() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("InvalidClass");
    }
    
    // Additional test cases for other methods...

}