import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        // Test for empty pointer
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());

        // Test for non-empty pointer
        JsonPointer pointer = new JsonPointer("/foo/bar/baz");
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        // Test for matching property
        JsonPointer pointer = new JsonPointer("/foo");
        assertEquals("foo", pointer.getMatchingProperty());

        // Test for non-matching property
        JsonPointer pointer2 = new JsonPointer("/bar");
        assertNull(pointer2.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        // Test for matching index
        JsonPointer pointer = new JsonPointer("/0");
        assertEquals(0, pointer.getMatchingIndex());

        // Test for non-matching index
        JsonPointer pointer2 = new JsonPointer("/1");
        assertEquals(-1, pointer2.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        // Test for matching property
        JsonPointer pointer = new JsonPointer("/foo");
        assertTrue(pointer.mayMatchProperty());

        // Test for non-matching property
        JsonPointer pointer2 = new JsonPointer("/bar");
        assertFalse(pointer2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        // Test for matching element
        JsonPointer pointer = new JsonPointer("/0");
        assertTrue(pointer.mayMatchElement());

        // Test for non-matching element
        JsonPointer pointer2 = new JsonPointer("/1");
        assertFalse(pointer2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        // Test for matching property
        JsonPointer pointer = new JsonPointer("/foo");
        JsonPointer nextSegment = pointer.matchProperty("foo");
        assertNotNull(nextSegment);
        JsonPointer nextSegment2 = pointer.matchProperty("bar");
        assertNull(nextSegment2);
    }

    @Test
    public void testMatchElement() {
        // Test for matching element
        JsonPointer pointer = new JsonPointer("/0");
        JsonPointer nextSegment = pointer.matchElement(0);
        assertNotNull(nextSegment);
        JsonPointer nextSegment2 = pointer.matchElement(1);
        assertNull(nextSegment2);
    }

    @Test
    public void testTail() {
        // Test for tail
        JsonPointer pointer = new JsonPointer("/foo/bar");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("/bar", tail.toString());
    }

    @Test
    public void testToString() {
        // Test for toString
        JsonPointer pointer = new JsonPointer("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testHashCode() {
        // Test for hashCode
        JsonPointer pointer = new JsonPointer("/foo/bar");
        JsonPointer pointer2 = new JsonPointer("/foo/bar");
        assertEquals(pointer.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test for equals
        JsonPointer pointer = new JsonPointer("/foo/bar");
        JsonPointer pointer2 = new JsonPointer("/foo/bar");
        assertTrue(pointer.equals(pointer2));

        JsonPointer pointer3 = new JsonPointer("/foo/baz");
        assertFalse(pointer.equals(pointer3));
    }

    @Test
    public void testCompile() {
        // Test for valid input
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertNotNull(pointer);

        // Test for invalid input
        String input2 = "foo/bar";
        try {
            JsonPointer pointer2 = JsonPointer.compile(input2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"foo/bar\"", e.getMessage());
        }
    }

    @Test
    public void testValueOf() {
        // Test for valid input
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.valueOf(input);
        assertNotNull(pointer);

        // Test for invalid input
        String input2 = "foo/bar";
        try {
            JsonPointer pointer2 = JsonPointer.valueOf(input2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"foo/bar\"", e.getMessage());
        }
    }
    
    //TODO: Add test cases for fromSegment method
    
}