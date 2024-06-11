
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
        String str = "test";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
    }

    @Test
    public void testCreateValue_numberValue() {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateValue_dateValue() {
        String str = "2022-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValue_classValue() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Class);
    }

    @Test
    public void testCreateValue_fileValue() {
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_existingFileValue() {
        String str = "/path/to/existing/file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File);
        assertTrue(((File) result).exists());
    }

    @Test
    public void testCreateValue_filesValue() {
        String str = "/path/to/files/*.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValue_urlValue() {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof URL);
    }

    // TODO: Add test cases for unsupported types

    @Test
    public void testCreateValue_unknownValue() {
        String str = "unknown";
        Object obj = "UnknownType";

        Object result = TypeHandler.createValue(str, obj);

        assertNull(result);
    }

    @Test
    public void testCreateObject_success() {
        String str = "java.lang.String";

        Object result = TypeHandler.createObject(str);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testCreateObject_failure() {
        String str = "unknown.ClassName";

        Object result = TypeHandler.createObject(str);

        assertNull(result);
    }

    @Test
    public void testCreateNumber_success() {
        String str = "123";

        Number result = TypeHandler.createNumber(str);

        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateNumber_failure() {
        String str = "123a";

        Number result = TypeHandler.createNumber(str);

        assertNull(result);
    }

    @Test
    public void testCreateClass_success() {
        String str = "java.lang.String";

        Class result = TypeHandler.createClass(str);

        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateClass_failure() {
        String str = "unknown.ClassName";

        Class result = TypeHandler.createClass(str);

        assertNull(result);
    }

    @Test
    public void testCreateDate_success() {
        String str = "2022-01-01";

        Date result = TypeHandler.createDate(str);

        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateDate_failure() {
        String str = "2022/01/01";

        Date result = TypeHandler.createDate(str);

        assertNull(result);
    }

    @Test
    public void testCreateURL_success() {
        String str = "http://www.example.com";

        URL result = TypeHandler.createURL(str);

        assertNotNull(result);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testCreateURL_failure() {
        String str = "invalid_url";

        URL result = TypeHandler.createURL(str);

        assertNull(result);
    }

    @Test
    public void testCreateFile_success() {
        String str = "/path/to/file.txt";

        File result = TypeHandler.createFile(str);

        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateFiles_success() {
        String str = "/path/to/files/*.txt";

        File[] result = TypeHandler.createFiles(str);

        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }
}
