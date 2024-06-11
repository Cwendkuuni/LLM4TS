import static org.junit.Assert.*;
import org.junit.Test;

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

    // More test cases for other methods can be added here

    @Test(expected = ClassNotFoundException.class)
    public void testClassInfoConstructorWithInvalidName() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("InvalidClassName");
    }

    @Test
    public void testPrintFieldsNoField() {
        ClassInfo classInfo = new ClassInfo();
        String[] fields = classInfo. printFields();
        assertNull(fields);
    }

    @Test
    public void testPrintMethodsNoMethod() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods();
        assertNull(methods);
    }

    // More test cases for other scenarios can be added here

}