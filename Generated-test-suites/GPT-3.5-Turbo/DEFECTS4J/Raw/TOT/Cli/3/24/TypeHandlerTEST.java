import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_stringValue() {
        String str = "test";
        String obj = "string";

        Object result = TypeHandler.createValue(str, obj);

        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_objectValue() {
        String str = "java.lang.String";
        Class clazz = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_numberValue() {
        String str = "10";
        Class clazz = PatternOptionBuilder.NUMBER_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertTrue(result instanceof Number);
        assertEquals(10, ((Number) result).intValue());
    }

    @Test
    public void testCreateValue_dateValue() {
        String str = "2021-01-01";
        Class clazz = PatternOptionBuilder.DATE_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNull(result);
    }

    @Test
    public void testCreateValue_classValue() {
        String str = "java.lang.String";
        Class clazz = PatternOptionBuilder.CLASS_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertEquals(str, ((Class) result).getName());
    }

    @Test
    public void testCreateValue_fileValue() {
        String str = "/path/to/file.txt";
        Class clazz = PatternOptionBuilder.FILE_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_existingFileValue() {
        String str = "/path/to/existing_file.txt";
        Class clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_filesValue() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        Class clazz = PatternOptionBuilder.FILES_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNull(result);
    }

    @Test
    public void testCreateValue_urlValue() {
        String str = "https://example.com";
        Class clazz = PatternOptionBuilder.URL_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
        assertEquals(str, ((URL) result).toString());
    }

    @Test
    public void testCreateValue_unknownValue() {
        String str = "unknown";
        Class clazz = PatternOptionBuilder.NONE;

        Object result = TypeHandler.createValue(str, clazz);

        assertNull(result);
    }

    @Test
    public void testCreateObject_validClassName() {
        String str = "java.lang.String";

        Object result = TypeHandler.createObject(str);

        assertNotNull(result);
        assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateObject_invalidClassName() {
        String str = "invalid.Class";

        Object result = TypeHandler.createObject(str);

        assertNull(result);
    }

    @Test
    public void testCreateNumber_integer() {
        String str = "10";

        Number result = TypeHandler.createNumber(str);

        assertNotNull(result);
        assertEquals(10, result.intValue());
    }

    @Test
    public void testCreateNumber_decimal() {
        String str = "10.5";

        Number result = TypeHandler.createNumber(str);

        assertNotNull(result);
        assertEquals(10.5, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateNumber_invalidNumber() {
        String str = "invalid";

        Number result = TypeHandler.createNumber(str);

        assertNull(result);
    }

    @Test
    public void testCreateClass_validClassName() {
        String str = "java.lang.String";

        Class result = TypeHandler.createClass(str);

        assertNotNull(result);
        assertEquals(str, result.getName());
    }

    @Test
    public void testCreateClass_invalidClassName() {
        String str = "invalid.Class";

        Class result = TypeHandler.createClass(str);

        assertNull(result);
    }

    @Test
    public void testCreateDate_validDateString() {
        String str = "2021-01-01";

        Date result = TypeHandler.createDate(str);

        assertNull(result);
    }

    @Test
    public void testCreateURL_validURL() {
        String str = "https://example.com";

        URL result = TypeHandler.createURL(str);

        assertNotNull(result);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateURL_invalidURL() {
        String str = "invalid/url";

        URL result = TypeHandler.createURL(str);

        assertNull(result);
    }

    @Test
    public void testCreateFile_validFilePath() {
        String str = "/path/to/file.txt";

        File result = TypeHandler.createFile(str);

        assertNotNull(result);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles_validFilePaths() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";

        File[] result = TypeHandler.createFiles(str);

        assertNull(result);
    }
}