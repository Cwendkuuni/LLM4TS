import org.junit.Test;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import java.net.URL;
import java.io.File;
import java.util.Date;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        String str = "testString";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof String);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObjectValue() throws ParseException {
        String str = "org.example.TestObject";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof TestObject);
    }

    @Test
    public void testCreateValueWithNumberValue() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof Number);
        assertEquals(10, result);
    }

    @Test
    public void testCreateValueWithDateValue() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValueWithClassValue() throws ParseException {
        String str = "org.example.TestClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof Class);
    }

    @Test
    public void testCreateValueWithFileValue() throws ParseException {
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithExistingFileValue() throws ParseException {
        String str = "/path/to/existing/file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof File);
        assertTrue(((File) result).exists());
    }

    @Test
    public void testCreateValueWithFilesValue() throws ParseException {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof File[]);
        assertEquals(2, ((File[]) result).length);
    }

    @Test
    public void testCreateValueWithURLValue() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertTrue(result instanceof URL);
        assertEquals(str, ((URL) result).toString());
    }

    @Test
    public void testCreateValueWithInvalidValue() {
        String str = "invalidValue";
        Object obj = Object.class;

        try {
            Object result = TypeHandler.createValue(str, obj);
            fail("Expected ParseException to be thrown");
        } catch (ParseException e) {
            assertEquals("Unable to create an instance of: java.lang.Object", e.getMessage());
        }
    }
}