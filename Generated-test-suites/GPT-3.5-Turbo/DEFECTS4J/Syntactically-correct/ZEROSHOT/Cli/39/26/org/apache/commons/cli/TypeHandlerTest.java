import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Before
    public void setup() {
        // setup code
    }

    @Test
    public void testCreateValue_stringValue_shouldReturnValue() throws ParseException {
        Object result = TypeHandler.createValue("testString", PatternOptionBuilder.STRING_VALUE);
        assertEquals("testString", result);
    }

    @Test
    public void testCreateValue_objectValue_shouldCreateObject() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateValue_numberValue_shouldCreateNumber() throws ParseException {
        Object result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateValue_dateValue_shouldCreateDate() throws ParseException {
        Object result = TypeHandler.createValue("2022-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValue_classValue_shouldCreateClass() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_fileValue_shouldCreateFile() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_existingFileValue_shouldCreateFile() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/existingFile.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_filesValue_shouldCreateFiles() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/files/*", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValue_urlValue_shouldCreateURL() throws ParseException {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof URL);
    }
}