import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueFromStringAndClass() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueFromClassString() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", Class.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateNumberForLong() throws ParseException {
        Number result = TypeHandler.createNumber("12345");
        assertEquals(12345L, result);
    }

    @Test
    public void testCreateNumberForDouble() throws ParseException {
        Number result = TypeHandler.createNumber("12.34");
        assertEquals(12.34, result);
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

    @Test
    public void testCreateDate() {
        try {
            TypeHandler.createDate("2022-01-01");
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateURL() throws ParseException {
        String url = "https://www.example.com";
        URL result = TypeHandler.createURL(url);
        assertNotNull(result);
        assertEquals(url, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalid() throws ParseException {
        TypeHandler.createURL("invalid-url");
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
        assertEquals(filePath, result.getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("path/to/file");
    }
}