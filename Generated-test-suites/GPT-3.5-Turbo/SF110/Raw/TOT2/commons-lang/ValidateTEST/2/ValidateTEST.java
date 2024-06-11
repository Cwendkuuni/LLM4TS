import static org.junit.Assert.*;

import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        assertTrue(true);
        assertFalse(false);
    }

    @Test
    public void testNotNull() {
        assertNotNull(Validate.notNull("test"));
        assertNull(Validate.notNull(null));
    }

    @Test
    public void testNotEmptyArray() {
        String[] array = {"test"};
        assertArrayEquals(array, Validate.notEmpty(array));
        String[] emptyArray = {};
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(emptyArray));
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = new ArrayList<>();
        list.add("test");
        assertEquals(list, Validate.notEmpty(list));
        List<String> emptyList = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(emptyList));
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        assertEquals(map, Validate.notEmpty(map));
        Map<String, String> emptyMap = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(emptyMap));
    }

    @Test
    public void testNotEmptyCharSequence() {
        String chars = "test";
        assertEquals(chars, Validate.notEmpty(chars));
        String emptyChars = "";
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(emptyChars));
    }

    @Test
    public void testNotBlank() {
        String chars = "test";
        assertEquals(chars, Validate.notBlank(chars));
        String blankChars = "   ";
        assertThrows(IllegalArgumentException.class, () -> Validate.notBlank(blankChars));
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = {"test", "test"};
        assertArrayEquals(array, Validate.noNullElements(array));
        String[] nullArray = {null};
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(nullArray));
    }

    @Test
    public void testNoNullElementsCollection() {
        List<String> list = new ArrayList<>();
        list.add("test");
        assertEquals(list, Validate.noNullElements(list));
        List<String> nullList = new ArrayList<>();
        nullList.add(null);
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(nullList));
    }

    @Test
    public void testValidIndexArray() {
        String[] array = {"test", "test"};
        assertArrayEquals(array, Validate.validIndex(array, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(array, 3));
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = new ArrayList<>();
        list.add("test");
        assertEquals(list, Validate.validIndex(list, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(list, 1));
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "test";
        assertEquals(chars, Validate.validIndex(chars, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(chars, 5));
    }

    @Test
    public void testValidState() {
        Validate.validState(true);
        assertThrows(IllegalStateException.class, () -> Validate.validState(false));
    }

    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("test", "test");
        assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern("123", "test"));
    }

    @Test
    public void testInclusiveBetween() {
        Validate.inclusiveBetween(1, 3, 2);
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1, 3, 4));
    }

    @Test
    public void testExclusiveBetween() {
        Validate.exclusiveBetween(1, 3, 2);
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1, 3, 3));
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "test");
        assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(Integer.class, "test"));
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(Number.class, Integer.class);
        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(Integer.class, Number.class));
    }

}