import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ClassInfoTest {
    
    private ClassInfo classInfo;
    
    @Before
    public void setUp() {
        classInfo = new ClassInfo();
    }
    
    @Test
    public void testIsAbstract() {
        assertTrue(classInfo.isAbstract());
    }
    
    @Test
    public void testIsInterface() {
        assertFalse(classInfo.isInterface());
    }
    
    @Test
    public void testGetClassName() {
        assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());
    }
    
    @Test
    public void testGetClassPackage() {
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }
    
    @Test
    public void testGetSuperClassName() {
        assertNull(classInfo.getSuperClassName());
    }
    
    @Test
    public void testPrintFields() {
        assertNotNull(classInfo.printFields());
    }
    
    @Test
    public void testPrintMethods() {
        assertNotNull(classInfo.printMethods());
    }
    
    @Test
    public void testPrintConstructors() {
        assertNotNull(classInfo.printConstructors());
    }
    
    @Test
    public void testPrintInterfaces() {
        assertNotNull(classInfo.printInterfaces());
    }
    
    @Test
    public void testGetDataForMember() {
        assertEquals(6, classInfo.getData(classInfo.getClass().getFields()).length);
    }
    
    @Test
    public void testGetDataForClass() {
        assertEquals(2, classInfo.getData(classInfo.getClass().getInterfaces()).length);
    }
    
    @Test
    public void testSrchMethods() {
        assertNull(classInfo.srchMethods("test"));
    }
    
    @Test
    public void testGetFoundMethod() {
        assertNull(classInfo.getFoundMethod(0));
    }

}