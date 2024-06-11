import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithMessageAndLongValue() {
        try {
            Validate.isTrue(2 == 3, "Test Message %d", 5L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotNull() {
        assertNotNull(Validate.notNull("test"));
    }

    @Test
    public void testNotEmptyArray() {
        assertArrayEquals(new String[]{"test"}, Validate.notEmpty(new String[]{"test"}));
    }

    @Test
    public void testNotEmptyCollection() {
        assertTrue(Validate.notEmpty(Arrays.asList("test")));
    }

    @Test
    public void testNotBlank() {
        assertEquals("test", Validate.notBlank("test"));
    }

    @Test
    public void testNoNullElementsArray() {
        assertArrayEquals(new String[]{"test"}, Validate.noNullElements(new String[]{"test"}));
    }

    @Test
    public void testValidIndexArray() {
        assertArrayEquals(new String[]{"test"}, Validate.validIndex(new String[]{"test"}, 0));
    }

    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("test", "test");
    }

    @Test
    public void testInclusiveBetween() {
        Validate.inclusiveBetween(0, 10, 5);
    }

    @Test
    public void testExclusiveBetween() {
        Validate.exclusiveBetween(1, 5, 3);
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "test");
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(Number.class, Integer.class);
    }

}