import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_ReturnsString() throws ParseException {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof String);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Object_ReturnsObject() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof Object);
        assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_Number_ReturnsNumber() throws ParseException {
        String str = "10";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof Number);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateValue_Date_ReturnsDate() throws ParseException {
        String str = "2021-01-01";
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValue_Class_ReturnsClass() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof Class);
        assertEquals(str, ((Class<?>) result).getName());
    }

    @Test
    public void testCreateValue_File_ReturnsFile() throws ParseException {
        String str = "/path/to/file";
        Class<?> clazz = PatternOptionBuilder.FILE_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof File);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_ExistingFile_ReturnsExistingFile() throws ParseException {
        String str = "/path/to/existing/file";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof FileInputStream);
    }

    @Test
    public void testCreateValue_Files_ReturnsFiles() throws ParseException {
        String str = "/path/to/files";
        Class<?> clazz = PatternOptionBuilder.FILES_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValue_URL_ReturnsURL() throws ParseException {
        String str = "http://www.example.com";
        Class<?> clazz = PatternOptionBuilder.URL_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertTrue(result instanceof URL);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_UnknownClass_ReturnsNull() throws ParseException {
        String str = "unknown.class";
        Class<?> clazz = PatternOptionBuilder.UNKNOWN_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_ClassNotFound_ThrowsParseException() throws ParseException {
        String str = "non.existing.Class";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;

        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_ObjectCreationFailed_ThrowsParseException() throws ParseException {
        String str = "java.lang.String"; // String class cannot be created with an empty constructor
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;

        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumber_ThrowsParseException() throws ParseException {
        String str = "abc";

        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateNumber_Integer_ReturnsLong() throws ParseException {
        String str = "10";

        Number result = TypeHandler.createNumber(str);

        assertTrue(result instanceof Long);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateNumber_Double_ReturnsDouble() throws ParseException {
        String str = "10.5";

        Number result = TypeHandler.createNumber(str);

        assertTrue(result instanceof Double);
        assertEquals(10.5, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL_ThrowsParseException() throws ParseException {
        String str = "invalid.url";

        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateURL_ValidURL_ReturnsURL() throws ParseException {
        String str = "http://www.example.com";

        URL result = TypeHandler.createURL(str);

        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateFile_ReturnsFile() {
        String str = "/path/to/file";

        File result = TypeHandler.createFile(str);

        assertEquals(str, result.getPath());
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_NonExistingFile_ThrowsParseException() throws ParseException {
        String str = "/path/to/nonexisting/file";

        TypeHandler.openFile(str);
    }

    @Test
    public void testOpenFile_ExistingFile_ReturnsFileInputStream() throws ParseException {
        String str = "/path/to/existing/file";

        FileInputStream result = TypeHandler.openFile(str);

        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_NotImplemented_ThrowsUnsupportedOperationException() {
        String str = "2021-01-01";

        TypeHandler.createDate(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_NotImplemented_ThrowsUnsupportedOperationException() {
        String str = "/path/to/files";

        TypeHandler.createFiles(str);
    }
}