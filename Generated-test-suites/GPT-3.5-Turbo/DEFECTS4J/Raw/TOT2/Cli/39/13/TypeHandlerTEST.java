import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() {
        // Test creating Object from String
        try {
            Object obj = TypeHandler.createValue("test", Object.class);
            assertNotNull(obj);
            assertEquals("test", obj);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateValueWithClass() {
        // Test creating Object from Class
        try {
            Object obj = TypeHandler.createValue("test", String.class);
            assertNotNull(obj);
            assertEquals("test", obj);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateNumber() {
        // Test creating Number
        try {
            Number num = TypeHandler.createNumber("123");
            assertNotNull(num);
            assertEquals(123, num.intValue());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateClass() {
        // Test creating Class
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertNotNull(clazz);
            assertEquals(String.class, clazz);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        // Test createDate method
        TypeHandler.createDate("2021-01-01");
    }

    @Test
    public void testCreateURL() {
        // Test creating URL
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertNotNull(url);
            assertEquals("http://www.example.com", url.toString());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateFile() {
        // Test creating File
        File file = TypeHandler.createFile("example.txt");
        assertNotNull(file);
        assertEquals("example.txt", file.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        // Test createFiles method
        TypeHandler.createFiles("/path/to/files");
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectException() throws ParseException {
        // Test createObject method with exception
        TypeHandler.createObject("invalidClass");
    }

    @Test(expected = ParseException.class)
    public void testCreateURLException() throws ParseException {
        // Test createURL method with exception
        TypeHandler.createURL("invalidUrl");
    }

    @Test
    public void testCreateNumberException() {
        // Test createNumber method with exception
        try {
            Number num = TypeHandler.createNumber("abc");
            fail("NumberFormatException should be thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateClassException() throws ParseException {
        // Test createClass method with exception
        TypeHandler.createClass("invalidClass");
    }
}