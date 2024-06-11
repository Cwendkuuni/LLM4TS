import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValue_stringValue() throws ParseException {
        String str = "Hello";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        Object result = typeHandler.createValue(str, obj);

        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_objectValue() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        Object result = typeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateValue_numberValue_integer() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        Object result = typeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
        assertEquals(10L, result);
    }

    @Test
    public void testCreateValue_numberValue_decimal() throws ParseException {
        String str = "3.14";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        Object result = typeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(Double.class, result.getClass());
        assertEquals(3.14, result);
    }

    @Test
    public void testCreateValue_dateValue_notImplemented() {
        String str = "2022-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        try {
            typeHandler.createValue(str, obj);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        } catch (ParseException e) {
            fail("Unexpected ParseException occurred");
        }
    }

    @Test
    public void testCreateValue_classValue_valid() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        Object result = typeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(Class.class, result.getClass());
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_classValue_notFound() {
        String str = "com.example.NonexistentClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        try {
            typeHandler.createValue(str, obj);
            fail("Expected ParseException to be thrown");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: " + str, e.getMessage());
        }
    }

    @Test
    public void testCreateValue_fileValue_valid() throws ParseException {
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        Object result = typeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(File.class, result.getClass());
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_existingFileValue_valid() throws ParseException {
        String str = "/path/to/existingFile.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        try {
            Object result = typeHandler.createValue(str, obj);

            assertNotNull(result);
            assertEquals(FileInputStream.class, result.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException occurred");
        }
    }

    @Test
    public void testCreateValue_existingFileValue_notFound() {
        String str = "/path/to/nonexistentFile.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        try {
            typeHandler.createValue(str, obj);
            fail("Expected ParseException to be thrown");
        } catch (ParseException e) {
            assertEquals("Unable to find file: " + str, e.getMessage());
        }
    }

    @Test
    public void testCreateValue_filesValue_notImplemented() {
        String str = "/path/to/file1.txt, /path/to/file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        try {
            typeHandler.createValue(str, obj);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        } catch (ParseException e) {
            fail("Unexpected ParseException occurred");
        }
    }

    @Test
    public void testCreateValue_urlValue_valid() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        Object result = typeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(URL.class, result.getClass());

        try {
            assertEquals(str, ((URL) result).toURI().toString());
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testCreateValue_urlValue_malformed() {
        String str = "invalid_url";
        Object obj = PatternOptionBuilder.URL_VALUE;

        try {
            typeHandler.createValue(str, obj);
            fail("Expected ParseException to be thrown");
        } catch (ParseException e) {
            assertEquals("Unable to parse the URL: " + str, e.getMessage());
        }
    }
}