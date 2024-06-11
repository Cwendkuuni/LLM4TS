import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithMessageAndLong() {
        Validate.isTrue(true, "The value %s is not true", 10);
        try {
            Validate.isTrue(false, "The value %s is not true", 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 10 is not true", e.getMessage());
        }
    }

    @Test
    public void testIsTrueWithMessageAndDouble() {
        Validate.isTrue(true, "The value %s is not true", 10.5);
        try {
            Validate.isTrue(false, "The value %s is not true", 10.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 10.5 is not true", e.getMessage());
        }
    }

    @Test
    public void testIsTrueWithMessageAndObjectArray() {
        Validate.isTrue(true, "The values %s are not true", new Object[]{10, 20});
        try {
            Validate.isTrue(false, "The values %s are not true", new Object[]{10, 20});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The values [10, 20] are not true", e.getMessage());
        }
    }

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated expression is false", e.getMessage());
        }
    }

    @Test
    public void testNotNull() {
        Integer object = 10;
        Integer result = Validate.notNull(object);
        assertEquals(object, result);

        try {
            Validate.notNull(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("The validated object is null", e.getMessage());
        }
    }

    @Test
    public void testNotNullWithMessageAndObjectArray() {
        Integer object = 10;
        Integer result = Validate.notNull(object, "The object %s is null", "myObject");
        assertEquals(object, result);

        try {
            Validate.notNull(null, "The object %s is null", "myObject");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("The object myObject is null", e.getMessage());
        }
    }
    
    // Additional test methods for other Validate class methods go here...

}