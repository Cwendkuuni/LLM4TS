package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.WindowConstants;

import org.junit.Test;

public class ClassInfoTest {
    
    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isAbstract());
        
        classInfo = new ClassInfo("AbstractClassName");
        assertTrue(classInfo.isAbstract());
    }
    
    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isInterface());
        
        classInfo = new ClassInfo("InterfaceName");
        assertTrue(classInfo.isInterface());
    }
    
    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("ClassName");
        assertEquals("ClassName", classInfo.getClassName());
    }
    
    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("ClassName");
        assertNull(classInfo.getClassPackage());
        
        classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang", classInfo.getClassPackage());
    }
    
    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getSuperClassName());
        
        classInfo = new ClassInfo("ClassName");
        assertNull(classInfo.getSuperClassName());
        
        classInfo = new ClassInfo("SubClassName");
        assertEquals("SuperClassName", classInfo.getSuperClassName());
    }
    
    //Add more test cases for other methods
    
}