import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObject() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        Number result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(10L, result.longValue());
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        // test not implemented yet
        // will throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        });
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        Class<?> result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueWithFile() {
        File result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        // test non-existing file
        assertThrows(ParseException.class, () -> {
            TypeHandler.openFile("non_existing_file.txt");
        });

        // test unreadable file
        assertThrows(ParseException.class, () -> {
            TypeHandler.openFile("unreadable_file.txt");
        });
    }

    @Test
    public void testCreateFiles() {
        // test not implemented yet
        // will throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createFiles("test_folder");
        });
    }
}