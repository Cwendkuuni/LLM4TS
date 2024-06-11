package org.apache.commons.cli;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNotNull(result);
        Assert.assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        String str = "test";
        Class<String> clazz = String.class;
        String result = TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Class_String() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateNumber_Integer() throws ParseException {
        String str = "100";
        Number result = TypeHandler.createNumber(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(Integer.class, result.getClass());
        Assert.assertEquals(100, result.intValue());
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        String str = "100.5";
        Number result = TypeHandler.createNumber(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(Double.class, result.getClass());
        Assert.assertEquals(100.5, result.doubleValue(), 0.0);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }

    // Test for unsupported operation
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.toString());
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        FileInputStream result = TypeHandler.openFile(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.getChannel().toString());
    }

    // Test for unsupported operation
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String str = "dir/*.txt";
        TypeHandler.createFiles(str);
    }

}