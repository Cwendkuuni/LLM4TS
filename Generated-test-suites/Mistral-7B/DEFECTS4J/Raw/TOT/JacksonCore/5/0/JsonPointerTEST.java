import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/bar", pointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("baz");
    }

    @Test
    public void testQuotedPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar~1/baz");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/baz", pointer.tail().toString());
    }

    @Test
    public void testTailPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer tail = pointer.tail();
        assertEquals("/bar", tail.toString());
        tail = tail.tail();
        assertNull(tail);
    }

    @Test
    public void testPropertyMatching() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer propertyMatch = new JsonPointer("/foo/bar");
        assertTrue(propertyMatch.matches());
        assertEquals(pointer, propertyMatch.tail());
    }

    @Test
    public void testElementMatching() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
        JsonPointer elementMatch = new JsonPointer("/foo/1/bar");
        assertFalse(elementMatch.matches());
        assertNull(elementMatch.tail());
        assertEquals(pointer, elementMatch);
    }

    @Test
    public void testMatchesEmptyArray() {
        JsonPointer pointer = JsonPointer.compile("/foo/[]");
        assertTrue(pointer.matches());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testMatchesEmptyObject() {
        JsonPointer pointer = JsonPointer.compile("/foo/{}");
        assertTrue(pointer.matches());
        assertEquals("/foo", pointer.toString());
    }
}