package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new Object();
        String str = "foo";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertNotNull(result);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassString() {
        String clazzName = String.class.getName();
        String str = "\"foo\"";
        Object result = TypeHandler.createValue(str, Class.forName(clazzName));
        assertNotNull(result);
        assertEquals("foo", result);
    }

    @Test
    public void testCreateValueStringClassNull() {
        String clazzName = "NonExistentClass";
        String str = "foo";
        Object result = TypeHandler.createValue(str, Class.forName(clazzName));
        assertNull(result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String str = "foo";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertNotNull(result);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        String clazzName = Object.class.getName();
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals(ClassUtils.forName(clazzName), result.getClass());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String str = "123";
        Number result = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValueDouble() {
        String str = "123.45";
        Number result = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String str = "2023-03-13";
        Date result = (Date) TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertNotNull(result);
        assertEquals("Tue Mar 13 00:00:00 CEST 2023", result.toString());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String clazzName = StringBuffer.class.getName();
        String str = clazzName;
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertEquals(ClassUtils.forName(clazzName), result.getClass());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String str = "path/to/file.txt";
        File result = (File) TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertEquals(new File(str), result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() {
        String str = "http://example.com/path/to/file.txt";
        URL result = (URL) TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertEquals(new URL(str), result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNullValue() {
        String str = null;
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        String className = "java.util.Date";
        Class result = TypeHandler.createClass(className);
        assertNotNull(result);
        assertEquals(Date.class, result);
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";
        Number number = TypeHandler.createNumber(numberString);
        assertNotNull(number);
        assertEquals(123, number.intValue());
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        String numberString = "foo";
        Number number = TypeHandler.createNumber(numberString);
        assertNull(number);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2023-03-13";
        Date date = TypeHandler.createDate(dateString);
        assertNotNull(date);
        assertEquals("Tue Mar 13 00:00:00 CEST 2023", date.toString());
    }

    @Test
    public void testCreateDate_InvalidDate() {
        String dateString = "foo";
        Date date = TypeHandler.createDate(dateString);
        assertNull(date);
    }

    @Test
    public void testCreateFile() {
        String filePath = "path/to/file.txt";
        File file = TypeHandler.createFile(filePath);
        assertNotNull(file);
        assertEquals(new File(filePath), file);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com/path/to/file.txt";
        URL url = TypeHandler.createURL(urlString);
        assertNotNull(url);
        assertEquals(new URL(urlString), url);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURL() {
        String urlString = "foo:/path/to/file.txt";
        TypeHandler.createURL(urlString);
    }
}