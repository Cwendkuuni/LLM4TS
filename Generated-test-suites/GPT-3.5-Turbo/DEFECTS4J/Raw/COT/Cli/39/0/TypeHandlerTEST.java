import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Before
    public void setUp() {
        // Setup code here
    }

    @Test
    public void testCreateValue_stringValue_returnsString() throws ParseException {
        String str = "test";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_objectValue_returnsObject() throws ParseException {
        String str = "org.example.ObjectClass";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(ObjectClass.class, result.getClass());
    }

    @Test
    public void testCreateValue_numberValue_returnsNumber() throws ParseException {
        String str = "123";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(result instanceof Number);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_numberValue_invalidNumberFormat() throws ParseException {
        String str = "abc";
        TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValue_dateValue_returnsDate() throws ParseException {
        String str = "2021-01-01";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValue_classValue_returnsClass() throws ParseException {
        String str = "org.example.MyClass";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(MyClass.class, result.getClass());
    }

    @Test
    public void testCreateValue_fileValue_returnsFile() throws ParseException {
        String str = "/path/to/file";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertTrue(result instanceof File);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_existingFileValue_returnsFile() throws ParseException {
        String str = "/path/to/file";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertTrue(result instanceof File);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_filesValue_returnsFileArray() throws ParseException {
        String str = "/path/to/file1,/path/to/file2";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        assertEquals(2, files.length);
        assertEquals("/path/to/file1", files[0].getPath());
        assertEquals("/path/to/file2", files[1].getPath());
    }

    @Test
    public void testCreateValue_urlValue_returnsURL() throws ParseException {
        String str = "http://www.example.com";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertTrue(result instanceof URL);
        assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_urlValue_invalidURLFormat() throws ParseException {
        String str = "invalid-url";
        TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_unknownValue_returnsNull() throws ParseException {
        String str = "unknown";
        TypeHandler.createValue(str, UnknownClass.class);
    }

    @Test
    public void testCreateObject_validClassName_returnsObject() throws ParseException {
        String classname = "org.example.ObjectClass";
        Object result = TypeHandler.createObject(classname);
        assertEquals(ObjectClass.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_invalidClassName_throwsParseException() throws ParseException {
        String classname = "org.example.InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_integerValue_returnsLong() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertTrue(result instanceof Long);
        assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateNumber_decimalValue_returnsDouble() throws ParseException {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertTrue(result instanceof Double);
        assertEquals(123.45, result.doubleValue(), 0.01);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_invalidNumberFormat_throwsParseException() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_validClassName_returnsClass() throws ParseException {
        String classname = "org.example.ObjectClass";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(ObjectClass.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_invalidClassName_throwsParseException() throws ParseException {
        String classname = "org.example.InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_notImplemented_throwsUnsupportedOperationException() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL_validURL_returnsURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_invalidURLFormat_throwsParseException() throws ParseException {
        String str = "invalid-url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_validFilePath_returnsFile() {
        String str = "/path/to/file";
        File result = TypeHandler.createFile(str);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles_validFilePaths_returnsFileArray() {
        String str = "/path/to/file1,/path/to/file2";
        File[] result = TypeHandler.createFiles(str);
        assertEquals(2, result.length);
        assertEquals("/path/to/file1", result[0].getPath());
        assertEquals("/path/to/file2", result[1].getPath());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_notImplemented_throwsUnsupportedOperationException() {
        String str = "/path/to/file1,/path/to/file2";
        TypeHandler.createFiles(str);
    }
}