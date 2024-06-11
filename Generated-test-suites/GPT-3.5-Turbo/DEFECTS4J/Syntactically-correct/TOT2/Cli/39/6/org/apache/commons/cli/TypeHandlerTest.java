import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        // Typical use case
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);

        // Edge case
        try {
            TypeHandler.createValue("test", null);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Error scenario
        try {
            TypeHandler.createValue("test", Double.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObject() {
        // Typical use case
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Edge case
        try {
            TypeHandler.createObject("nonExistingClass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    // More test cases for other methods...

}