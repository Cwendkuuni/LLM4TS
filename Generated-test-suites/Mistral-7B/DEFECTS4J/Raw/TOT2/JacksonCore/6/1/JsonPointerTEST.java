import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {
    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "bar";
        JsonPointer pointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testPropertyMatch() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer property = new JsonPointer("/foo", null);
        assertEquals(property, pointer.matchProperty("bar"));
        assertNull(pointer.matchProperty("baz"));
        assertNull(pointer.tail());
    }

    @Test
    public void testElementMatch() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer element = new JsonPointer("/foo", null);
        assertEquals(element, pointer.matchElement(0));
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.tail());
    }

    @Test
    public void testTail() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        assertEquals("/bar/baz", tail.toString());
    }

    @Test
    public void testConstructWithNullInput() {
        JsonPointer pointer = new JsonPointer(null, null, null);
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testConstructWithEmptyInput() {
        JsonPointer pointer = new JsonPointer("", "", null);
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testConstructWithEmptyProperty() {
        String input = "/foo/";
        JsonPointer pointer = new JsonPointer(input, "", null);
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testConstructWithEmptySegment() {
        String input = "/foo/";
        JsonPointer pointer = new JsonPointer(input, "foo", null);
        assertEquals(JsonPointer.EMPTY, pointer);
    }
}