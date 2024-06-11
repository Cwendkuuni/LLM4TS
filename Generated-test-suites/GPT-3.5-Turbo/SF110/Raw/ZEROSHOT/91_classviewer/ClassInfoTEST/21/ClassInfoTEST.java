import com.jstevh.viewer.ClassInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassInfoTest {

    private ClassInfo classInfo;

    @Before
    public void setup() throws ClassNotFoundException {
        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
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
        Assert.assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        Assert.assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        String[] fields = classInfo.printFields();
        Assert.assertNull(fields);
    }

    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        Assert.assertNotNull(methods);
        Assert.assertEquals(7, methods.length);
    }

    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        Assert.assertNotNull(constructors);
        Assert.assertEquals(2, constructors.length);
    }

    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        Assert.assertNotNull(interfaces);
        Assert.assertEquals(2, interfaces.length);
    }

    @Test
    public void testGetDataMethodsWithMembers() throws NoSuchMethodException {
        Method[] methods = new Method[]{
                ClassInfo.class.getDeclaredMethod("testGetClassName"),
                ClassInfo.class.getDeclaredMethod("testIsAbstract"),
                ClassInfo.class.getDeclaredMethod("testPrintFields"),
                ClassInfo.class.getDeclaredMethod("setup"),
                ClassInfo.class.getDeclaredMethod("testPrintMethods"),
                ClassInfo.class.getDeclaredMethod("testIsInterface"),
        };
        String[] data = ClassInfo.getData(methods);
        Assert.assertNotNull(data);
        Assert.assertEquals(6, data.length);
    }

    @Test
    public void testGetDataClassesWithMembers() {
        Class<?>[] classes = new Class[]{
                ClassInfoTest.class,
                Field.class,
                Method.class,
                Assert.class
        };
        String[] data = ClassInfo.getData(classes);
        Assert.assertNotNull(data);
        Assert.assertEquals(4, data.length);
    }

    @Test
    public void testSrchMethods() {
        String[] foundMethods = classInfo.srchMethods("print");
        Assert.assertNotNull(foundMethods);
        Assert.assertEquals(3, foundMethods.length);
    }

    @Test
    public void testGetFoundMethod() {
        MethodData methodData = classInfo.getFoundMethod(0);
        Assert.assertNotNull(methodData);
        Assert.assertEquals("com.jstevh.viewer.ClassInfo", methodData.getMethClass());
        Assert.assertEquals("isAbstract", methodData.getMethName());
    }
}