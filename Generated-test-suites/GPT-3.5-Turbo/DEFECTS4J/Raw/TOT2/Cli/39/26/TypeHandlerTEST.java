import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        try {
            Object obj = TypeHandler.createValue("testValue", String.class);
            assertEquals("testValue", obj);
        } catch (ParseException e) {
            fail("Exception thrown while creating value");
        }
    }

    @Test
    public void testCreateValueException() {
        try {
            TypeHandler.createValue("", null);
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.util.Date");
            assertTrue(obj instanceof Date);
        } catch (ParseException e) {
            fail("Exception thrown while creating object");
        }
    }

    @Test
    public void testCreateObjectException() {
        try {
            TypeHandler.createObject("InvalidClass");
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number num = TypeHandler.createNumber("10");
            assertEquals(10L, num);
        } catch (ParseException e) {
            fail("Exception thrown while creating number");
        }
    }

    @Test
    public void testCreateNumberException() {
        try {
            TypeHandler.createNumber("NotANumber");
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.util.Date");
            assertEquals(Date.class, clazz);
        } catch (ParseException e) {
            fail("Exception thrown while creating class");
        }
    }

    @Test
    public void testCreateClassException() {
        try {
            TypeHandler.createClass("InvalidClass");
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertEquals("http://www.example.com", url.toString());
        } catch (ParseException e) {
            fail("Exception thrown while creating URL");
        }
    }

    @Test
    public void testCreateURLException() {
        try {
            TypeHandler.createURL("invalidURL");
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("/path/to/file.txt");
        assertNotNull(file);
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("/path/to/files");
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
}