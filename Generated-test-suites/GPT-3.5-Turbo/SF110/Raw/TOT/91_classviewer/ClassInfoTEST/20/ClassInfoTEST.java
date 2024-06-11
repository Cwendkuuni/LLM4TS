import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
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
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printMethods());
    }

    @Test
    public void testPrintMethods_Param() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printMethods(1));
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
    public void testGetData_Member() {
        Member[] members = new Member[0];
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(members));
    }

    @Test
    public void testGetData_Class() {
        Class[] classes = new Class[0];
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(classes));
    }

    @Test
    public void testSrchMethods_Null() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(null));
    }

    @Test
    public void testSrchMethods_String() {
        String[] index = new String[0];
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods("test", index));
    }

    @Test
    public void testGetFoundMethod() {
        MethodData expected = null;
        assertNull(classInfo.getFoundMethod(0));
    }
}