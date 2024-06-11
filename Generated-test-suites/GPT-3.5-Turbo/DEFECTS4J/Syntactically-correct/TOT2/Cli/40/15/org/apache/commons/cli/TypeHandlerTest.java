import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        try {
            Object obj = TypeHandler.createValue("test", String.class);
            assertEquals("test", obj);
            
            obj = TypeHandler.createValue("java.lang.String", Class.class);
            assertNotNull(obj);
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertNotNull(obj);
            
            obj = TypeHandler.createObject("NonExistingClass");
            fail("No exception thrown for non-existing class");
        } catch (ParseException e) {
            // Exception expected
        }
    }

    // Add test cases for other methods...

}