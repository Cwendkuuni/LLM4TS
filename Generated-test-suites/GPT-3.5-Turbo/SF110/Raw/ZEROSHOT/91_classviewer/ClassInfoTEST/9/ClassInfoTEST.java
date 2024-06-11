package com.jstevh.viewer;

import org.junit.Before;
import org.junit.Test;
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
        assertFalse(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        assertEquals("javax.swing.WindowConstants", classInfo.getClassName());
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
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printMethods());
    }

    @Test
    public void testPrintMethods1() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printMethods(1));
    }

    @Test
    public void testPrintConstructors() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        String[] expected = null;
        assertArrayEquals(expected, classInfo.printInterfaces());
    }

    @Test
    public void testGetDataMember() {
        Member[] m = null;
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(m));
    }

    @Test
    public void testGetDataClass() {
        Class[] c = null;
        String[] expected = null;
        assertArrayEquals(expected, ClassInfo.getData(c));
    }

    @Test
    public void testSrchMethods() {
        String tempStr = null;
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(tempStr));
    }

    @Test
    public void testSrchMethods1() {
        String tempStr = null;
        String[] index = null;
        String[] expected = null;
        assertArrayEquals(expected, classInfo.srchMethods(tempStr, index));
    }
}