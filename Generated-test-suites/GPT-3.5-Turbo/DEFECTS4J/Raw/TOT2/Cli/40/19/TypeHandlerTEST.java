import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithClass() {
        try {
            Object result = TypeHandler.createValue("testString", String.class);
            assertEquals("testString", result);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateValueWithString() {
        try {
            Object result = TypeHandler.createValue("testString", PatternOptionBuilder.STRING_VALUE);
            assertEquals("testString", result);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateValueWithNonExistingClass() {
        try {
            Object result = TypeHandler.createValue("NonExistingClass", PatternOptionBuilder.OBJECT_VALUE);
            assertNull(result);
        } catch (ParseException e) {
            assertEquals("java.lang.ClassNotFoundException; Unable to find the class: NonExistingClass", e.getMessage());
        }
    }

    @Test
    public void testCreateNumberLong() {
        try {
            Number result = TypeHandler.createNumber("123");
            assertEquals(123L, result.longValue());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateNumberDouble() {
        try {
            Number result = TypeHandler.createNumber("123.456");
            assertEquals(123.456, result.doubleValue(), 0.0);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL result = TypeHandler.createURL("http://www.example.com");
            assertEquals(new URL("http://www.example.com"), result);
        } catch (ParseException | MalformedURLException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("testFile.txt");
        assertEquals("testFile.txt", result.getName());
    }

    @Test
    public void testOpenFile() {
        try {
            FileInputStream result = TypeHandler.openFile("testFile.txt");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("testFolder");
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // This exception is expected
        }
    }
}