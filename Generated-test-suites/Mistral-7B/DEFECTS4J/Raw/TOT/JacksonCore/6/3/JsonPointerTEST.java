import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String pointerString = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(pointerString, pointer.toString());
    }

    @Test
    public void testPointerWithElement() {
        String pointerString = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/", pointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String invalidPointer = "~bad";
        JsonPointer.compile(invalidPointer);
    }

    @Test
    public void testPointerWithElementAndProperty() {
        String pointerString = "/foo/0/baz";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        JsonPointer nextPointer = pointer.tail();
        assertEquals("baz", nextPointer.getMatchingProperty());
        assertEquals(-1, nextPointer.getMatchingIndex());
        assertNull(nextPointer.tail());
    }

    @Test
    public void testPointerWithQuotedSegment() {
        String pointerString = "/foo/~1/bar";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        JsonPointer nextPointer = pointer.tail();
        assertEquals("bar", nextPointer.getMatchingProperty());
        assertEquals(-1, nextPointer.getMatchingIndex());
        assertNull(nextPointer.tail());
    }

    @Test
    public void testPointerWithEmptySegment() {
        String pointerString = "/foo/~";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }
}