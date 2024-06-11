import org.junit.*;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithLongValue() {
        try {
            Validate.isTrue(true, "The value %d is not true", 5L);
        } catch (IllegalArgumentException e) {
            fail("isTrue method with long value failed");
        }
    }

    @Test
    public void testNotNullWithNonNullObject() {
        assertNotNull(Validate.notNull("Test"));
    }

    @Test
    public void testNotEmptyArray() {
        String[] array = {"Test"};
        assertArrayEquals(array, Validate.notEmpty(array));
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = List.of("Test");
        assertEquals(list, Validate.notEmpty(list));
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, String> map = Map.of("key", "value");
        assertEquals(map, Validate.notEmpty(map));
    }

    @Test
    public void testNotEmptyCharSequence() {
        assertEquals("Test", Validate.notEmpty("Test"));
    }

    @Test
    public void testNotBlankWithNonBlankChars() {
        assertEquals("Test", Validate.notBlank("Test"));
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = {"Test", null};
        assertArrayEquals(array, Validate.noNullElements(array));
    }

    @Test
    public void testNoNullElementsCollection() {
        List<String> list = List.of("Test", null);
        assertIterableEquals(list, Validate.noNullElements(list));
    }

    @Test
    public void testValidIndexArray() {
        String[] array = {"Test"};
        assertEquals(array, Validate.validIndex(array, 0));
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = List.of("Test");
        assertEquals(list, Validate.validIndex(list, 0));
    }

    @Test
    public void testValidIndexCharSequence() {
        assertEquals("Test", Validate.validIndex("Test", 0));
    }

    @Test
    public void testValidStateWithTrueExpression() {
        Validate.validState(true);
    }

    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("Test", "^[a-zA-Z]+$");
    }

    @Test
    public void testInclusiveBetween() {
        Validate.inclusiveBetween(1, 5, 3);
    }

    @Test
    public void testExclusiveBetween() {
        Validate.exclusiveBetween(1, 5, 3);
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "Test");
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(Object.class, String.class);
    }

}