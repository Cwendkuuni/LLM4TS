import static org.junit.Assert.*;
import org.junit.*;

public class ClassInfoTest {
    
    // Test cases for the isAbstract() method
    
    @Test
    public void testIsAbstract_ReturnsTrue_IfStatusAbstractIsTrue() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.statusAbstract = true;
        
        boolean result = classInfo.isAbstract();
        
        assertTrue(result);
    }
    
    @Test
    public void testIsAbstract_ReturnsFalse_IfStatusAbstractIsFalse() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.statusAbstract = false;
        
        boolean result = classInfo.isAbstract();
        
        assertFalse(result);
    }
    
    // Test cases for the isInterface() method
    
    @Test
    public void testIsInterface_ReturnsTrue_IfClassIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.c = SomeInterface.class;
        
        boolean result = classInfo.isInterface();
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInterface_ReturnsFalse_IfClassIsNotInterface() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.c = SomeClass.class;
        
        boolean result = classInfo.isInterface();
        
        assertFalse(result);
    }
    
    // Test cases for the getClassName() method
    
    @Test
    public void testGetClassName_ReturnsClassName() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.c = SomeClass.class;
        
        String result = classInfo.getClassName();
        
        assertEquals("com.example.SomeClass", result);
    }
    
    // Test cases for the getClassPackage() method
    
    @Test
    public void testGetClassPackage_ReturnsClassPackage_IfPackageExists() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.c = SomeClass.class;
        
        String result = classInfo.getClassPackage();
        
        assertEquals("com.example", result);
    }
    
    @Test
    public void testGetClassPackage_ReturnsNull_IfPackageDoesNotExist() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.c = Object.class;
        
        String result = classInfo.getClassPackage();
        
        assertNull(result);
    }
    
    // Test cases for the getSuperClassName() method
    
    @Test
    public void testGetSuperClassName_ReturnsSuperClassName_IfClassHasSuperClass() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.c = SomeClass.class;
        
        String result = classInfo.getSuperClassName();
        
        assertEquals("com.example.SuperClass", result);
    }
    
    @Test
    public void testGetSuperClassName_ReturnsNull_IfClassDoesNotHaveSuperClass() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.c = Object.class;
        
        String result = classInfo.getSuperClassName();
        
        assertNull(result);
    }
    
    // Additional test cases for other methods...
    
}