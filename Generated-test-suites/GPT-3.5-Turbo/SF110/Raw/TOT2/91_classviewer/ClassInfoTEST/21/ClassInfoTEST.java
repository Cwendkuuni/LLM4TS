import org.junit.*;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        // Test if isAbstract method returns correct status
        ClassInfo classInfo = new ClassInfo();
        boolean isAbstract = classInfo.isAbstract();
        assertFalse(isAbstract);
    }

    @Test
    public void testIsInterface() {
        // Test if isInterface method returns correct status
        ClassInfo classInfo = new ClassInfo();
        boolean isInterface = classInfo.isInterface();
        assertFalse(isInterface);
    }

    @Test
    public void testGetClassName() {
        // Test if getClassName method returns correct class name
        ClassInfo classInfo = new ClassInfo();
        String className = classInfo.getClassName();
        assertEquals("javax.swing.WindowConstants", className);
    }

    // Add more test cases for other methods in ClassInfo class

}