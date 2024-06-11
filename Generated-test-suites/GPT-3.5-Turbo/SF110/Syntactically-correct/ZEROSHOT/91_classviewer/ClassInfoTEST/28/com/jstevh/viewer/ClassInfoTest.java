import org.junit.*;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        boolean isAbstract = classInfo.isAbstract();
        assertFalse(isAbstract);
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        boolean isInterface = classInfo.isInterface();
        assertFalse(isInterface);
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        String className = classInfo.getClassName();
        assertEquals("com.jstevh.viewer.ClassInfo", className);
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        String classPackage = classInfo.getClassPackage();
        assertNull(classPackage);
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        String superClassName = classInfo.getSuperClassName();
        assertNull(superClassName);
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo();
        String[] fields = classInfo.printFields();
        assertNull(fields);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods();
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintMethods_param() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.srchMethods("test");
        assertNull(methods);
    }

    @Test
    public void testSrchMethods_param() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.srchMethods("test", new String[]{"index"});
        assertNull(methods);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNull(methodData);
    }
}