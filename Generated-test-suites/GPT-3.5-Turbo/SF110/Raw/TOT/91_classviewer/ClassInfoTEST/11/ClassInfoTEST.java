import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

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
        assertArrayEquals(expected, classInfo.printMethods(2));
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
        String[] expected = null;
        assertArrayEquals(expected, classInfo.getData(new Member[0]));
    }
    
    @Test
    public void testGetData_Class() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.getData(new Class[0]));
    }
    
    @Test
    public void testSrchMethods_Null() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(null));
    }
    
    @Test
    public void testSrchMethods_String() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods("test"));
    }
    
    @Test
    public void testSrchMethods_StringAndIndex() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods("test", new String[0]));
    }
    
    @Test
    public void testGetFoundMethod() {
        assertNull(classInfo.getFoundMethod(0));
    }
}