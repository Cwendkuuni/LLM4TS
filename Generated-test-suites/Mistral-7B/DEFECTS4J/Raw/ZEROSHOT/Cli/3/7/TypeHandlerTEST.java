package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TypeHandlerTest class.
 */
public class TypeHandlerTest {

    /**
     * Initialize class for testing.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        // Classes to be used in test methods
    }

    /**
     * Test createValue(String, Object) method for String type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObject() throws Exception {
        String str = "test string";
        Object obj = "java.lang.String";
        Object result = TypeHandler.createValue(str, obj);

        assertSame(str, result);
    }

    /**
     * Test createValue(String, Object) method for Object type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectObject() throws Exception {
        String str = "com.test.MyClass";
        Object obj = Class.forName("java.lang.Object");

        Object result = TypeHandler.createValue(str, obj);

        assertSame(Class.forName(str), result);
    }

    /**
     * Test createValue(String, Object) method for Number type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectNumber() throws Exception {
        String str = "123";
        Object obj = int.class;

        Number result = (Number) TypeHandler.createValue(str, obj);

        assertEquals(123, result.intValue());
    }

    /**
     * Test createValue(String, Object) method for Number type with decimal.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectDouble() throws Exception {
        String str = "123.45";
        Object obj = double.class;

        Number result = (Number) TypeHandler.createValue(str, obj);

        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    /**
     * Test createValue(String, Object) method for Date type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectDate() throws Exception {
        String str = "2023-03-15";
        Object obj = Date.class;

        Date result = (Date) TypeHandler.createValue(str, obj);

        Date expected = new Date(2023 - 1900, 2 - 1, 15);
        assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Object) method for Class type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectClass() throws Exception {
        String str = "java.lang.String";
        Object obj = String.class;

        Class<?> result = (Class<?>) TypeHandler.createValue(str, obj);

        assertSame(String.class, result);
    }

    /**
     * Test createValue(String, Object) method for File type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectFile() throws Exception {
        String str = "path/to/file.txt";
        Object obj = File.class;

        File result = (File) TypeHandler.createValue(str, obj);

        File expected = new File(str);
        assertSame(expected, result);
    }

    /**
     * Test createValue(String, Object) method for existing File type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectExistingFile() throws Exception {
        String str = "path/to/file.txt";
        Object obj = File.class;

        File result = (File) TypeHandler.createValue(str, obj);

        File expected = new File(str);
        assertSame(expected, result);
    }

    /**
     * Test createValue(String, Object) method for Files type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectFiles() throws Exception {
        String str = "path/to/folder/**/*.txt";

        Object obj = File[].class;

        File[] result = (File[]) TypeHandler.createValue(str, obj);

        FileFilter filter = file -> file.isFile() && file.getName().endsWith(".txt");
        File folder = new File("path/to/folder");

        File[] expected = folder.listFiles(filter);

        assertArrayEquals(expected, result);
    }

    /**
     * Test createValue(String, Object) method for URL type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectURL() throws Exception {
        String str = "http://www.example.com";
        Object obj = URL.class;

        URL result = (URL) TypeHandler.createValue(str, obj);

        URL expected = new URL(str);
        assertSame(expected, result);
    }

    /**
     * Test createValue(String, Object) method for invalid type.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateValueStringObjectInvalidType() throws Exception {
        String str = "test string";
        Object obj = int[].class;

        Object result = TypeHandler.createValue(str, obj);

        assertNull(result);
    }
}