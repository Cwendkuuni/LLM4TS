package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_String_ReturnsString() {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Class_ThrowsParseException_ForInvalidClass() {
        String str = "test";
        Class<?> clazz = int.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_Class_ReturnsStringForStringOption() {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Class_ReturnsObjectForObjectOption() {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        String str = "test";
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(className, result.getClass().getName());
    }

    @Test
    public void testCreateValue_String_Class_ReturnsNumberForNumberOption() {
        String str = "123";
        Class<?> clazz = int.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateValue_String_Class_ReturnsNumberForDoubleOption() {
        String str = "123.45";
        Class<?> clazz = double.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(123.45, result.doubleValue(), 0.0);
    }

    @Test
    public void testCreateValue_String_Class_ReturnsDateForDateOption() {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_String_Class_ReturnsURLForURLOption() {
        String str = "http://example.com";
        Class<?> clazz = URL.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_String_Class_ReturnsFileForFileOption() {
        String str = "test.txt";
        Class<?> clazz = File.class;
        Object result = TypeHandler.createValue(str, clazz);
        File file = (File) result;
        assertEquals(new File(str), file);
    }

    @Test
    public void testCreateValue_String_Class_ReturnsFileInputStreamForFileInputOption() {
        String str = "test.txt";
        Class<?> clazz = FileInputStream.class;
        Object result = TypeHandler.createValue(str, clazz);
        FileInputStream fileInputStream = (FileInputStream) result;
        assertEquals(new FileInputStream(new File(str)), fileInputStream);
    }

    @Test
    public void testCreateValue_String_Class_ThrowsParseException_ForInvalidString() {
        String str = "";
        Class<?> clazz = int.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateObject_String_ReturnsNewInstanceOfGivenClass() {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object result = TypeHandler.createObject(className);
        assertEquals(className, result.getClass().getName());
    }

    @Test
    public void testCreateObject_String_ThrowsParseException_ForInvalidClass() {
        String className = "java.util.Date";
        assertThrows(ParseException.class, () -> TypeHandler.createObject(className));
    }

    @Test
    public void testCreateNumber_String_ReturnsLong() {
        String str = "123";
        Object result = TypeHandler.createNumber(str);
        assertEquals(123, result.longValue());
    }

    @Test
    public void testCreateNumber_String_ReturnsDouble() {
        String str = "123.45";
        Object result = TypeHandler.createNumber(str);
        assertEquals(123.45, result.doubleValue(), 0.0);
    }

    @Test
    public void testCreateDate_String_ThrowsUnsupportedOperationException() {
        String str = "2022-12-31";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    @Test
    public void testCreateFile_String_ReturnsNewFileInstance() {
        String str = "test.txt";
        Object result = TypeHandler.createFile(str);
        File file = (File) result;
        assertEquals(new File(str), file);
    }

    @Test
    public void testOpenFile_String_ReturnsFileInputStream() throws IOException {
        String str = "test.txt";
        Object result = TypeHandler.openFile(str);
        FileInputStream fileInputStream = (FileInputStream) result;
        assertEquals(new FileInputStream(new File(str)), fileInputStream);
    }

    @Test
    public void testCreateURL_String_ReturnsURLInstance() {
        String str = "http://example.com";
        Object result = TypeHandler.createURL(str);
        URL url = (URL) result;
        assertEquals(new URL(str), url);
    }
}