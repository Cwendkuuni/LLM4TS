package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;


public class TypeHandlerTest {

    @Test
    public void testCreateValue() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
        assertEquals(new Object(), TypeHandler.createValue("java.lang.Object", Object.class));
        assertEquals(123, TypeHandler.createValue("123", Number.class));
        Date date = new Date();
        assertEquals(date, TypeHandler.createValue(date.toString(), Date.class));
        assertEquals(Class.class, TypeHandler.createValue("java.lang.Class", Class.class));
        assertEquals(new File(""), TypeHandler.createValue("", File.class));
        assertEquals(new File(""), TypeHandler.createValue("", File.class));
        assertEquals(new URL("http://www.example.com"), TypeHandler.createValue("http://www.example.com", URL.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueParseException() throws ParseException {
        TypeHandler.createValue("invalid", Number.class);
    }

    @Test
    public void testCreateObject() throws ParseException {
        assertEquals(new Object(), TypeHandler.createObject("java.lang.Object"));
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectClassNotFoundException() throws ParseException {
        TypeHandler.createObject("invalid.Class");
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInstantiationException() throws ParseException {
        TypeHandler.createObject("java.util.Date");
    }

    @Test
    public void testCreateNumber() throws ParseException {
        assertEquals(123, TypeHandler.createNumber("123"));
        assertEquals(123.456, TypeHandler.createNumber("123.456"));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberNumberFormatException() throws ParseException {
        TypeHandler.createNumber("invalid");
    }

    @Test
    public void testCreateClass() throws ParseException {
        assertEquals(Object.class, TypeHandler.createClass("java.lang.Object"));
    }

    @Test(expected = ParseException.class)
    public void testCreateClassNotFoundException() throws ParseException {
        TypeHandler.createClass("invalid.Class");
    }

    @Test
    public void testCreateDate() {
        Date date = new Date();
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateUnsupportedOperationException() {
        TypeHandler.createDate("2019-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        assertEquals(new URL("http://www.example.com"), TypeHandler.createURL("http://www.example.com"));
    }

    @Test(expected = ParseException.class)
    public void testCreateURLMalformedURLException() throws ParseException, MalformedURLException {
        TypeHandler.createURL("invalid");
    }

    @Test
    public void testCreateFile() {
        assertEquals(new File("path/to/file"), TypeHandler.createFile("path/to/file"));
    }

    @Test
    public void testCreateFiles() {
        // Not yet implemented
    }
}