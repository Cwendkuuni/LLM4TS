import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        // Test for abstract class
        ClassInfo classInfo = new ClassInfo("AbstractClass");
        assertTrue(classInfo.isAbstract());
        
        // Test for non-abstract class
        classInfo = new ClassInfo("ConcreteClass");
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        // Test for interface class
        ClassInfo classInfo = new ClassInfo("InterfaceClass");
        assertTrue(classInfo.isInterface());
        
        // Test for non-interface class
        classInfo = new ClassInfo("ConcreteClass");
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("TestClass");
        assertEquals("TestClass", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("TestClass");
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("SubClass");
        assertEquals("BaseClass", classInfo.getSuperClassName());
    }

    // Additional test cases for other methods in ClassInfo class
    
}