import static org.junit.Assert.*;
import org.junit.Test;

// Import ClassInfo class to be tested
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNotNull(classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNotNull(classInfo.printMethods());
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNotNull(classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNotNull(classInfo.printInterfaces());
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNotNull(classInfo.srchMethods("equals"));
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNotNull(classInfo.getFoundMethod(0));
    }

}