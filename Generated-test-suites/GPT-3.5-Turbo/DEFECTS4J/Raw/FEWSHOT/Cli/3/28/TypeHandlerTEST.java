package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        Object obj = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", obj);

        obj = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertNull(obj);

        obj = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(NumberUtils.createNumber("123"), obj);

        obj = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(obj);

        obj = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, obj);

        obj = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals(new File("test.txt"), obj);

        obj = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(new File("test.txt"), obj);

        obj = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNull(obj);

        obj = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        try {
            assertEquals(new URL("http://www.example.com"), obj);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObject() {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, obj);

        obj = TypeHandler.createObject("java.lang.Object");
        assertEquals(Object.class, obj);

        obj = TypeHandler.createObject("org.apache.commons.cli.TypeHandler");
        assertEquals(TypeHandler.class, obj);

        obj = TypeHandler.createObject("nonexistent.Class");
        assertNull(obj);
    }

    @Test
    public void testCreateNumber() {
        Number num = TypeHandler.createNumber("123");
        assertEquals(NumberUtils.createNumber("123"), num);

        num = TypeHandler.createNumber("12.345");
        assertEquals(NumberUtils.createNumber("12.345"), num);

        num = TypeHandler.createNumber("abc");
        assertNull(num);
    }

    @Test
    public void testCreateClass() {
        Class clazz = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, clazz);

        clazz = TypeHandler.createClass("org.apache.commons.cli.TypeHandler");
        assertEquals(TypeHandler.class, clazz);

        clazz = TypeHandler.createClass("nonexistent.Class");
        assertNull(clazz);
    }

    @Test
    public void testCreateDate() {
        Date date = TypeHandler.createDate("2021-01-01");
        assertNull(date);

        date = TypeHandler.createDate("abc");
        assertNull(date);
    }

    @Test
    public void testCreateURL() {
        URL url = TypeHandler.createURL("http://www.example.com");
        try {
            assertEquals(new URL("http://www.example.com"), url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        url = TypeHandler.createURL("www.example.com");
        assertNull(url);

        url = TypeHandler.createURL("abc");
        assertNull(url);
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertEquals(new File("test.txt"), file);
    }

    @Test
    public void testCreateFiles() {
        File[] files = TypeHandler.createFiles("file1.txt,file2.txt");
        assertNull(files);
    }
}