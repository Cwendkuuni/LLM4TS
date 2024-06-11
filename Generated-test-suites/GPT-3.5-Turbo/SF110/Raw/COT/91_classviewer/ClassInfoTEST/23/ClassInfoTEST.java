import org.junit.Assert;
import org.junit.Test;

public class ClassInfoTest {
    
    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        boolean isAbstract = classInfo.isAbstract();
        Assert.assertFalse(isAbstract);
    }
    
    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        boolean isInterface = classInfo.isInterface();
        Assert.assertFalse(isInterface);
    }
    
    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        String className = classInfo.getClassName();
        Assert.assertEquals("javax.swing.WindowConstants", className);
    }
    
    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        String classPackage = classInfo.getClassPackage();
        Assert.assertNull(classPackage);
    }
    
    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        String superClassName = classInfo.getSuperClassName();
        Assert.assertNull(superClassName);
    }
    
    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo();
        String[] fields = classInfo.printFields();
        Assert.assertNull(fields);
    }
    
    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.printMethods();
        Assert.assertNull(methods);
    }
    
    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo();
        String[] constructors = classInfo.printConstructors();
        Assert.assertNull(constructors);
    }
    
    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo();
        String[] interfaces = classInfo.printInterfaces();
        Assert.assertNull(interfaces);
    }
    
    @Test
    public void testGetDataWithMembers() throws NoSuchMethodException {
        ClassInfo classInfo = new ClassInfo();
        Method[] methods = classInfo.getClass().getMethods();
        String[] data = ClassInfo.getData(methods);
        Assert.assertNotNull(data);
        Assert.assertEquals(methods.length, data.length);
    }
    
    @Test
    public void testGetDataWithClasses() {
        ClassInfo classInfo = new ClassInfo();
        Class[] interfaces = classInfo.getClass().getInterfaces();
        String[] data = ClassInfo.getData(interfaces);
        Assert.assertNotNull(data);
        Assert.assertEquals(interfaces.length, data.length);
    }
    
    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.srchMethods("test");
        Assert.assertNull(methods);
    }
    
    @Test
    public void testSrchMethodsWithIndex() {
        ClassInfo classInfo = new ClassInfo();
        String[] methods = classInfo.srchMethods("test", new String[] {"index"});
        Assert.assertNull(methods);
    }
    
    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        MethodData methodData = classInfo.getFoundMethod(0);
        Assert.assertNull(methodData);
    }
    
}