import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithClassObject() {
        try {
            Object value = TypeHandler.createValue("test", String.class);
            assertEquals("test", value);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertNotNull(obj);
            assertEquals("java.lang.String", obj.getClass().getName());
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateNumberWithLong() {
        try {
            Number number = TypeHandler.createNumber("123");
            assertTrue(number instanceof Long);
            assertEquals(123L, number);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateNumberWithDouble() {
        try {
            Number number = TypeHandler.createNumber("123.456");
            assertTrue(number instanceof Double);
            assertEquals(123.456, number);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertNotNull(clazz);
            assertEquals("java.lang.String", clazz.getName());
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateNotImplemented() {
        TypeHandler.createDate("2022-01-01");
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("https://www.example.com");
            assertNotNull(url);
            assertEquals("https://www.example.com", url.toString());
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
        assertEquals("test.txt", file.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFilesNotImplemented() {
        TypeHandler.createFiles("/path/to/files");
    }

}