import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValue_StringObject() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValue_StringClass() throws ParseException {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValue_ObjectValue() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValue_NumberValue() throws ParseException {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(123L, result);
    }
    
    @Test
    public void testCreateValue_DateValue() throws ParseException {
        String str = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }
    
    @Test
    public void testCreateValue_ClassValue() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateValue_FileValue() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(new File(str), result);
    }
    
    @Test
    public void testCreateValue_ExistingFileValue() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(new File(str), result);
    }
    
    @Test
    public void testCreateValue_FilesValue() throws ParseException {
        String str = "test1.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }
    
    @Test
    public void testCreateValue_UrlValue() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(new URL(str), result);
    }
    
    @Test
    public void testCreateValue_NullValue() throws ParseException {
        String str = "test";
        Object obj = null;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFoundException() throws ParseException {
        String classname = "non.existent.Class";
        TypeHandler.createObject(classname);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObject_InstantiationException() throws ParseException {
        String classname = "java.util.Calendar";
        TypeHandler.createObject(classname);
    }
    
    @Test
    public void testCreateObject_Success() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertEquals("", result);
    }
    
    @Test
    public void testCreateNumber_Integer() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result);
    }
    
    @Test
    public void testCreateNumber_Decimal() throws ParseException {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123.45, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumber_NumberFormatException() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFoundException() throws ParseException {
        String classname = "non.existent.Class";
        TypeHandler.createClass(classname);
    }
    
    @Test
    public void testCreateClass_Success() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateDate() {
        String str = "2020-01-01";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }
    
    @Test
    public void testCreateURL_Success() throws ParseException, MalformedURLException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(new URL(str), result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURLException() throws ParseException, MalformedURLException {
        String str = "example.com";
        TypeHandler.createURL(str);
    }
    
    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertEquals(new File(str), result);
    }
    
    @Test
    public void testCreateFiles() {
        String str = "test1.txt,test2.txt";
        File[] result = TypeHandler.createFiles(str);
        assertNull(result);
    }
}