import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClassInfoTest {
    
    // ClassInfo object for testing
    private ClassInfo classInfo;
    
    @Before
    public void setUp() throws ClassNotFoundException {
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
    public void testPrintMethods_param() {
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
    public void testGetData_Member() {
        Member[] members = null;
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(members));
    }
    
    @Test
    public void testGetData_Class() {
        Class[] classes = null;
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(classes));
    }
    
    @Test
    public void testSrchMethods() {
        String tempStr = "test";
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(tempStr));
    }
    
    @Test
    public void testSrchMethods_index() {
        String tempStr = "test";
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