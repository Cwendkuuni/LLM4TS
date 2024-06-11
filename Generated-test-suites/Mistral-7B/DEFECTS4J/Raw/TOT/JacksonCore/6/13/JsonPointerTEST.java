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
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer1() {
        JsonPointer pointer = JsonPointer.compile("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer2() {
        JsonPointer pointer = JsonPointer.compile("/~bar");
    }

    @Test
    public void testNestedPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/foo", pointer.tail()._asString);
    }

    @Test
    public void testArrayPointer() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testArrayPointerWithProperty() {
        JsonPointer pointer = JsonPointer.compile("/0/prop");
        assertFalse(pointer.matches());
        assertEquals("prop", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testNestedArrayPointer() {
        JsonPointer pointer = JsonPointer.compile("/0/0/prop");
        assertFalse(pointer.matches());
        assertEquals("prop", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/0/0", pointer.tail()._asString);
    }

    @Test
    public void testCompositePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/prop");
        assertFalse(pointer.matches());
        assertEquals("prop", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/foo/0", pointer.tail()._asString);
    }

    @Test
    public void testRecursivePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/foo/0", pointer.tail()._asString);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer matching = new JsonPointer("/foo");
        JsonPointer expected = pointer.matchProperty("bar");
        assertEquals(expected, matching);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        JsonPointer matching = new JsonPointer("/foo");
        JsonPointer expected = pointer.matchElement(0);
        assertEquals(expected, matching);
    }

    @Test
    public void testMismatchedProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer matching = new JsonPointer("/baz");
        assertNull(pointer.matchProperty("bar"));
    }

    @Test
    public void testMismatchedIndex() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        JsonPointer matching = new JsonPointer("/foo");
        assertNull(pointer.matchElement(1));
    }
}