import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testConstructorEmpty() {
        JsonPointer pointer = new JsonPointer();
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");

        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("foo/bar");
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer parent = JsonPointer.compile("/foo");
        JsonPointer child = new JsonPointer("foo/bar", "bar", JsonPointer.EMPTY);

        JsonPointer result = parent.matchProperty("foo");

        assertEquals(child, result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer parent = JsonPointer.compile("/0");
        JsonPointer child = JsonPointer.EMPTY;

        JsonPointer result = parent.matchElement(0);

        assertEquals(child, result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer tail = pointer.tail();

        assertNotNull(tail);
        assertTrue(tail.matches());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");

        assertEquals(pointer1, pointer2);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");

        assertEquals("/foo/bar", pointer.toString());
    }
}