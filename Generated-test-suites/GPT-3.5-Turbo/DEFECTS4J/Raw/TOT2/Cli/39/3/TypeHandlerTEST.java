import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithStringClass() throws ParseException {
        Object result = TypeHandler.createValue("test", Class.class);
        assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateNumberDouble() throws ParseException {
        Number result = TypeHandler.createNumber("3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testCreateNumberLong() throws ParseException {
        Number result = TypeHandler.createNumber("10");
        assertEquals(10L, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalid() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassNotFound() throws ParseException {
        TypeHandler.createClass("invalid.class");
    }

    @Test
    public void testCreateDate() {
        try {
            TypeHandler.createDate("2021-01-01");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLMalformed() throws ParseException {
        TypeHandler.createURL("invalid-url");
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("/path/to/file.txt");
        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("/path/to/files");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }
}