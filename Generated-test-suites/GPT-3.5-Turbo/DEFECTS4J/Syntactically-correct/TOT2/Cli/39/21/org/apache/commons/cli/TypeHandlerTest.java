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
            TypeHandler.createValue("test", Object.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Success
        }
    }

    @Test
    public void testCreateObject() {
        // Typical use case
        try {
            Object result = TypeHandler.createObject("java.lang.Object");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            TypeHandler.createObject("NonExistingClass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Success
        }
    }

    @Test
    public void testCreateNumber() {
        // Typical use case
        try {
            Number result = TypeHandler.createNumber("10");
            assertEquals(10L, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Edge case
        try {
            Number result = TypeHandler.createNumber("10.5");
            assertEquals(10.5, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        // Error scenario
        try {
            TypeHandler.createNumber("notANumber");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Success
        }
    }

    // Additional test methods for other public methods in TypeHandler class

}