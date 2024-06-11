import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_Object() {
        // Typical use case
        String str = "testString";
        Object obj = new Object();
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertNotNull(result);
            assertEquals(Object.class, result.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        
        // Edge case
        str = "testString";
        obj = PatternOptionBuilder.NUMBER_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        
        // Error scenario
        str = "testString";
        obj = "invalidClass";
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertNull(result);
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Unable to create an instance of:"));
        }
    }

    @Test
    public void testCreateValue_Class() {
        // Typical use case
        String str = "java.lang.String";
        Class<?> clazz = String.class;
        try {
            Object result = TypeHandler.createValue(str, clazz);
            assertNotNull(result);
            assertEquals(String.class, result.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        
        // Edge case
        str = "java.lang.String";
        clazz = PatternOptionBuilder.URL_VALUE;
        try {
            Object result = TypeHandler.createValue(str, clazz);
            assertNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
        
        // Error scenario
        str = "invalidClass";
        clazz = String.class;
        try {
            Object result = TypeHandler.createValue(str, clazz);
            assertNull(result);
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Unable to find the class:"));
        }
    }

    // Additional test cases for other methods in TypeHandler class

}