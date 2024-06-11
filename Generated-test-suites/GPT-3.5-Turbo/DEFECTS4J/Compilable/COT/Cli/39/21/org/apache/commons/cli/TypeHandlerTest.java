package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException {
        Object result = TypeHandler.createValue("test", Class.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.Object");
        Assert.assertNotNull(result);
        Assert.assertEquals(Object.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClass() throws ParseException  {
        TypeHandler.createObject("InvalidClass");
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("10");
        Assert.assertEquals(10L, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        TypeHandler.createNumber("not a number");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClass() throws ParseException {
        TypeHandler.createClass("InvalidClass");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2019-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        Assert.assertNotNull(result);
        Assert.assertEquals("http://www.example.com", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        TypeHandler.createURL("invalidURL");
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        Assert.assertNotNull(result);
        Assert.assertEquals("test.txt", result.getName());
    }

    @Test
    public void testCreateFiles() {
        // Not yet implemented
    }

}
