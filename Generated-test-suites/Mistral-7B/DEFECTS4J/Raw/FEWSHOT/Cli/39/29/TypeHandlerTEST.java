package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateStringValue() {
        String str = "test";
        Object value = TypeHandler.createValue(str, String.class);
        assertEquals(str, value);
    }

    @Test
    public void testCreateObjectValueNull() {
        Object value = TypeHandler.createValue(null, Object.class);
        assertNull(value);
    }

    @Test
    public void testCreateObjectValueValidClassname() throws ParseException {
        String classname = "java.util.Date";
        Object value = TypeHandler.createValue(classname, Object.class);
        assertInstanceOf(Date.class, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectValueInvalidClassname() throws ParseException {
        String classname = "java.util.DateX";
        TypeHandler.createValue(classname, Object.class);
    }

    @Test
    public void testCreateNumberValueLong() throws ParseException {
        String str = "123";
        Number value = (Number) TypeHandler.createValue(str, Long.class);
        assertEquals(Long.valueOf(123), value);
    }

    @Test
    public void testCreateNumberValueDouble() throws ParseException {
        String str = "123.45";
        Number value = (Number) TypeHandler.createValue(str, Double.class);
        assertEquals(Double.valueOf(123.45), value);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberValueInvalid() throws ParseException {
        String str = "123a";
        TypeHandler.createValue(str, Number.class);
    }

    @Test
    public void testCreateClassValueExistingClass() throws ParseException, ClassNotFoundException, InstantiationException {
        String classname = "java.util.Date";
        Object value = TypeHandler.createValue(classname, Class.class);
        assertEquals(Class.forName(classname), value);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassValueNonExistingClass() throws ParseException {
        String classname = "java.util.DateX";
        TypeHandler.createValue(classname, Class.class);
    }

    @Test
    public void testCreateDateValueInvalid() {
        String str = "123";
        assertNull(TypeHandler.createValue(str, Date.class));
    }

    @Test
    public void testCreateDateValueValid() {
        String str = "2022-01-01";
        Date date = (Date) TypeHandler.createValue(str, Date.class);
        assertNotNull(date);
        assertEquals("Sat Jan 01 00:00:00 CET 2022", date.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateValueNotImplemented() {
        String str = "2022-01-01";
        TypeHandler.createValue(str, Date.class);
    }

    @Test
    public void testCreateURLValueInvalid() {
        String str = "https://www.example.com/test/123";
        assertNull(TypeHandler.createValue(str, URL.class));
    }

    @Test
    public void testCreateURLValueValid() {
        String str = "https://www.example.com/test/123";
        URL url = TypeHandler.createValue(str, URL.class);
        assertNotNull(url);
        assertEquals("https://www.example.com/test/123", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLValueInvalidFormat() throws ParseException {
        String str = "https://www.example.com:test/123";
        TypeHandler.createValue(str, URL.class);
    }

    @Test
    public void testCreateFileValueNull() {
        String str = null;
        File file = TypeHandler.createFile(str);
        assertNull(file);
    }

    @Test
    public void testCreateFileValueExistingFile() {
        String str = "/tmp/test.txt";
        File file = TypeHandler.createFile(str);
        assertNotNull(file);
        assertEquals(str, file.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileValueNonExistingFile() throws IOException {
        String str = "/tmp/non_existing_file.txt";
        File file = TypeHandler.createFile(str);
        assertNull(file);
    }

    @Test
    public void testCreateFileInputStreamValueExistingFile() throws IOException {
        String str = "/tmp/test.txt";
        File file = new File(str);
        FileInputStream inputStream = (FileInputStream) TypeHandler.createValue(str, FileInputStream.class);
        assertNotNull(inputStream);
        assertEquals(file, inputStream.getFileDescriptor().getFileDescriptor());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStreamValueNonExistingFile() throws IOException {
        String str = "/tmp/non_existing_file.txt";
        FileInputStream inputStream = (FileInputStream) TypeHandler.createValue(str, FileInputStream.class);
        assertNull(inputStream);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFilesValueNotImplemented() {
        String str = "test/*.txt";
        TypeHandler.createFiles(str);
    }
}