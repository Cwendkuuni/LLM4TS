import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_string() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_object() {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateValue_number() {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateValue_date() {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValue_class() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_file() {
        String str = "path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_existingFile() {
        String str = "existing_file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_files() {
        String str = "path/to/file1.txt,path/to/file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValue_url() {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertNotNull(result);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testCreateValue_invalidType() {
        String str = "test";
        Object obj = "invalid_type";

        Object result = TypeHandler.createValue(str, obj);

        assertNull(result);
    }
}