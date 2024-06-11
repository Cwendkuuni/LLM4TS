package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValue_String_String() throws Exception
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_InvalidClass() throws Exception
    {
        String str = "test";
        TypeHandler.createValue(str, Integer.class);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testCreateValue_String_Object() throws Exception
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, Object.class);
        assertNotNull(obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidString_Object() throws Exception
    {
        String str = "test";
        TypeHandler.createValue(str, String[].class);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testCreateValue_String_ClassString() throws Exception
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidString_Class() throws Exception
    {
        String str = "test";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testCreateObject() throws Exception
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createObject(classname);
        assertEquals(classname, obj.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassName() throws Exception
    {
        String classname = "java.lang.Integer";
        TypeHandler.createObject(classname);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testCreateNumber() throws Exception
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123L, number.longValue());

        str = "123.45";
        number = TypeHandler.createNumber(str);
        assertEquals(123.45, number.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString() throws Exception
    {
        String str = "abc";
        TypeHandler.createNumber(str);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testCreateClass() throws Exception
    {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(classname, clazz.getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClassName() throws Exception
    {
        String classname = "java.lang.Integer";
        TypeHandler.createClass(classname);
        fail("ParseException expected but not thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws Exception
    {
        String str = "2022-12-31";
        TypeHandler.createDate(str);
        fail("UnsupportedOperationException expected but not thrown");
    }

    @Test
    public void testCreateURL() throws Exception
    {
        String str = "http://test.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("http", url.getProtocol());
        assertEquals("test.com", url.getHost());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidString() throws Exception
    {
        String str = "test.com";
        TypeHandler.createURL(str);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testCreateFile() throws Exception
    {
        String str = "test.txt";
        File file = TypeHandler.createFile(str);
        assertTrue(file.exists());
        file.delete();
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidString() throws Exception
    {
        String str = "test/test.txt";
        TypeHandler.createFile(str);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testCreateFileInputStream() throws Exception
    {
        String filePath = "test.txt";
        String str = "test";
        File file = new File(filePath);
        if (!file.exists())
        {
            file.createNewFile();
        }
        FileInputStream fileInputStream = (FileInputStream) TypeHandler.createValue(filePath, FileInputStream.class);
        assertEquals(file, fileInputStream.getFileDescriptor().getFileDescriptor());
        file.delete();
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStream_InvalidFilePath() throws Exception
    {
        String filePath = "test/test.txt";
        FileInputStream fileInputStream = (FileInputStream) TypeHandler.createValue(filePath, FileInputStream.class);
        fail("FileNotFoundException expected but not thrown");
    }
}