import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClassInfoTest {
    private ClassInfo classInfo;

    @Before
    public void setUp() {
        classInfo = new ClassInfo();
    }

    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackageNull() {
        assertNull(classInfo.getClassPackage());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfoWithPackage = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfoWithPackage.getClassPackage());
    }

    @Test
    public void testGetSuperClassNameNull() {
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfoWithSuperClass = new ClassInfo("javax.swing.JFrame");
        assertEquals("java.awt.Frame", classInfoWithSuperClass.getSuperClassName());
    }

    @Test
    public void testPrintFieldsNoFields() {
        String[] fields = classInfo.printFields();
        assertNull(fields);
    }

    @Test
    public void testPrintMethodsNoMethods() {
        String[] methods = classInfo.printMethods();
        assertNull(methods);
    }

    @Test
    public void testPrintMethodsParamNoMethods() {
        String[] methods = classInfo.printMethods(0);
        assertNull(methods);
    }

    @Test
    public void testPrintConstructorsNoConstructors() {
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);
    }

    @Test
    public void testPrintInterfacesNoInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);
    }

    @Test
    public void testGetDataEmptyArray() {
        String[] emptyArray = new String[0];
        String[] data = ClassInfo.getData(emptyArray);
        assertNull(data);
    }

    @Test
    public void testSrchMethodsNull() {
        String[] methods = classInfo.srchMethods(null);
        assertNull(methods);
    }

    @Test
    public void testSrchMethodsNotNull() {
        String[] methods = classInfo.srchMethods("public");
        assertNotNull(methods);
        assertEquals(22, methods.length);
    }

    @Test
    public void testSrchMethodsParamNull() {
        String[] methods = classInfo.srchMethods("public", null);
        assertNull(methods);
    }

    @Test
    public void testSrchMethodsParamNotNull() {
        String[] index = { "com.jstevh.viewer.ClassInfo" };
        String[] methods = classInfo.srchMethods("public", index);
        assertNotNull(methods);
        assertEquals(20, methods.length);
    }

    @Test
    public void testGetFoundMethodNull() {
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNull(methodData);
    }

    // Add more test cases for each method

}