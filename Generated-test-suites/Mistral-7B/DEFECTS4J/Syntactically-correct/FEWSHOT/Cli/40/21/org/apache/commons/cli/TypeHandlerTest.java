package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TypeHandlerTest {

    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new File("testFile.txt");
    }

    @After
    public void tearDown() throws Exception {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testCreateValueString() {
        String str = "test";
        assertEquals("test", TypeHandler.createValue(str, String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidString() {
        String str = "";
        TypeHandler.createValue(str, String.class);
    }

    @Test
    public void testCreateValueObject() {
        String classname = "java.util.Date";
        Date date = new Date();
        assertEquals(date, TypeHandler.createValue(classname, Date.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidObject() {
        String classname = "java.util.Date";
        TypeHandler.createValue(classname, int.class);
    }

    @Test
    public void testCreateValueNumber() {
        String str = "123";
        Long number = 123L;
        assertEquals(number, TypeHandler.createValue(str, Long.class));

        str = "123.45";
        Double number2 = 123.45;
        assertEquals(number2, TypeHandler.createValue(str, Double.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidNumber() {
        String str = "abc";
        TypeHandler.createValue(str, Integer.class);
    }

    @Test
    public void testCreateValueClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String classname = "java.util.Date";
        Class<?> clazz = Class.forName(classname);
        Date date = new Date();

        assertEquals(date.getClass(), clazz.newInstance().getClass());
        assertEquals(date, TypeHandler.createValue(classname, Date.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() {
        String classname = "java.util.Date";
        TypeHandler.createValue(classname, int.class);
    }

    @Test
    public void testCreateValueDate() throws ParseException {
        String str = "2022-12-31";
        Date date = new Date();
        Date expectedDate = new Date(2022, 11, 31);

        assertEquals(expectedDate, TypeHandler.createValue(str, Date.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateUnsupported() {
        String str = "2022-12-31";
        TypeHandler.createValue(str, int.class);
    }

    @Test
    public void testCreateValueURL() throws MalformedURLException {
        String str = "http://www.example.com";
        URL url = new URL("http://www.example.com");

        assertEquals(url, TypeHandler.createValue(str, URL.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidURL() {
        String str = "http://www.example.com/";
        TypeHandler.createValue(str, int.class);
    }

    @Test
    public void testCreateValueFile() {
        String str = "path/to/file.txt";
        File file = new File(str);

        assertEquals(file, TypeHandler.createValue(str, File.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidFile() {
        String str = "path/to/file.txt";
        TypeHandler.createValue(str, int.class);
    }

    @Test
    public void testCreateValueFileInputStream() throws FileNotFoundException, UnsupportedEncodingException {
        String str = "path/to/testFile.txt";

        FileInputStream expectedInput = new FileInputStream(testFile);
        FileInputStream input = (FileInputStream) TypeHandler.createValue(str, FileInputStream.class);

        assertEquals(expectedInput.available(), input.available());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidFileInputStream() {
        String str = "path/to/testFile.txt";
        TypeHandler.createValue(str, int.class);
    }
}