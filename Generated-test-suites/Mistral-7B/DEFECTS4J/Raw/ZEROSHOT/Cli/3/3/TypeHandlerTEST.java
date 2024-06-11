package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * TypeHandlerTest class file.
 */
public class TypeHandlerTest {

    /**
     * Test createValue method with String object.
     */
    @Test
    public void testCreateValueString() {
        String str = "testString";
        String expected = "testString";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(expected, obj);
    }

    /**
     * Test createValue method with Object object.
     */
    @Test
    public void testCreateValueObject() {
        Object obj = new Object() {
        };
        String str = "testObject";
        Class<?> clazz = obj.getClass();
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(clazz, actual.getClass());
    }

    /**
     * Test createValue method with invalid clazz.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidClazz() {
        String str = "test";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test createObject method.
     */
    @Test
    public void testCreateObject() {
        Class<?> clazz = String.class;
        String className = clazz.getName();
        Object actual = TypeHandler.createObject(className);
        assertEquals(clazz, actual.getClass());
    }

    /**
     * Test createObject method with invalid class name.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectInvalidClassName() {
        String className = "org.apache.commons.cli.test.NonexistentClass";
        TypeHandler.createObject(className);
    }

    /**
     * Test createNumber method.
     */
    @Test
    public void testCreateNumber() {
        String str = "123";
        Long expected = 123L;
        Number actual = TypeHandler.createNumber(str);
        assertEquals(expected.longValue(), actual.longValue());

        str = "123.45";
        Double expectedDouble = 123.45;
        Number actualDouble = TypeHandler.createNumber(str);
        assertEquals(expectedDouble.doubleValue(), actualDouble.doubleValue(), 0.0);

        str = "invalid";
        assertNull(TypeHandler.createNumber(str));
    }

    /**
     * Test createClass method.
     */
    @Test
    public void testCreateClass() {
        String className = String.class.getName();
        Class<?> expected = String.class;
        Class<?> actual = TypeHandler.createClass(className);
        assertEquals(expected, actual);
    }

    /**
     * Test createClass method with invalid class name.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateClassInvalidClassName() {
        String className = "org.apache.commons.cli.test.NonexistentClass";
        TypeHandler.createClass(className);
    }

    /**
     * Test createDate method.
     */
    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        Date expected = new Date(0L);
        Date actual = TypeHandler.createDate(str);
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());

        str = "2022-01-01 12:30:45";
        expected = new Date(1641176045000L);
        Date actual2 = TypeHandler.createDate(str);
        assertNotNull(actual2);
        assertEquals(expected.toString(), actual2.toString());

        str = "invalid";
        assertNull(TypeHandler.createDate(str));
    }

    /**
     * Test createURL method.
     */
    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL expected = new URL("http://www.example.com");
        URL actual = TypeHandler.createURL(urlString);
        assertEquals(expected.toString(), actual.toString());

        urlString = "invalid";
        assertNull(TypeHandler.createURL(urlString));
    }

    /**
     * Test createFile method.
     */
    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File expected = new File(filePath);
        File actual = TypeHandler.createFile(filePath);
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    /**
     * Test createFiles method.
     */
    @Test
    public void testCreateFiles() {
        String filePath = "test1.txt,test2.txt,test3.txt";
        File[] expected = new File[] { new File("test1.txt"), new File("test2.txt"), new File("test3.txt") };
        File[] actual = TypeHandler.createFiles(filePath);
        assertNotNull(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getAbsolutePath(), actual[i].getAbsolutePath());
        }

        filePath = "test.txt,invalid";
        assertNull(TypeHandler.createFiles(filePath));
    }
}