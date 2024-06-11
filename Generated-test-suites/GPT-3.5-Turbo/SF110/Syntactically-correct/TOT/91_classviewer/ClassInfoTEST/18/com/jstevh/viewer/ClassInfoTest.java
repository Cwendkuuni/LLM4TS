import org.junit.*;
import static org.junit.Assert.*;

public class ClassInfoTest {
    
    private ClassInfo classInfo;
    
    @Before
    public void setUp() {
        classInfo = new ClassInfo();
    }
    
    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }
    
    @Test
    public void testIsInterface() {
        assertTrue(classInfo.isInterface());
    }
    
    @Test
    public void testGetClassName() {
        assertEquals("com.jstevh.viewer.WindowConstants", classInfo.getClassName());
    }
    
    @Test
    public void testGetClassPackage() {
        assertNull(classInfo.getClassPackage());
    }
    
    @Test
    public void testGetSuperClassName() {
        assertNull(classInfo.getSuperClassName());
    }
    
    @Test
    public void testPrintFields() {
        assertNull(classInfo.printFields());
    }
    
    @Test
    public void testPrintMethods() {
        assertNull(classInfo.printMethods());
    }
    
    @Test
    public void testPrintMethodsParam1() {
        assertNull(classInfo.printMethods(1));
    }
    
    @Test
    public void testPrintConstructors() {
        assertNull(classInfo.printConstructors());
    }
    
    @Test
    public void testPrintInterfaces() {
        assertNull(classInfo.printInterfaces());
    }
    
    @Test
    public void testGetDataMemberArray() {
        Member[] members = new Member[0];
        assertNull(ClassInfo.getData(members));
        
        Method[] methods = new Method[1];
        methods[0] = classInfo.getClass().getDeclaredMethod("testIsAbstract");
        String[] expected = {methods[0].toString()};
        assertArrayEquals(expected, ClassInfo.getData(methods));
    }
    
    @Test
    public void testGetDataClassArray() {
        Class[] classes = new Class[0];
        assertNull(ClassInfo.getData(classes));
        
        Class[] interfaces = classInfo.getClass().getInterfaces();
        String[] expected = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            expected[i] = interfaces[i].getName();
        }
        assertArrayEquals(expected, ClassInfo.getData(interfaces));
    }
    
    @Test
    public void testSrchMethods() {
        assertNull(classInfo.srchMethods("test"));
        
        ClassInfo.debug = true;
        assertNotNull(classInfo.srchMethods("print"));
    }
    
    @Test
    public void testSrchMethodsArray() {
        assertNull(classInfo.srchMethods("test", null));
        
        ClassInfo.debug = true;
        assertNotNull(classInfo.srchMethods("print", new String[] {"printMethods"}));
    }
    
    @Test
    public void testGetFoundMethod() {
        assertNull(classInfo.getFoundMethod(0));
        
        ClassInfo.debug = true;
        assertNotNull(classInfo.getFoundMethod(0));
    }
    
    @After
    public void tearDown() {
        classInfo = null;
    }
}