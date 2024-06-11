import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObject() {
        Object result = TypeHandler.createValue("test", Object.class);
        assertNotNull(result);
        assertEquals("test", (String) result);
    }

    @Test
    public void testCreateValueWithClass() {
        Object result = TypeHandler.createValue("123", Integer.class);
        assertNotNull(result);
        assertEquals(123, (int) result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testCreateNumberWithLong() {
        Number result = TypeHandler.createNumber("123");
        assertNotNull(result);
        assertEquals(123, result);
    }

    @Test
    public void testCreateNumberWithDouble() {
        Number result = TypeHandler.createNumber("123.45");
        assertNotNull(result);
        assertEquals(123.45, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidInput() {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            assertNotNull(result);
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateClassNotFound() throws ParseException {
        TypeHandler.createClass("InvalidClassName");
    }

    @Test
    public void testCreateURL() {
        try {
            URL result = TypeHandler.createURL("http://www.example.com");
            assertNotNull(result);
            assertEquals(new URL("http://www.example.com"), result);
        } catch (ParseException | MalformedURLException e) {
            fail("Should not throw an exception");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateURLMalformed() throws ParseException {
        TypeHandler.createURL("invalidUrl");
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] result = TypeHandler.createFiles("folder");
            assertNotNull(result);
            assertTrue(result.length > 0);
            for (File file : result) {
                assertTrue(file.isDirectory());
            }
        } catch (UnsupportedOperationException e) {
            fail("Not yet implemented");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesNotImplemented() throws ParseException {
        TypeHandler.createFiles("folder");
    }
}