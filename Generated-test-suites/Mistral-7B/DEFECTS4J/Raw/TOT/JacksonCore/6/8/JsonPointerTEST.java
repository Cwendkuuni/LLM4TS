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
    public void testSimplePointer() throws IllegalArgumentException {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());

        assertEquals(null, pointer.matchProperty("baz"));
        assertEquals(pointer, pointer.matchProperty("foo"));
        assertEquals(pointer, pointer.matchElement(0));
        assertEquals(pointer, pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() throws IllegalArgumentException {
        String input = "~foo";
        JsonPointer.compile(input); // should throw an exception
    }

    @Test
    public void testMultipleSegmentsPointer() throws IllegalArgumentException {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/bar", pointer.tail().toString());
        assertEquals(input, pointer.toString());

        assertEquals(null, pointer.matchProperty("baz"));
        assertEquals(pointer.matchProperty("foo"), pointer.matchProperty("foo"));
        assertEquals(null, pointer.matchElement(1));
        assertEquals(pointer.matchElement(0), pointer.tail());
    }

    @Test
    public void testQuotedPointer() throws IllegalArgumentException {
        String input = "/foo/~1/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/bar", pointer.tail().toString());
        assertEquals(input, pointer.toString());

        assertEquals(null, pointer.matchProperty("baz"));
        assertEquals(pointer.matchProperty("foo"), pointer.matchProperty("foo"));
        assertEquals(null, pointer.matchElement(0));
        assertEquals(pointer.matchElement(1), pointer.tail());
    }
}