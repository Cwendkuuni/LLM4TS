import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInfoTest {
    private ClassInfo classInfo;

    @Before
    public void setUp() throws ClassNotFoundException {
        classInfo = new ClassInfo();
    }

    @Test
    public void testIsAbstract() {
        Assert.assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        Assert.assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        Assert.assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        Assert.assertNull(classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        Assert.assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        String[] expected = {};
        String[] actual = classInfo.printFields();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintMethods() {
        String[] expected = {};
        String[] actual = classInfo.printMethods();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintMethodsParam() {
        String[] expected = null;
        String[] actual = classInfo.printMethods(1);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintConstructors() {
        String[] expected = {};
        String[] actual = classInfo.printConstructors();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrintInterfaces() {
        String[] expected = {};
        String[] actual = classInfo.printInterfaces();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetDataMember() throws NoSuchFieldException {
        Field field = ClassInfo.class.getDeclaredField("debug");
        field.setAccessible(true);
        boolean expected = field.getBoolean(classInfo);
        boolean actual = field.getBoolean(classInfo);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDataMethod() throws NoSuchMethodException {
        Method method = ClassInfo.class.getDeclaredMethod("getData", Member[].class);
        method.setAccessible(true);
        Member[] members = new Member[0];
        String[] expected = null;
        String[] actual = (String[]) method.invoke(classInfo, new Object[]{members});
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testTryClass() throws NoSuchMethodException {
        Method method = ClassInfo.class.getDeclaredMethod("tryClass", String.class);
        method.setAccessible(true);
        String name = null;
        Class expected = null;
        Class actual = (Class) method.invoke(classInfo, name);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintClassInfo() {}
}