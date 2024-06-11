import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testValidJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testInvalidJsonPointer() {
        try {
            JsonPointer.compile("foo/bar");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"foo/bar\"", e.getMessage());
        }
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/1");
        assertFalse(pointer.matches());
        assertEquals(1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        assertFalse(pointer1.matches());
        assertTrue(pointer1.mayMatchProperty());

        JsonPointer pointer2 = JsonPointer.compile("/1");
        assertFalse(pointer2.matches());
        assertFalse(pointer2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        assertFalse(pointer1.matches());
        assertFalse(pointer1.mayMatchElement());

        JsonPointer pointer2 = JsonPointer.compile("/1");
        assertFalse(pointer2.matches());
        assertTrue(pointer2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());

        JsonPointer next = pointer.matchProperty("foo");
        assertNotNull(next);
        assertTrue(next.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/1");
        assertFalse(pointer.matches());

        JsonPointer next = pointer.matchElement(1);
        assertNotNull(next);
        assertTrue(next.matches());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());

        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertFalse(tail.matches());
        assertEquals("/bar", tail.toString());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer3 = JsonPointer.compile("/bar/foo");

        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer1));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer2.equals(pointer3));
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer3 = JsonPointer.compile("/bar/foo");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
        assertNotEquals(pointer2.hashCode(), pointer3.hashCode());
    }
}