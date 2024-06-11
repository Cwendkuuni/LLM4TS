import static org.junit.Assert.*;
import org.junit.Test;

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
        String[] fields = classInfo.printFields();
        assertEquals(0, fields.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods();
        assertEquals(0, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        String[] constructors = classInfo.printConstructors();
        assertEquals(0, constructors.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        String[] interfaces = classInfo.printInterfaces();
        assertEquals(0, interfaces.length);
    }

    @Test
    public void testGetDataWithMembers() {
        Member[] members = new Member[0]; // Update with relevant data
        String[] data = ClassInfo.getData(members);
        assertNull(data);
    }

    @Test
    public void testGetDataWithClasses() {
        Class[] classes = new Class[0]; // Update with relevant data
        String[] data = ClassInfo.getData(classes);
        assertNull(data);
    }

    @Test
    public void testSearchMethods() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods(null));
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getFoundMethod(0));
    }

}