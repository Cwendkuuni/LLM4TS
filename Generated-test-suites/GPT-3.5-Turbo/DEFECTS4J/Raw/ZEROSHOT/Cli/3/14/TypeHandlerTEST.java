import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Before
    public void setUp() {
        // Set up code here
    }

    @Test
    public void testCreateValueWithString() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithObject() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateValueWithNumber() {
        Object result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateValueWithDate() {
        Object result = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValueWithClass() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateValueWithFile() {
        Object result = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithExistingFile() {
        Object result = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithFiles() {
        Object result = TypeHandler.createValue("/path/to/files", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValueWithURL() {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        Object result = TypeHandler.createValue("invalid_class", "InvalidClass");
        assertNull(result);
    }
}