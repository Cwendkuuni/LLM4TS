import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        assertNull(classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        assertNull(classInfo.printMethods());
    }

    @Test
    public void testPrintMethodsParam1() {
        assertNull(classInfo.printMethods(1));
    }

    @Test
    public void testPrintConstructors() {
        assertNull(classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        assertNull(classInfo.printInterfaces());
    }

    @Test
    public void testGetDataMemberArray() {
        assertNull(ClassInfo.getData(new Member[0]));
    }

    @Test
    public void testGetDataClassArray() {
        assertNull(ClassInfo.getData(new Class[0]));
    }

    @Test
    public void testSrchMethods() {
        assertNull(classInfo.srchMethods(null));
    }

    @Test
    public void testSrchMethodsParam1Param2() {
        assertNull(classInfo.srchMethods(null, null));
    }
}