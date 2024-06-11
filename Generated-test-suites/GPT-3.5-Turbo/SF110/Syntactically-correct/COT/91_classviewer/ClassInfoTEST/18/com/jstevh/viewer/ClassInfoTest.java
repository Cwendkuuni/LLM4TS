import static org.junit.Assert.*;
import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        // Test case: Class is not abstract
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isAbstract());

        // Test case: Class is abstract
        classInfo = new ClassInfo("AbstractClass");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        // Test case: Class is not an interface
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isInterface());

        // Test case: Class is an interface
        classInfo = new ClassInfo("InterfaceClass");
        assertTrue(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        String className = classInfo.getClassName();

        // Test case: Class name is not null
        assertNotNull(className);

        // Test case: Class name is correct
        assertEquals("com.jstevh.viewer.ClassInfo", className);
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        String classPackage = classInfo.getClassPackage();

        // Test case: Class package is null
        assertNull(classPackage);

        // Test case: Class package is correct
        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        classPackage = classInfo.getClassPackage();
        assertEquals("com.jstevh.viewer", classPackage);
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        String superClassName = classInfo.getSuperClassName();

        // Test case: Class has no super class
        assertNull(superClassName);

        // Test case: Class has a super class
        classInfo = new ClassInfo("SubClass");
        superClassName = classInfo.getSuperClassName();
        assertEquals("com.jstevh.viewer.SuperClass", superClassName);
    }

    // Additional test cases for potential edge cases and exception handling scenarios
    // ...

}