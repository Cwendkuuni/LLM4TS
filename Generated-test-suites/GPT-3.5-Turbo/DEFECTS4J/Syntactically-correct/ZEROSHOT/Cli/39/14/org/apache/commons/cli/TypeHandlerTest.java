import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringClass() throws ParseException {
        Object result = TypeHandler.createValue("test", Class.class);
        assertNull(result);
    }

    @Test
    public void testCreateValue_ObjectValue() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        Object expected = TypeHandler.createObject("test");
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_NumberValue_Long() throws ParseException {
        Object result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123L, result);
    }

    @Test
    public void testCreateValue_NumberValue_Double() throws ParseException {
        Object result = TypeHandler.createValue("3.14", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(3.14, result);
    }

    @Test
    public void testCreateValue_DateValue() throws ParseException {
        Object result = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_ClassValue() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_FileValue() throws ParseException {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals("test.txt", ((File) result).getName());
    }

    @Test
    public void testCreateValue_ExistingFileValue() throws ParseException {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals("test.txt", ((File) result).getName());
    }

    @Test
    public void testCreateValue_FilesValue() throws ParseException {
        Object result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_URLValue() throws ParseException {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertEquals("http://www.example.com", ((URL) result).toString());
    }

    @Test
    public void testCreateValue_Default() throws ParseException {
        Object result = TypeHandler.createValue("test", null);
        assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFoundException() throws ParseException {
        TypeHandler.createObject("nonexistent.Class");
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_Exception() throws ParseException {
        TypeHandler.createObject("java.lang.Exception");
    }

    @Test
    public void testCreateNumber_Long() throws ParseException {
        Number result = TypeHandler.createNumber("123");
        assertEquals(123L, result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        Number result = TypeHandler.createNumber("3.14");
        assertEquals(3.14, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_NumberFormatException() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFoundException() throws ParseException {
        TypeHandler.createClass("nonexistent.Class");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2020-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertEquals("http://www.example.com", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURLException() throws ParseException {
        TypeHandler.createURL("invalidurl");
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("file1.txt,file2.txt");
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}