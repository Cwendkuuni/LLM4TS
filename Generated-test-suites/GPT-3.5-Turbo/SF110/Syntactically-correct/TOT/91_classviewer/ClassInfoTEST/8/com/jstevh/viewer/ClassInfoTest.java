import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;

public class ClassInfoTest {
    private ClassInfo classInfo;
    
    @Before
    public void setUp() throws Exception {
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
        String[] expectedFields = null;
        assertArrayEquals(expectedFields, classInfo.printFields());
    }
    
    @Test
    public void testPrintMethods() {
        String[] expectedMethods = null;
        assertArrayEquals(expectedMethods, classInfo.printMethods());
    }
    
    @Test
    public void testPrintMethodsInt() {
        String[] expectedMethods = null;
        assertArrayEquals(expectedMethods, classInfo.printMethods(0));
    }
    
    @Test
    public void testPrintConstructors() {
        String[] expectedConstructors = null;
        assertArrayEquals(expectedConstructors, classInfo.printConstructors());
    }
    
    @Test
    public void testPrintInterfaces() {
        String[] expectedInterfaces = null;
        assertArrayEquals(expectedInterfaces, classInfo.printInterfaces());
    }
    
    @Test
    public void testGetDataMember() {
        Member[] members = null;
        String[] expectedData = null;
        assertArrayEquals(expectedData, classInfo.getData(members));
    }
    
    @Test
    public void testGetDataClass() {
        Class[] classes = null;
        String[] expectedData = null;
        assertArrayEquals(expectedData, classInfo.getData(classes));
    }
    
    @Test
    public void testSrchMethodsString() {
        String tempStr = null;
        String[] expectedMethods = null;
        assertArrayEquals(expectedMethods, classInfo.srchMethods(tempStr));
    }
    
    @Test
    public void testSrchMethodsStringStringArray() {
        String tempStr = null;
        String[] index = null;
        String[] expectedMethods = null;
        assertArrayEquals(expectedMethods, classInfo.srchMethods(tempStr, index));
    }
    
    @Test
    public void testGetFoundMethod() {
        int pos = 0;
        MethodData expectedMethod = null;
        assertEquals(expectedMethod, classInfo.getFoundMethod(pos));
    }
}