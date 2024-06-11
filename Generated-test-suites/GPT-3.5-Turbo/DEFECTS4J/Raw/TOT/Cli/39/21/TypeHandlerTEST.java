
import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.text.ParseException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObjectType() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithObjectObjectType() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Object expected = new Object();
        assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testCreateValueWithNumberObjectType() throws ParseException {
        String str = "10.5";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(Double.class, result.getClass());
    }

    @Test
    public void testCreateValueWithDateObjectType() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValueWithClassObjectType() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(Class.class, result.getClass());
    }

    @Test
    public void testCreateValueWithFileObjectType() throws ParseException {
        String str = "test/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithExistingFileObjectType() throws ParseException {
        String str = "test/file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithFilesObjectType() throws ParseException {
        String str = "test/*.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValueWithURLObjectType() throws ParseException {
        String str = "https://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testCreateValueWithInvalidObjectType() throws ParseException {
        String str = "test";
        Object obj = Integer.class;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassName() throws ParseException {
        String className = "invalid.class";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateObjectWithValidClassName() throws ParseException {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        Object expected = new String();
        assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testCreateNumberWithDecimalString() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);
        assertEquals(Double.class, result.getClass());
    }

    @Test
    public void testCreateNumberWithIntegerString() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        assertEquals(Long.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidString() throws ParseException {
        String str = "test";
        TypeHandler.createNumber(str);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassName() throws ParseException {
        String className = "invalid.class";
        TypeHandler.createClass(className);
    }

    @Test
    public void testCreateClassWithValidClassName() throws ParseException {
        String className = "java.lang.String";
        Class<?> result = TypeHandler.createClass(className);
        assertEquals(String.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURLWithValidURLString() throws ParseException {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURLString() throws ParseException {
        String str = "invalid_url";
        TypeHandler.createURL(str);
    }
    
    @Test
    public void testCreateFileWithValidPath() {
        String str = "test/file.txt";
        File result = TypeHandler.createFile(str);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles() {
        String str = "test/*.txt";
        File[] result = TypeHandler.createFiles(str);
        assertEquals(0, result.length);
    }

}
