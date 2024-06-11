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
    public void testPrintMethodsInt() {
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
    public void testGetDataMemberArray() {
        Member[] members = new Member[2];
        members[0] = new Method();
        members[1] = new Field();
        String[] data = ClassInfo.getData(members);
        assertNotNull(data);
        assertEquals(2, data.length);
    }

    @Test
    public void testGetDataClassArray() {
        Class[] classes = new Class[2];
        classes[0] = String.class;
        classes[1] = Integer.class;
        String[] data = ClassInfo.getData(classes);
        assertNotNull(data);
        assertEquals(2, data.length);
    }

    @Test
    public void testSrchMethodsNullString() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods(null));
    }

    @Test
    public void testSrchMethodsNullIndex() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods("test", null));
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods("test"));
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getFoundMethod(0));
    }

    @Test
    public void testMain() throws Exception {
        ClassInfo.main(new String[0]);
    }
}