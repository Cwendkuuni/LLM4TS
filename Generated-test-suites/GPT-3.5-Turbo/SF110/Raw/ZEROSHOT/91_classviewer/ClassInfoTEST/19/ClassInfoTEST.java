import org.junit.Test;
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
        assertEquals("javax.swing.WindowConstants", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.printMethods());
    }

    @Test
    public void testPrintMethods2() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.printMethods(1));
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.printInterfaces());
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods(null));
    }

    @Test
    public void testSrchMethods2() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods(null, null));
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getFoundMethod(0));
    }

    @Test
    public void testMain() throws Exception {
        // code coverage test only, no assertion needed
        ClassInfo.main(new String[] {});
    }

}