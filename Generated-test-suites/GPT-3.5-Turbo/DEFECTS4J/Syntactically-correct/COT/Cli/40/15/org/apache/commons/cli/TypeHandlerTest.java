import org.junit.*;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() throws ParseException {
        String str = "test";
        Object obj = "object";
        Object result = TypeHandler.createValue(str, obj);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringClass() throws ParseException {
        String str = "test";
        Class<String> clazz = String.class;
        String result = TypeHandler.createValue(str, clazz);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringClass_StringValue() throws ParseException {
        String str = "test";
        Class<String> clazz = String.class;
        String result = TypeHandler.createValue(str, clazz);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringClass_NumberValue_LowerCase() throws ParseException {
        String str = "100";
        Class<Number> clazz = Number.class;
        Number result = TypeHandler.createValue(str, clazz);
        assertEquals(100, result);
    }

    @Test
    public void testCreateValue_StringClass_NumberValue_UpperCase() throws ParseException {
        String str = "100";
        Class<Number> clazz = Number.class;
        Number result = TypeHandler.createValue(str, clazz);
        assertEquals(100, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClass_NumberValue_Invalid() throws ParseException {
        String str = "test";
        Class<Number> clazz = Number.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_StringClass_DateValue() throws ParseException {
        String str = "2022-01-01";
        Class<Date> clazz = Date.class;
        Date result = TypeHandler.createValue(str, clazz);
        // validation of date object is complex, and will depend on the date format
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_StringClass_ClassValue() throws ParseException {
        String str = "java.lang.String";
        Class<Class> clazz = Class.class;
        Class result = TypeHandler.createValue(str, clazz);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_StringClass_FileValue() throws ParseException {
        String str = "/path/to/file";
        Class<File> clazz = File.class;
        File result = TypeHandler.createValue(str, clazz);
        assertEquals(new File("/path/to/file"), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClass_ExistingFileValue_FileNotFound() throws ParseException {
        String str = "/path/to/nonexistent/file";
        Class<FileInputStream> clazz = FileInputStream.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_StringClass_FilesValue() throws ParseException {
        String str = "/path/to/file1;/path/to/file2;/path/to/file3";
        Class<File[]> clazz = File[].class;
        File[] result = TypeHandler.createValue(str, clazz);
        assertEquals(new File[]{new File("/path/to/file1"), new File("/path/to/file2"), new File("/path/to/file3")}, result);
    }

    @Test
    public void testCreateValue_StringClass_URLValue() throws ParseException {
        String str = "http://www.example.com";
        Class<URL> clazz = URL.class;
        URL result = TypeHandler.createValue(str, clazz);
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClass_URLValue_Invalid() throws ParseException {
        String str = "example";
        Class<URL> clazz = URL.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFound() throws ParseException {
        String classname = "nonexistent.class";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_Integer() throws ParseException {
        String str = "100";
        Number result = TypeHandler.createNumber(str);
        assertEquals(100, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_Invalid() throws ParseException {
        String str = "test";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFound() throws ParseException {
        String classname = "nonexistent.class";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_NotImplemented() {
        TypeHandler.createDate("2022-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_Invalid() throws ParseException {
        String str = "example";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file";
        File result = TypeHandler.createFile(str);
        assertEquals(new File("/path/to/file"), result);
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "/path/to/file";
        FileInputStream result = TypeHandler.openFile(str);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_FileNotFound() throws ParseException {
        String str = "/path/to/nonexistent/file";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/file1;/path/to/file2;/path/to/file3";
        File[] result = TypeHandler.createFiles(str);
        assertEquals(new File[]{new File("/path/to/file1"), new File("/path/to/file2"), new File("/path/to/file3")}, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_NotImplemented() {
        TypeHandler.createFiles("/path/to/file");
    }
}