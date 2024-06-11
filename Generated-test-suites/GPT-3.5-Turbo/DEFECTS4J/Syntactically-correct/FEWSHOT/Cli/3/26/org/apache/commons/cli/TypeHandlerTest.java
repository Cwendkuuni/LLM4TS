import org.junit.Test;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_stringValue() {
        Object result = TypeHandler.createValue("hello", PatternOptionBuilder.STRING_VALUE);
        assertEquals("hello", result);
    }

    @Test
    public void testCreateValue_objectValue() {
        Object result = TypeHandler.createValue("org.apache.commons.cli.Util", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Util);
    }

    @Test
    public void testCreateValue_numberValue() {
        Object result = TypeHandler.createValue("100", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Number);
        assertEquals(100, ((Number) result).intValue());
    }

    @Test
    public void testCreateValue_dateValue() {
        Object result = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
        // Add test cases for valid date strings and verify the result
    }

    @Test
    public void testCreateValue_classValue() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Class);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_fileValue() {
        Object result = TypeHandler.createValue("/path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
        assertEquals("/path/to/file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValue_existingFileValue() {
        Object result = TypeHandler.createValue("/path/to/file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
        assertEquals("/path/to/file.txt", ((File) result).getPath());
        // Add test cases for non-existent files and verify the result
    }

    @Test
    public void testCreateValue_filesValue() {
        Object result = TypeHandler.createValue("/path/to/file1.txt,/path/to/file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        assertEquals(2, files.length);
        assertEquals("/path/to/file1.txt", files[0].getPath());
        assertEquals("/path/to/file2.txt", files[1].getPath());
    }

    @Test
    public void testCreateValue_urlValue() {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof URL);
        assertEquals("http://www.example.com", result.toString());
    }

    // Add more test cases to comprehensively test all methods in TypeHandler class

}