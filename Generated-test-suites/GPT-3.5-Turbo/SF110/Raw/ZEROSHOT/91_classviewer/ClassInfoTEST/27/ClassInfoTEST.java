import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ClassInfoTest {
    
    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(false, classInfo.isAbstract());
    }
    
    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(false, classInfo.isInterface());
    }
    
    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());
    }
    
    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }
    
    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(null, classInfo.getSuperClassName());
    }
    
    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(null, classInfo.printFields());
    }
    
    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(null, classInfo.printMethods());
    }
    
    @Test
    public void testPrintMethodsIntParam() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(null, classInfo.printMethods(1));
    }
    
    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(null, classInfo.printConstructors());
    }
    
    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(null, classInfo.printInterfaces());
    }
    
    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(null, classInfo.getFoundMethod(1));
    }
    
}