import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void createValue_StringValue_ReturnsString() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertEquals(str, result);
    }

    @Test
    public void createValue_ObjectValue_ReturnsObject() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(str, result.getClass().getName());
    }

    @Test
    public void createValue_NumberValue_ReturnsNumber() throws ParseException {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void createValue_DateValue_ReturnsDate() throws ParseException {
        String str = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void createValue_ClassValue_ReturnsClass() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Class);
        assertEquals(str, ((Class<?>) result).getName());
    }

    @Test
    public void createValue_FileValue_ReturnsFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void createValue_ExistingFileValue_ReturnsFileInputStream() throws ParseException {
        String str = "existingfile.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof FileInputStream);
    }

    @Test
    public void createValue_FilesValue_ReturnsFileArray() throws ParseException {
        String str = "file1.txt,file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        assertEquals(2, files.length);
        assertEquals("file1.txt", files[0].getPath());
        assertEquals("file2.txt", files[1].getPath());
    }

    @Test
    public void createValue_URLValue_ReturnsURL() throws ParseException {
        String str = "http://example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof URL);
        assertEquals(str, result.toString());
    }

    @Test
    public void createValue_InvalidValue_ReturnsNull() throws ParseException {
        String str = "INVALID";
        Object obj = new Object();

        Object result = TypeHandler.createValue(str, obj);

        assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void createObject_ClassNotFound_ThrowsParseException() throws ParseException {
        String classname = "nonexistent.Class";

        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void createObject_ExceptionCreatingObject_ThrowsParseException() throws ParseException {
        String classname = "java.util.Date"; // Date class does not have an empty constructor

        TypeHandler.createObject(classname);
    }

    @Test
    public void createNumber_IntegerString_ReturnsLong() throws ParseException {
        String str = "12345";

        Number result = TypeHandler.createNumber(str);

        assertNotNull(result);
        assertTrue(result instanceof Long);
        assertEquals(12345L, result);
    }

    @Test
    public void createNumber_DecimalString_ReturnsDouble() throws ParseException {
        String str = "12.345";

        Number result = TypeHandler.createNumber(str);

        assertNotNull(result);
        assertTrue(result instanceof Double);
        assertEquals(12.345, result);
    }

    @Test(expected = ParseException.class)
    public void createNumber_InvalidNumberString_ThrowsParseException() throws ParseException {
        String str = "InvalidNumber";

        TypeHandler.createNumber(str);
    }

    @Test
    public void createClass_ClassFound_ReturnsClass() throws ParseException {
        String classname = "java.lang.String";

        Class<?> result = TypeHandler.createClass(classname);

        assertNotNull(result);
        assertEquals(classname, result.getName());
    }

    @Test(expected = ParseException.class)
    public void createClass_ClassNotFound_ThrowsParseException() throws ParseException {
        String classname = "nonexistent.Class";

        TypeHandler.createClass(classname);
    }

    @Test
    public void createURL_ValidURL_ReturnsURL() throws ParseException {
        String str = "http://example.com";

        URL result = TypeHandler.createURL(str);

        assertNotNull(result);
        assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void createURL_InvalidURL_ThrowsParseException() throws ParseException {
        String str = "InvalidURL";

        TypeHandler.createURL(str);
    }

    @Test
    public void createFile_ValidFilePath_ReturnsFile() throws ParseException {
        String str = "file.txt";

        File result = TypeHandler.createFile(str);

        assertNotNull(result);
        assertEquals(str, result.getPath());
    }

    @Test
    public void openFile_ExistingFile_ReturnsFileInputStream() throws ParseException {
        String str = "existingfile.txt";

        FileInputStream result = TypeHandler.openFile(str);

        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void openFile_NonExistingFile_ThrowsParseException() throws ParseException {
        String str = "nonexistingfile.txt";

        TypeHandler.openFile(str);
    }

    @Test
    public void createFiles_ValidFilePaths_ReturnsFileArray() throws ParseException {
        String str = "file1.txt,file2.txt";

        File[] result = TypeHandler.createFiles(str);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("file1.txt", result[0].getPath());
        assertEquals("file2.txt", result[1].getPath());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createFiles_NotImplemented_ThrowsUnsupportedOperationException() throws ParseException {
        String str = "file1.txt,file2.txt";

        TypeHandler.createFiles(str);
    }
}