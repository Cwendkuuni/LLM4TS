import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testComposition() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/baz/qux");

        JsonPointer pointer3 = JsonPointer.fromSegment("foo", "bar");
        JsonPointer pointer4 = JsonPointer.fromSegment("baz", "qux");

        assertEquals(pointer1, pointer3);
        assertEquals(pointer2, pointer4);
    }

    @Test
    public void testMatching() {
        JsonPointer pointer = JsonPointer.compile("/foo");

        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchElement());
        assertTrue(pointer.mayMatchProperty());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = pointer.tail();

        assertFalse(pointer.matches());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(null, pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");

        JsonPointer next = pointer.matchProperty("foo");

        assertNotNull(next);
        assertTrue(next.matches());
    }

    @Test
    public void testMatchingElement() {
        JsonPointer pointer = JsonPointer.compile("/0");

        JsonPointer next = pointer.matchElement(0);

        assertNotNull(next);
        assertTrue(next.matches());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");

        String expected = "/foo/bar";
        String actual = pointer.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");

        assertEquals(pointer1, pointer2);
    }
}