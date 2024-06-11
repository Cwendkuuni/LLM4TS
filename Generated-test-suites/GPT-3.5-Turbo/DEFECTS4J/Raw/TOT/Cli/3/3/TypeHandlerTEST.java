import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    //createValue() method tests

    @Test
    public void createValue_StringValue_ReturnsString() {
        String str = "testString";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertTrue(result instanceof String);
        assertEquals(str, (String) result);
    }

    @Test
    public void createValue_ObjectValue_ReturnsObject() {
        String str = "org.example.TestObject";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertTrue(result instanceof TestObject);
    }

    @Test
    public void createValue_NumberValue_ReturnsNumber() {
        String str = "12345";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(result instanceof Number);
        assertEquals(12345, ((Number) result).intValue());
    }

    @Test
    public void createValue_DateValue_ReturnsDate() {
        String str = "2021-01-01";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertTrue(result instanceof Date);
        assertEquals(str, result.toString());
    }

    @Test
    public void createValue_ClassValue_ReturnsClass() {
        String str = "org.example.TestClass";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertTrue(result instanceof Class);
        assertEquals(str, ((Class) result).getName());
    }

    @Test
    public void createValue_FileValue_ReturnsFile() {
        String str = "/path/to/file";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertTrue(result instanceof File);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void createValue_ExistingFileValue_ReturnsFile() {
        String str = "/path/to/file";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertTrue(result instanceof File);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void createValue_FilesValue_ReturnsFileArray() {
        String str = "/path/to/file1,/path/to/file2";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        assertEquals(2, files.length);
        assertEquals("/path/to/file1", files[0].getPath());
        assertEquals("/path/to/file2", files[1].getPath());
    }

    @Test
    public void createValue_URLValue_ReturnsURL() {
        String str = "http://www.example.com";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertTrue(result instanceof URL);
        assertEquals(str, result.toString());
    }

    //createObject() method tests

    @Test
    public void createObject_ValidClassName_ReturnsObject() {
        String className = "org.example.TestObject";
        Object result = TypeHandler.createObject(className);
        assertTrue(result instanceof TestObject);
    }

    @Test
    public void createObject_InvalidClassName_ReturnsNull() {
        String className = "org.example.NonExistentClass";
        Object result = TypeHandler.createObject(className);
        assertNull(result);
    }

    //createNumber() method tests

    @Test
    public void createNumber_ValidNumberString_ReturnsNumber() {
        String numberString = "12345";
        Number result = TypeHandler.createNumber(numberString);
        assertNotNull(result);
        assertEquals(12345, result.intValue());
    }

    @Test
    public void createNumber_InvalidNumberString_ReturnsNull() {
        String numberString = "abcd";
        Number result = TypeHandler.createNumber(numberString);
        assertNull(result);
    }

    //createClass() method tests

    @Test
    public void createClass_ValidClassName_ReturnsClass() {
        String className = "org.example.TestClass";
        Class result = TypeHandler.createClass(className);
        assertNotNull(result);
        assertEquals(className, result.getName());
    }

    @Test
    public void createClass_InvalidClassName_ReturnsNull() {
        String className = "org.example.NonExistentClass";
        Class result = TypeHandler.createClass(className);
        assertNull(result);
    }

    //createDate() method tests

    @Test
    public void createDate_ValidDateString_ReturnsDate() {
        String dateString = "2021-01-01";
        Date result = TypeHandler.createDate(dateString);
        assertNotNull(result);
        assertEquals(dateString, result.toString());
    }

    @Test
    public void createDate_InvalidDateString_ReturnsNull() {
        String dateString = "abcd";
        Date result = TypeHandler.createDate(dateString);
        assertNull(result);
    }

    //createURL() method tests

    @Test
    public void createURL_ValidURLString_ReturnsURL() {
        String urlString = "http://www.example.com";
        URL result = TypeHandler.createURL(urlString);
        assertNotNull(result);
        assertEquals(urlString, result.toString());
    }

    @Test
    public void createURL_InvalidURLString_ReturnsNull() {
        String urlString = "abcd";
        URL result = TypeHandler.createURL(urlString);
        assertNull(result);
    }

    //createFile() method tests

    @Test
    public void createFile_ValidPath_ReturnsFile() {
        String filePath = "/path/to/file";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
        assertEquals(filePath, result.getPath());
    }

    //createFiles() method test
    @Test
    public void createFiles_ValidPaths_ReturnsFileArray(){
        String filePaths = "/path/to/file1,/path/to/file2,/path/to/file3";
        File[] result = TypeHandler.createFiles(filePaths);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("/path/to/file1",result[0].getPath());
        assertEquals("/path/to/file2",result[1].getPath());
        assertEquals("/path/to/file3",result[2].getPath());
    }

    @Test
    public void createFiles_EmptyPaths_ReturnsNull(){
        String filePaths = "";
        File[] result = TypeHandler.createFiles(filePaths);
        assertNull(result);
    }

    //createFiles() method test
    @Test
    public void createFiles_NonExistingPaths_ReturnsNull(){
        String filePaths = "/path/to/non/existent/file1,/path/to/non/existent/file2";
        File[] result = TypeHandler.createFiles(filePaths);
        assertNull(result);
    }

}