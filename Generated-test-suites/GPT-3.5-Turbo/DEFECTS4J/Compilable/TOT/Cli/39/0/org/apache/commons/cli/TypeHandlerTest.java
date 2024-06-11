package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    // Test createValue(String str, Object obj)

    @Test
    public void createValue_StringObject_Success() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = typeHandler.createValue(str, obj);

        Assert.assertEquals(str, result);
    }

    @Test (expected = ParseException.class)
    public void createValue_StringObject_ParseException() throws ParseException {
        String str = "test";
        Object obj = new Object();
        typeHandler.createValue(str, obj);
    }

    // Test createValue(String str, Class<?> clazz)

    @Test
    public void createValue_StringClass_Success() throws ParseException {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;
        Object result = typeHandler.createValue(str, clazz);

        Assert.assertEquals(str, result);
    }

    @Test
    public void createValue_ClassString_Success() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = typeHandler.createValue(str, clazz);

        Assert.assertEquals(str, result);
    }

    @Test
    public void createValue_ClassObject_Success() throws ParseException {
        String str = "test";
        Class<?> clazz = Object.class;
        Object result = typeHandler.createValue(str, clazz);

        Assert.assertNotNull(result);
        Assert.assertEquals(Object.class, result.getClass());
    }

    @Test (expected = ParseException.class)
    public void createValue_StringClass_ParseException() throws ParseException {
        String str = "test";
        Class<?> clazz = new Object().getClass();
        typeHandler.createValue(str, clazz);
    }

    // Test createObject(String classname)

    @Test
    public void createObject_Classname_Success() throws ParseException {
        String classname = "java.lang.String";
        Object result = typeHandler.createObject(classname);

        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result.getClass());
    }

    @Test (expected = ParseException.class)
    public void createObject_Classname_ClassNotFoundException() throws ParseException {
        String classname = "com.example.FakeClass";
        typeHandler.createObject(classname);
    }

    @Test (expected = ParseException.class)
    public void createObject_Classname_InstantiationException() throws ParseException {
        String classname = "java.util.Date";
        typeHandler.createObject(classname);
    }

    // Test createNumber(String str)

    @Test
    public void createNumber_Integer_Success() throws ParseException {
        String str = "10";
        Number result = typeHandler.createNumber(str);

        Assert.assertNotNull(result);
        Assert.assertEquals(Integer.class, result.getClass());
        Assert.assertEquals(10, result);
    }

    @Test
    public void createNumber_Double_Success() throws ParseException {
        String str = "10.5";
        Number result = typeHandler.createNumber(str);

        Assert.assertNotNull(result);
        Assert.assertEquals(Double.class, result.getClass());
        Assert.assertEquals(10.5, result);
    }

    @Test (expected = ParseException.class)
    public void createNumber_ParseException() throws ParseException {
        String str = "abc";
        typeHandler.createNumber(str);
    }

    // Test createClass(String classname)

    @Test
    public void createClass_Success() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = typeHandler.createClass(classname);

        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }

    @Test (expected = ParseException.class)
    public void createClass_ClassNotFoundException() throws ParseException {
        String classname = "com.example.FakeClass";
        typeHandler.createClass(classname);
    }

    // Test createDate(String str)

    @Test
    public void createDate_NotImplemented() {
        String str = "2021-01-01";
        Date result = typeHandler.createDate(str);

        Assert.assertNull(result);
    }

    // Test createURL(String str)

    @Test
    public void createURL_Success() throws ParseException, MalformedURLException {
        String str = "https://www.example.com";
        URL result = typeHandler.createURL(str);

        Assert.assertNotNull(result);
        Assert.assertEquals("https", result.getProtocol());
        Assert.assertEquals("www.example.com", result.getHost());
    }

    @Test (expected = ParseException.class)
    public void createURL_ParseException() throws ParseException {
        String str = "fakeurl";
        typeHandler.createURL(str);
    }

    // Test createFile(String str)

    @Test
    public void createFile_Success() {
        String str = "/path/to/file.txt";
        File result = typeHandler.createFile(str);

        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.getPath());
    }

    // Test createFiles(String str)

    @Test
    public void createFiles_NotImplemented() {
        String str = "/path/to/files";
        File[] result = typeHandler.createFiles(str);

        Assert.assertNull(result);
    }

}