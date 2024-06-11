
import org.junit.Test;
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
        assertEquals("javax.swing.WindowConstants", classInfo.getClassName());
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

    // Additional test cases for other methods can be added here
    
}
