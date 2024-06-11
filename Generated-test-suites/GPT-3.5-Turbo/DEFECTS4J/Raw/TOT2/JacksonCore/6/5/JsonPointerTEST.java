import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        
        assertNotNull(pointer);
        assertEquals("/foo/bar/baz", pointer.toString());
    }
    
    @Test
    public void testCompileEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");

        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("invalid/pointer");
    }
    
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "baz", JsonPointer.EMPTY);

        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);

        assertEquals("bar", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/foo", "2", JsonPointer.EMPTY);

        assertEquals(2, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);

        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo", "2", JsonPointer.EMPTY);

        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
        JsonPointer result = pointer.matchProperty("bar");

        assertNotNull(result);
        assertEquals(JsonPointer.EMPTY, result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo", "2", JsonPointer.EMPTY);
        JsonPointer result = pointer.matchElement(2);

        assertNotNull(result);
        assertEquals(JsonPointer.EMPTY, result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "baz", new JsonPointer("/bar", "qux", JsonPointer.EMPTY));
        JsonPointer result = pointer.tail();

        assertNotNull(result);
        assertEquals("/bar/qux", result.toString());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");

        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");

        assertNotNull(pointer.hashCode());
    }
}