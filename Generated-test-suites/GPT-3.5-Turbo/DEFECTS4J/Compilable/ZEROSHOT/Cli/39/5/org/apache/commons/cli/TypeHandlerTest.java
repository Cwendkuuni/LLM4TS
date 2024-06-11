package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        try {
            Assert.assertEquals("String", TypeHandler.createValue("str", String.class));
            Assert.assertEquals(123, TypeHandler.createValue("123", Integer.class));
            Assert.assertEquals(123L, TypeHandler.createValue("123", Long.class));
            Assert.assertEquals(123.45, TypeHandler.createValue("123.45", Double.class));
            Assert.assertEquals(new Date(), TypeHandler.createValue("2020-01-01", Date.class));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Assert.assertEquals(Object.class, TypeHandler.createObject("java.lang.Object").getClass());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Assert.assertEquals(123, TypeHandler.createNumber("123").intValue());
            Assert.assertEquals(123L, TypeHandler.createNumber("123").longValue());
            Assert.assertEquals(123.45, TypeHandler.createNumber("123.45").doubleValue(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Assert.assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            Assert.assertEquals("http://www.example.com", url.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateFile() {
        try {
            File file = TypeHandler.createFile("path/to/file.txt");
            Assert.assertEquals("path/to/file.txt", file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] files = TypeHandler.createFiles("path/to/files/*");
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // ignore
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}