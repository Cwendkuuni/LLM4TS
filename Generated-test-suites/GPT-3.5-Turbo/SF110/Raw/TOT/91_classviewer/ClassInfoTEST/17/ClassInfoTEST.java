import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClassInfoTest {

    private ClassInfo classInfo;

    @BeforeEach
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
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printMethods());
    }

    @Test
    public void testPrintMethodsParam() {
        int param = 1;
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printMethods(param));
    }

    @Test
    public void testPrintConstructors() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printInterfaces());
    }

    @Test
    public void testGetDataMember() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.getData(null));
    }

    @Test
    public void testGetDataClass() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.getData(null));
    }

    @Test
    public void testSrchMethods() {
        String tempStr = "methodName";
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(tempStr));
    }

    @Test
    public void testSrchMethodsParam() {
        String tempStr = "methodName";
        String[] index = {"index1", "index2"};
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(tempStr, index));
    }

    @Test
    public void testGetFoundMethod() {
        int pos = 0;
        assertNull(classInfo.getFoundMethod(pos));
    }

}