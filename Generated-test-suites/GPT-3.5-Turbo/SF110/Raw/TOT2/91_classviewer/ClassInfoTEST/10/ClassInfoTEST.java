import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {
    
    @Test
    public void testIsAbstract() {
        // Typical case
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isAbstract());
    }
    
    @Test
    public void testIsInterface() {
        // Typical case
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isInterface());
    }
    
    @Test
    public void testGetClassName() {
        // Typical case
        ClassInfo classInfo = new ClassInfo();
        assertEquals("javax.swing.WindowConstants", classInfo.getClassName());
    }
    
    @Test
    public void testGetClassPackage() {
        // Typical case
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getClassPackage());
    }
    
    @Test
    public void testGetSuperClassName() {
        // Typical case
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getSuperClassName());
    }
    
    // Add more test methods for other public methods in ClassInfo
    
    // Edge cases and exception handling scenarios
    
    @Test
    public void testPrintFields_NoFields() {
        ClassInfo classInfo = new ClassInfo();
        String[] fields = classInfo.printFields();
        assertNull(fields);
    }
    
    @Test
    public void testPrintMethods_NoMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods();
        assertNull(methods);
    }
    
    // Add more edge cases and exception handling scenarios
    
}