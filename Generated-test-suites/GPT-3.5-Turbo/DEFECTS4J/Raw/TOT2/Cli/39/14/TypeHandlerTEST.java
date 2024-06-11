import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        // Typical use case
        try {
            Object obj = TypeHandler.createValue("test", String.class);
            assertNotNull(obj);
            assertEquals("test", obj);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Edge case
        try {
            Object obj = TypeHandler.createValue("test", Object.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateObject() {
        // Typical use case
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertNotNull(obj);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            Object obj = TypeHandler.createObject("nonexistentclass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateNumber() {
        // Typical use case
        try {
            Number num = TypeHandler.createNumber("10");
            assertNotNull(num);
            assertEquals(10L, num.longValue());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            TypeHandler.createNumber("notanumber");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateClass() {
        // Typical use case
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertNotNull(clazz);
            assertEquals(String.class, clazz);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            TypeHandler.createClass("nonexistentclass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateURL() {
        // Typical use case
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertNotNull(url);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            TypeHandler.createURL("invalidurl");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateFile() {
        // Typical use case
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);

        // Error scenario
        File nonExistFile = TypeHandler.createFile("nonexistentfile");
        assertFalse(nonExistFile.exists());
    }

    @Test
    public void testCreateFiles() {
        // Edge case
        try {
            TypeHandler.createFiles("test");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
}