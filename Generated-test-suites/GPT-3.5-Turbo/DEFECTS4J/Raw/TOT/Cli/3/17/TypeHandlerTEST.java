import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_stringValue() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_objectValue() {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_numberValue_long() {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertTrue(result instanceof Long);
        assertEquals(123L, result);
    }

    @Test
    public void testCreateValue_numberValue_double() {
        String str = "3.14";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertTrue(result instanceof Double);
        assertEquals(3.14, result);
    }

    @Test
    public void testCreateValue_dateValue_validDate() {
        String str = "2022-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_dateValue_invalidDate() {
        String str = "2022-13-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateValue_classValue_validClass() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_classValue_invalidClass() {
        String str = "java.lang.InvalidClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateValue_fileValue() {
        String str = "/path/to/file";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_existingFileValue() {
        String str = "/path/to/existing/file";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_filesValue() {
        String str = "/path/to/file1,/path/to/file2";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_urlValue_validURL() {
        String str = "https://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_urlValue_invalidURL() {
        String str = "invalid-url";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateValue_unknownValue() {
        String str = "unknown";
        Object obj = "unknown";
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateObject_validClass() {
        String str = "java.lang.String";
        Object result = TypeHandler.createObject(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateObject_invalidClass() {
        String str = "java.lang.InvalidClass";
        Object result = TypeHandler.createObject(str);
        assertNull(result);
    }

    @Test
    public void testCreateNumber_validNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertTrue(result instanceof Long);
        assertEquals(123L, result);
    }

    @Test
    public void testCreateNumber_invalidNumber() {
        String str = "123a";
        Number result = TypeHandler.createNumber(str);
        assertNull(result);
    }

    @Test
    public void testCreateClass_validClass() {
        String str = "java.lang.String";
        Class result = TypeHandler.createClass(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateClass_invalidClass() {
        String str = "java.lang.InvalidClass";
        Class result = TypeHandler.createClass(str);
        assertNull(result);
    }

    @Test
    public void testCreateDate_validDate() {
        String str = "2022-01-01";
        Date result = TypeHandler.createDate(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateDate_invalidDate() {
        String str = "2022-13-01";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }

    @Test
    public void testCreateURL_validURL() {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateURL_invalidURL() {
        String str = "invalid-url";
        URL result = TypeHandler.createURL(str);
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/file1,/path/to/file2";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
    }
}