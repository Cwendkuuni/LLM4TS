import org.junit.*;
import static org.junit.Assert.*;

public class ClassInfoTest {
    
    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isAbstract());        
    }
    
    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isInterface());       
    }
    
    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());       
    }
    
    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getClassPackage());       
    }
    
    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals("javax.swing.WindowConstants", classInfo.getSuperClassName());       
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo();
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintMethodsInt() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods(0);
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);
    }

    @Test
    public void testGetDataMember() {
        ClassInfo classInfo = new ClassInfo();
        String[] data = ClassInfo.getData(classInfo.getClass().getDeclaredFields());
        assertNotNull(data);
        assertEquals(11, data.length);
    }

    @Test
    public void testGetMethodData() {
        ClassInfo classInfo = new ClassInfo();
        String[] data = ClassInfo.getData(classInfo.getClass().getDeclaredMethods());
        assertNotNull(data);
        assertEquals(14, data.length);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] result = classInfo.srchMethods("print");
        assertNull(result);
    }

    @Test
    public void testSrchMethodsWithStringArray() {
        ClassInfo classInfo = new ClassInfo();
        String[] result = classInfo.srchMethods("print", new String[] {"TestClass"});
        assertNull(result);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getFoundMethod(0));
    }
}