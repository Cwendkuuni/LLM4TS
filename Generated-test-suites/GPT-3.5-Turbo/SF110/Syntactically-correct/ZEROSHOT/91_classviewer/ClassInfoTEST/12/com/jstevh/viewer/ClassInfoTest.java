import com.jstevh.viewer.*;
import com.jstevh.tools.*;
import javax.swing.*;
import java.lang.reflect.*;
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
        assertEquals("javax.swing.WindowConstants", classInfo.getClassName());
    }
    
    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals("javax.swing", classInfo.getClassPackage());
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
    public void testPrintMethodsParam() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS));
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
        Member[] members = null;
        String[] data = ClassInfo.getData(members);
        assertNull(data);
        
        Class[] classes = null;
        data = ClassInfo.getData(classes);
        assertNull(data);
    }
    
    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.srchMethods(null));
    }
    
    @Test
    public void testSrchMethodsIndex() {
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
        long t1 = System.currentTimeMillis();
        ClassInfo.debug = true;
        ClassInfo.main(new String[]{});
        System.out.println("Total time: " + (System.currentTimeMillis() - t1));
    }
    
}