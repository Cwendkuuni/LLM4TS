import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() throws ParseException {
        Object obj = new Object();
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringUnknownType() throws ParseException {
        String str = "test";
        TypeHandler.createValue(str, String.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_STRING_VALUE() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_OBJECT_VALUE() throws ParseException {
        Object obj = new Object();
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertNotSame(obj, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringPatternOptionBuilder_OBJECT_VALUE_InvalidClass() throws ParseException {
        Object result = TypeHandler.createValue("test", Integer.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_NUMBER_VALUE() throws ParseException {
        Number result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123, result.longValue());

        result = TypeHandler.createValue("123.45", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123.45, result.doubleValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringPatternOptionBuilder_NUMBER_VALUE_InvalidNumber() throws ParseException {
        TypeHandler.createValue("abc", PatternOptionBuilder.NUMBER_VALUE);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_DATE_VALUE() throws ParseException {
        String dateString = "2023-03-31";
        Date result = TypeHandler.createValue(dateString, PatternOptionBuilder.DATE_VALUE);
        assertEquals("Fri Mar 31 00:00:00 EDT 2023", result.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_StringPatternOptionBuilder_DATE_VALUE_Unsupported() throws ParseException {
        String dateString = "2023-03-31";
        TypeHandler.createValue(dateString, Date[].class);
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_CLASS_VALUE() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object result = TypeHandler.createValue("test", clazz);
        assertEquals("test", result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringPatternOptionBuilder_CLASS_VALUE_InvalidClass() throws ParseException {
        String className = "java.lang.Integer";
        Object result = TypeHandler.createValue("test", String.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_FILE_VALUE() throws ParseException, IOException {
        String filePath = "/test/file.txt";
        File result = TypeHandler.createValue(filePath, File.class);
        File expected = new File(filePath);
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());

        FileInputStream inputStream = new FileInputStream(expected);
        byte[] bytes = new byte[5];
        inputStream.read(bytes);
        inputStream.close();
        assertEquals(bytes, TypeHandler.createValue(filePath, byte[].class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringPatternOptionBuilder_FILE_VALUE_InvalidPath() throws ParseException {
        String filePath = "/test/file.txt\0";
        TypeHandler.createValue(filePath, File.class);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_EXISTING_FILE_VALUE() throws ParseException, IOException {
        String filePath = "/test/file.txt";
        File result = (File) TypeHandler.createValue(filePath, File.class);
        File expected = new File(filePath);
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_StringPatternOptionBuilder_EXISTING_FILE_VALUE_NonExistentFile() throws ParseException, IOException {
        String filePath = "/test/nonExistentFile.txt";
        TypeHandler.createValue(filePath, File.class);
        fail("Expected FileNotFoundException");
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_URL_VALUE() throws ParseException {
        String urlString = "http://example.com/";
        URL result = TypeHandler.createValue(urlString, URL.class);
        assertEquals(urlString, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringPatternOptionBuilder_URL_VALUE_InvalidURL() throws ParseException {
        String urlString = "http://example.com/\0";
        TypeHandler.createValue(urlString, URL.class);
        fail("Expected ParseException");
    }
}