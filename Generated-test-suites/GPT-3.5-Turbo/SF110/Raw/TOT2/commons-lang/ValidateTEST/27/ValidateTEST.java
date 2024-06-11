import static org.junit.Assert.*;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueWithMessageAndLongValue() {
        try {
            Validate.isTrue(false, "Test message: %d", 10L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Test message: 10", e.getMessage());
        }
    }

    @Test
    public void testNotNull() {
        assertNotNull(Validate.notNull("Test"));
    }
    
    @Test
    public void testNotEmptyArray() {
        try {
            Validate.notEmpty(new String[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated array is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotEmptyCollection() {
        try {
            Validate.notEmpty(new ArrayList<>());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated collection is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotEmptyMap() {
        try {
            Validate.notEmpty(new HashMap<>());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated map is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotBlank() {
        try {
            Validate.notBlank("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated character sequence is blank", e.getMessage());
        }
    }
    
    @Test
    public void testNoNullElementsInArray() {
        try {
            Validate.noNullElements(new String[]{"Test", null});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated array contains null element at index: 1", e.getMessage());
        }
    }
    
    @Test
    public void testValidIndexInArray() {
        try {
            Validate.validIndex(new String[]{"Test"}, 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("The validated array index is invalid: 1", e.getMessage());
        }
    }
    
    @Test
    public void testValidState() {
        Validate.validState(true);
    }
    
    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("Test", "[a-zA-Z]+");
    }
    
    @Test
    public void testInclusiveBetween() {
        Validate.inclusiveBetween(1, 10, 5);
    }
    
    @Test
    public void testExclusiveBetween() {
        Validate.exclusiveBetween(1, 10, 5);
    }
    
    @Test
    public void testIsInstanceOf() {
        Object obj = "Test";
        Validate.isInstanceOf(String.class, obj);
    }
    
    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(Object.class, String.class);
    }
}