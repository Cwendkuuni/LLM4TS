import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObject() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithNumber() {
        Number result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateValueWithClass() {
        Class<?> result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateNumberWithLong() {
        Number result = TypeHandler.createNumber("10");
        assertEquals(10L, result);
    }

    @Test
    public void testCreateNumberWithDouble() {
        Number result = TypeHandler.createNumber("10.5");
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateClass() {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNotFound() throws ParseException {
        TypeHandler.openFile("non_existent.txt");
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesNotImplemented() {
        TypeHandler.createFiles("dirPath");
    }
}