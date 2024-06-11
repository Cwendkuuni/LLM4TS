import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithExistingFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithFiles() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValueWithURL() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(Integer.class, result.getClass());
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(Double.class, result.getClass());
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        String str = "123a";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String className = "java.lang.String";
        Class<?> result = TypeHandler.createClass(className);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClass() throws ParseException {
        String className = "com.example.InvalidClass";
        TypeHandler.createClass(className);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String str = "invalid";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles() {
        String str = "test.txt";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(str, result[0].getPath());
    }

    

    // Additional test cases for potential edge cases and exception handling scenarios
    // ...

    
}