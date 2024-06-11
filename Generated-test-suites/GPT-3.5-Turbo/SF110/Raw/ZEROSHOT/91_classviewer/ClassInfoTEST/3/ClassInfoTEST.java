import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        String[] fields = classInfo.printFields();
        assertNull(fields);
    }
    
    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        assertNull(methods);
    }
    
    @Test
    public void testPrintMethods_Param() {
        String[] methods = classInfo.printMethods(1);
        assertNull(methods);
    }
    
    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);
    }
    
    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);
    }
    
    @Test
    public void testGetFoundMethod() {
        assertNull(classInfo.getFoundMethod(0));
    }
    
}