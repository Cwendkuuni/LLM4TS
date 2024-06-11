import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_Object() {
        // Typical use case
        assertEquals("String", TypeHandler.createValue("String", String.class));
        
        // Edge case
        assertNull(TypeHandler.createValue("Unknown", Integer.class));

        // Error scenario
        try {
            TypeHandler.createValue("Test", null);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObject() {
        // Typical use case
        assertNotNull(TypeHandler.createObject("java.lang.String"));
        
        // Edge case
        try {
            TypeHandler.createObject("UnknownClass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateNumber() {
        // Typical use case
        assertEquals(10L, TypeHandler.createNumber("10"));

        // Edge case
        assertEquals(10.5D, TypeHandler.createNumber("10.5"));

        // Error scenario
        try {
            TypeHandler.createNumber("NotANumber");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass() {
        // Typical use case
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));

        // Edge case
        try {
            TypeHandler.createClass("UnknownClass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateURL() {
        // Typical use case
        try {
            assertNotNull(TypeHandler.createURL("http://www.example.com"));
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            TypeHandler.createURL("invalid-url");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFile() {
        // Typical use case
        File file = TypeHandler.createFile("testFile.txt");
        assertEquals("testFile.txt", file.getName());

        // Edge case
        File invalidFile = TypeHandler.createFile("");
        assertFalse(invalidFile.exists());
    }

    @Test
    public void testOpenFile() {
        // Typical use case
        try {
            FileInputStream fis = TypeHandler.openFile("testFile.txt");
            assertNotNull(fis);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            TypeHandler.openFile("nonExistentFile.txt");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFiles() {
        // Edge case
        try {
            TypeHandler.createFiles("test_directory");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}