
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testValidJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/0");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = pointer.tail();
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = pointer.tail();
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());

        pointer = pointer.tail();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("foo/bar/0");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = pointer.matchProperty("foo");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/bar");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = pointer.matchProperty("foo");
        assertNull(pointer);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());

        pointer = pointer.matchElement(0);
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/1");
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());

        pointer = pointer.matchElement(0);
        assertNull(pointer);
    }

    @Test
    public void testEquality() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer1));

        JsonPointer pointer3 = JsonPointer.compile("/bar");
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer3.equals(pointer1));

        assertFalse(pointer1.equals(null));
    }

}
