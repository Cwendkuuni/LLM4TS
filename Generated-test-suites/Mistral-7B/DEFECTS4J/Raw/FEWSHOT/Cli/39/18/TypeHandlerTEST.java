package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() throws ParseException {
        assertInstanceOf(String.class, TypeHandler.createValue("test", String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassNotFound() throws ParseException {
        TypeHandler.createValue("java.util.Date", Object.class);
    }

    @Test
    public void testCreateValueStringClassObject() throws ParseException {
        assertInstanceOf(String.class, TypeHandler.createValue("test", Object.class));
    }

    @Test
    public void testCreateValueStringClassStringValue() throws ParseException {
        assertInstanceOf(String.class, TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueStringClassObjectValue() throws ParseException {
        assertInstanceOf(String.class, TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueStringClassNumberValue() throws ParseException {
        assertInstanceOf(Long.class, TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE));
        assertInstanceOf(Double.class, TypeHandler.createValue("123.45", PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassInvalidNumber() throws ParseException {
        TypeHandler.createValue("test", PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValueStringClassDateValue() throws ParseException {
        Date date = new Date();
        assertEquals(date, TypeHandler.createValue("test", PatternOptionBuilder.DATE_VALUE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringClassDateValueUnsupported() throws ParseException {
        TypeHandler.createValue("test", PatternOptionBuilder.DATE_VALUE);
    }

    @Test
    public void testCreateValueStringClassClassValue() throws ParseException, ClassNotFoundException {
        Class<?> cls = Class.forName("java.util.Date");
        assertInstanceOf(Date.class, TypeHandler.createValue("test", cls));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassInvalidClass() throws ParseException, ClassNotFoundException {
        Class<?> cls = Class.forName("java.util.InvalidClass");
        TypeHandler.createValue("test", cls);
    }

    @Test
    public void testCreateValueStringClassFileValue() throws ParseException {
        File file = new File("test.txt");
        assertInstanceOf(File.class, TypeHandler.createValue("test", PatternOptionBuilder.FILE_VALUE));
        assertEquals(file, TypeHandler.createValue("test/test.txt", PatternOptionBuilder.FILE_VALUE));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringClassFileNotFound() throws ParseException {
        TypeHandler.createValue("test/nonExistentFile.txt", PatternOptionBuilder.FILE_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassFileNull() throws ParseException {
        TypeHandler.createValue(null, PatternOptionBuilder.FILE_VALUE);
    }

    @Test
    public void testCreateValueStringClassURLValue() throws ParseException {
        URL url = new URL("http://test.com");
        assertInstanceOf(URL.class, TypeHandler.createValue("test", PatternOptionBuilder.URL_VALUE));
        assertEquals(url, TypeHandler.createValue("http://test.com", PatternOptionBuilder.URL_VALUE));
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValueStringClassInvalidURL() throws ParseException {
        TypeHandler.createValue("test", PatternOptionBuilder.URL_VALUE);
    }

    @Test
    public void testCreateValueStringClassURLNull() throws ParseException {
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateValueStringClassFilesValue() throws ParseException, InterruptedException {
        String[] files = {"test1.txt", "test2.txt"};
        assertInstanceOf(File[].class, TypeHandler.createValue(files, PatternOptionBuilder.FILES_VALUE));
        assertInstanceOf(File[].class, TypeHandler.createValue("test/*.txt", PatternOptionBuilder.FILES_VALUE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringClassFilesUnsupported() throws ParseException {
        TypeHandler.createValue("test/*.txt", PatternOptionBuilder.FILES_VALUE);
    }

}