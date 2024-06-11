import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Test case 1
        try {
            Validate.isTrue(true);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail("Test case 1 failed");
        }

        // Test case 2
        try {
            Validate.isTrue(false);
            fail("Test case 2 failed");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated expression is false", e.getMessage());
        }
    }

    @Test
    public void testNotNull() {
        // Test case 1
        String obj = Validate.notNull("Hello, World!");
        assertEquals("Hello, World!", obj);

        // Test case 2
        try {
            Validate.notNull(null);
            fail("Test case 2 failed");
        } catch (NullPointerException e) {
            assertEquals("The validated object is null", e.getMessage());
        }
    }

    @Test
    public void testNotEmptyArray() {
        // Test case 1
        String[] array = Validate.notEmpty(new String[]{"Hello", "World"});
        assertArrayEquals(new String[]{"Hello", "World"}, array);

        // Test case 2
        try {
            Validate.notEmpty(new String[0]);
            fail("Test case 2 failed");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated array is empty", e.getMessage());
        }
    }

    // TODO: Add test cases for other public methods in the Validate class

}