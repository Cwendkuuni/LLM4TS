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
        assertNull(classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo();
        String[] fields = classInfo.printFields();
        assertNull(fields);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods();
        assertNull(methods);
    }

    @Test
    public void testPrintMethodsInt() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods(1);
        assertNull(methods);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);
    }

    @Test
    public void testGetDataMember() {
        Member member = new Member() {
            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public int getModifiers() {
                return 0;
            }
        };

        String[] data = ClassInfo.getData(new Member[]{member});
        assertNotNull(data);
        assertEquals(1, data.length);
        assertNull(data[0]);
    }

    @Test
    public void testGetDataClass() {
        Class<?> className = int.class;
        String[] data = ClassInfo.getData(new Class[]{className});
        assertNotNull(data);
        assertEquals(1, data.length);
        assertEquals("int", data[0]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] foundMethods = classInfo.srchMethods("test");
        assertNull(foundMethods);
    }

    @Test
    public void testSrchMethodsStringArray() {
        ClassInfo classInfo = new ClassInfo();
        String[] foundMethods = classInfo.srchMethods("test", new String[]{"test"});
        assertNull(foundMethods);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNull(methodData);
    }

    @Test
    public void testMain() throws Exception {
        String[] args = new String[0];
        ClassInfo.main(args);
    }
}