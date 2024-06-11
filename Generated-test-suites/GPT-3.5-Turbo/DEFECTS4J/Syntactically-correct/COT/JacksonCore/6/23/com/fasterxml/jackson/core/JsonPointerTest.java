
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/path/to/node");
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("path", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());

        JsonPointer invalidPointer = JsonPointer.compile("path/to/node");
        assertNull(invalidPointer);
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/path/to/node");
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("path", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());

        JsonPointer invalidPointer = JsonPointer.valueOf("path/to/node");
        assertNull(invalidPointer);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.valueOf("/path/to/node");
        assertNotNull(pointer);
        assertEquals("/path/to/node", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.valueOf("/path/to/node");
        JsonPointer pointer2 = JsonPointer.valueOf("/path/to/node");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.valueOf("/path/to/node");
        JsonPointer pointer2 = JsonPointer.valueOf("/path/to/node");
        assertTrue(pointer1.equals(pointer2));

        JsonPointer pointer3 = JsonPointer.valueOf("/path/to/another/node");
        assertFalse(pointer1.equals(pointer3));
    }
}

