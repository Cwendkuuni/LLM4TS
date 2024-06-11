import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertNull(pointer.tail());
    }

    @Test
    public void testConstructorWithSegments() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        assertFalse(pointer.matches());

        assertEquals("foo", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNotNull(pointer.tail());

        pointer = pointer.tail();
        assertFalse(pointer.matches());

        assertEquals("bar", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer pointer = new JsonPointer("foo/bar");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        assertFalse(pointer.matches());

        JsonPointer matchedPointer = pointer.matchProperty("foo");
        assertNotNull(matchedPointer);
        assertFalse(matchedPointer.matches());

        assertEquals("bar", matchedPointer.getMatchingProperty());
        assertNull(matchedPointer.getMatchingIndex());
        assertNull(matchedPointer.tail());

        matchedPointer = matchedPointer.matchProperty("bar");
        assertNull(matchedPointer);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0/1");
        assertFalse(pointer.matches());

        JsonPointer matchedPointer = pointer.matchElement(0);
        assertNotNull(matchedPointer);
        assertFalse(matchedPointer.matches());

        assertNull(matchedPointer.getMatchingProperty());
        assertEquals(1, matchedPointer.getMatchingIndex());
        assertNotNull(matchedPointer.tail());

        matchedPointer = matchedPointer.matchElement(1);
        assertNull(matchedPointer);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());

        pointer = new JsonPointer();
        assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/foo");
        JsonPointer pointer2 = new JsonPointer("/foo");
        JsonPointer pointer3 = new JsonPointer("/bar");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/foo");
        JsonPointer pointer2 = new JsonPointer("/foo");
        JsonPointer pointer3 = new JsonPointer("/bar");

        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals("foo"));
    }

}