import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        ClassInfo classInfo = new ClassInfo("Sample", new DirManager());
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
    public void testGetData() {
        Member[] members = new Member[0];
        assertNull(ClassInfo.getData(members));
        
        Class[] classes = new Class[0];
        assertNull(ClassInfo.getData(classes));
    }
    
    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods("test"));
        assertNull(classInfo.srchMethods("test", new String[]{"1", "2", "3"}));
    }
    
    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getFoundMethod(0));
    }
    
    @Test
    public void testTryClass() {
        assertNull(ClassInfo.tryClass(null));
    }
}