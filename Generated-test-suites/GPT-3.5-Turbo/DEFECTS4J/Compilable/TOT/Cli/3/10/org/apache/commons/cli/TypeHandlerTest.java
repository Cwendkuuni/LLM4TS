package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;
import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_stringValue_returnString() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertTrue(result instanceof String);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_objectValue_returnObject() {
        Object result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
        assertTrue(result instanceof Object);
        assertEquals("java.lang.Object", result.getClass().getName());
    }

    @Test
    public void testCreateValue_numberValue_returnNumber() {
        Object result = TypeHandler.createValue("5", PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(result instanceof Number);
        assertEquals(5, result);
    }

    @Test
    public void testCreateValue_dateValue_returnDate() {
        Object result = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        assertTrue(result instanceof Date);
        assertEquals("2020-01-01", result.toString());
    }

    @Test
    public void testCreateValue_classValue_returnClass() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertTrue(result instanceof Class);
        assertEquals("java.lang.String", ((Class) result).getName());
    }

    @Test
    public void testCreateValue_fileValue_returnFile() {
        Object result = TypeHandler.createValue("/path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertTrue(result instanceof File);
        assertEquals("/path/to/file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValue_existingFileValue_returnFile() {
        Object result = TypeHandler.createValue("/path/to/existing_file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertTrue(result instanceof File);
        assertEquals("/path/to/existing_file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValue_filesValue_returnFileArray() {
        Object result = TypeHandler.createValue("/path/to/file1.txt,/path/to/file2.txt,/path/to/file3.txt", PatternOptionBuilder.FILES_VALUE);
        assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        assertEquals(3, files.length);
        assertEquals("/path/to/file1.txt", files[0].getPath());
        assertEquals("/path/to/file2.txt", files[1].getPath());
        assertEquals("/path/to/file3.txt", files[2].getPath());
    }

    @Test
    public void testCreateValue_urlValue_returnURL() {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertTrue(result instanceof URL);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testCreateValue_invalidClassValue_returnNull() {
        Object result = TypeHandler.createValue("non.existing.Class", PatternOptionBuilder.CLASS_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_invalidNumberValue_returnNull() {
        Object result = TypeHandler.createValue("not_a_number", PatternOptionBuilder.NUMBER_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_invalidDateValue_returnNull() {
        Object result = TypeHandler.createValue("not_a_date", PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_invalidURLValue_returnNull() {
        Object result = TypeHandler.createValue("not_a_url", PatternOptionBuilder.URL_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_invalidFileValue_returnNull() {
        Object result = TypeHandler.createValue("not_existing_file", PatternOptionBuilder.FILE_VALUE);
        assertTrue(result instanceof File);
        assertFalse(((File) result).exists());
    }

    @Test
    public void testCreateValue_invalidExistingFileValue_returnNull() {
        Object result = TypeHandler.createValue("not_existing_file", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_invalidFilesValue_returnNull() {
        Object result = TypeHandler.createValue("not_existing_file1,not_existing_file2", PatternOptionBuilder.FILES_VALUE);
        assertNull(result);
    }
}