import static org.junit.Assert.*;
import org.junit.*;

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
    public void testGetClassPackage() {
        assertNull(classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        String[] expectedFields = { "Field1=value1", "Field2=value2" }; // Replace with actual expected fields
        assertArrayEquals(expectedFields, classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        String[] expectedMethods = { "method1()", "method2()" }; // Replace with actual expected methods
        assertArrayEquals(expectedMethods, classInfo.printMethods());
    }

    @Test
    public void testPrintMethodsParam1() {
        String[] expectedMethods = { "method1()", "method3()" }; // Replace with actual expected methods
        assertArrayEquals(expectedMethods, classInfo.printMethods(1));
    }

    @Test
    public void testPrintConstructors() {
        String[] expectedConstructors = { "Constructor1()", "Constructor2()" }; // Replace with actual expected constructors
        assertArrayEquals(expectedConstructors, classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        String[] expectedInterfaces = { "Interface1", "Interface2" }; // Replace with actual expected interfaces
        assertArrayEquals(expectedInterfaces, classInfo.printInterfaces());
    }

    @Test
    public void testGetDataMember() {
        String[] expectedMembers = { "Member1", "Member2" }; // Replace with actual expected members
        assertArrayEquals(expectedMembers, classInfo.getData(classInfo.getMembers()));
    }

    @Test
    public void testGetDataMemberClass() {
        String[] expectedMembers = { "Member1Class", "Member2Class" }; // Replace with actual expected members
        assertArrayEquals(expectedMembers, classInfo.getData(classInfo.getMemberClasses()));
    }

    @Test
    public void testSrchMethods() {
        String[] expectedMethods = { "method1()", "method2()" }; // Replace with actual expected methods
        assertArrayEquals(expectedMethods, classInfo.srchMethods("searchTerm"));
    }

    @Test
    public void testSrchMethodsIndex() {
        String[] expectedMethods = { "method1()", "method3()" }; // Replace with actual expected methods
        assertArrayEquals(expectedMethods, classInfo.srchMethods("searchTerm", null));
    }

    @Test
    public void testGetFoundMethod() {
        String expectedMethPackage = "com.jstevh.viewer";
        String expectedMethClass = "Class1";
        String expectedMethName = "method1()";
        MethodData methodData = classInfo.getFoundMethod(0);
        assertEquals(expectedMethPackage, methodData.getMethPackage());
        assertEquals(expectedMethClass, methodData.getMethClass());
        assertEquals(expectedMethName, methodData.getMethName());
    }

    // Add more test methods for remaining methods in ClassInfo class

}