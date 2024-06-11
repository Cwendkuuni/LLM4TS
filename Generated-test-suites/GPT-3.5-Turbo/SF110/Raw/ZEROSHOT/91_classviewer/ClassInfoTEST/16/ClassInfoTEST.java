import org.junit.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        Assert.assertEquals(false, classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        Assert.assertEquals(false, classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        Assert.assertEquals("javax.swing.WindowConstants", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        Assert.assertEquals("javax.swing", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        Assert.assertEquals(null, classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo();
        String[] expected = null;
        Assert.assertArrayEquals(expected, classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] expected = null;
        Assert.assertArrayEquals(expected, classInfo.printMethods());
    }

    @Test
    public void testPrintMethodsInt() {
        ClassInfo classInfo = new ClassInfo();
        String[] expected = null;
        Assert.assertArrayEquals(expected, classInfo.printMethods(1));
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        String[] expected = null;
        Assert.assertArrayEquals(expected, classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        String[] expected = null;
        Assert.assertArrayEquals(expected, classInfo.printInterfaces());
    }

    @Test
    public void testGetData() {
        Member[] members = new Member[0];
        String[] expected = null;
        Assert.assertArrayEquals(expected, ClassInfo.getData(members));
    }

    @Test
    public void testGetData2() {
        Class[] classes = new Class[0];
        String[] expected = null;
        Assert.assertArrayEquals(expected, ClassInfo.getData(classes));
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] expected = null;
        Assert.assertArrayEquals(expected, classInfo.srchMethods(null));
    }

    @Test
    public void testSrchMethods2() {
        ClassInfo classInfo = new ClassInfo();
        String[] index = new String[0];
        String[] expected = null;
        Assert.assertArrayEquals(expected, classInfo.srchMethods(null, index));
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        ClassInfo.MethodData expected = null;
        Assert.assertEquals(expected, classInfo.getFoundMethod(0));
    }
}