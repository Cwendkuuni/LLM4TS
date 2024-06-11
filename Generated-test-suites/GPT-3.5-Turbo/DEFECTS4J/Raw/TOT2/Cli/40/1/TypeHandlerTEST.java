import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithClass() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("123");
        assertEquals(123, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidString() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidString() throws ParseException {
        TypeHandler.createClass("InvalidClass");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-09-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        TypeHandler.createURL("invalidURL");
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("testFile.txt");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileWithNonExistentFile() throws ParseException {
        TypeHandler.openFile("nonExistentFile.txt");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("directoryPath");
    }
}