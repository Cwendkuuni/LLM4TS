package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        Object result = TypeHandler.createValue("foo", String.class);
        assertEquals("foo", result);
    }

    @Test
    public void testCreateValueStringClassNotFoundException() {
        try {
            Object result = TypeHandler.createValue("java.lang.String", Class.forName("java.lang.NotFoundException"));
        } catch (ClassNotFoundException e) {
            // expected
        }
        assertNull(TypeHandler.createValue("java.lang.String", Class.forName("java.lang.NotFoundException")));
    }

    @Test
    public void testCreateValueStringClass() {
        Object result = TypeHandler.createValue("java.lang.String", String[].class);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringClass_String() {
        Object result = TypeHandler.createValue("java.lang.String", String[].class, "foo");
        assertNotNull(result);
        assertEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testCreateValueStringClass_Object() {
        Object result = TypeHandler.createValue("java.lang.String", File.class, "file.txt");
        assertNotNull(result);
        assertEquals(new File("file.txt"), result);
    }

    @Test
    public void testCreateValueStringClass_URL() {
        Object result = TypeHandler.createValue("http://example.com", URL.class);
        assertNotNull(result);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testCreateValueStringClass_Number() {
        Object result = TypeHandler.createValue("42", int.class);
        assertEquals(42, result);

        Object result2 = TypeHandler.createValue("42.0", double.class);
        assertEquals(42.0, result2, 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateValueStringClass_InvalidNumber() {
        Object result = TypeHandler.createValue("foo", int.class);
    }

    @Test
    public void testCreateValueStringClass_Date() {
        Object result = TypeHandler.createValue("2022-12-31", Date.class);
        assertNotNull(result);
        Date date = (Date) result;
        assertEquals(2022, date.getYear());
        assertEquals(12, date.getMonth());
        assertEquals(31, date.getDay());
    }

    @Test
    public void testCreateValueStringClass_Class() {
        Object result = TypeHandler.createValue("java.lang.StringBuilder", StringBuilder.class);
        assertNotNull(result);
        assertEquals(StringBuilder.class, result.getClass());
    }

    @Test
    public void testCreateValueStringClass_Null() {
        Object result = TypeHandler.createValue(null, String.class);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringClass_NullClazz() {
        Object result = TypeHandler.createValue("java.lang.String", null);
        assertNull(result);
    }

    @Test
    public void testCreateObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        StringBuilder result = (StringBuilder) TypeHandler.createObject("java.lang.StringBuilder");
        assertEquals(stringBuilderClass, result.getClass());
        assertEquals("", result.toString());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectClassNotFoundException() {
        Object result = TypeHandler.createObject("java.lang.NotFoundException");
    }

    @Test
    public void testCreateNumber() {
        Number result = TypeHandler.createNumber("42");
        assertEquals(42, result.intValue());

        Number result2 = TypeHandler.createNumber("42.0");
        assertEquals(42.0, result2.doubleValue(), 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalidNumber() {
        Object result = TypeHandler.createNumber("foo");
    }

    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2022-12-31");
        assertNotNull(result);
        assertEquals(2022, result.getYear());
        assertEquals(11, result.getMonth());
        assertEquals(30, result.getDate());
    }

    @Test
    public void testCreateURL() {
        URL result = TypeHandler.createURL("http://example.com");
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
        assertEquals("//example.com", result.getFile());
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("file.txt");
        assertNotNull(result);
        assertEquals("file.txt", result.getName());
    }

    @Test
    public void testCreateFiles() {
        String[] paths = {"file1.txt", "file2.txt"};
        File[] files = TypeHandler.createFiles(String.join(File.pathSeparator, paths));
        assertNotNull(files);
        assertEquals(2, files.length);
        assertEquals(new File("file1.txt"), files[0]);
        assertEquals(new File("file2.txt"), files[1]);
    }
}