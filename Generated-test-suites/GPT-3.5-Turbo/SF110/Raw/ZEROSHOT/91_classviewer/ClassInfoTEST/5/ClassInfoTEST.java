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
        int param = 1; // NO_OBJECT_METHODS
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
        String[] member = null;
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(member));
    }
    
    @Test
    public void testGetDataClass() {
        Class[] m = null;
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(m));
    }
    
    @Test
    public void testSrchMethods() {
        String tempStr = null;
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(tempStr));
    }
    
    @Test
    public void testSrchMethodsIndex() {
        String tempStr = null;
        String[] index = null;
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(tempStr, index));
    }
    
    @Test
    public void testGetFoundMethod() {
        int pos = 0;
        assertNull(classInfo.getFoundMethod(pos));
    }
    
    @Test
    public void testMain() throws Exception {
        // Not applicable for this test
    }
}