import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.Map;

public class ValidateTest {

    // 
    
    @Test
    public void testIsTrueWithMessageAndLong() {
        try {
            Validate.isTrue(false, "The value %d is not true", 10L);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 10 is not true", e.getMessage());
        }
    }
    
    @Test
    public void testIsTrueWithMessageAndDouble() {
        try {
            Validate.isTrue(false, "The value %.2f is not true", 5.5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 5.50 is not true", e.getMessage());
        }
    }
    
    @Test
    public void testIsTrueWithMessageAndObjectArray() {
        try {
            Validate.isTrue(false, "The values [%s, %s] are not true", "hello", 10);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The values [hello, 10] are not true", e.getMessage());
        }
    }
    
    @Test
    public void testIsTrue() {
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated expression is false", e.getMessage());
        }
    }
    
    @Test
    public void testNotNull() {
        Object obj = null;
        try {
            Validate.notNull(obj);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("The validated object is null", e.getMessage());
        }
    }
    
    @Test
    public void testNotNullWithMessageAndObjectArray() {
        Object obj = null;
        try {
            Validate.notNull(obj, "The object at index %d is null", 2);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("The object at index 2 is null", e.getMessage());
        }
    }
    
    // Add test methods for other public methods in the Validate class
    
    // 
    
}