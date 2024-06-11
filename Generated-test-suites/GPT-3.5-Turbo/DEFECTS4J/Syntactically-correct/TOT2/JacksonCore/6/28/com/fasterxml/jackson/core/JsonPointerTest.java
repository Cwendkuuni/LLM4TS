import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    // Test cases for the constructor that creates an empty JsonPointer
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    // Test cases for the constructor that creates a non-empty JsonPointer
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    // Test cases for compiling a valid JSON Pointer
    @Test
    public void testCompileValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/test/segment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    // Test cases for compiling an invalid JSON Pointer
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("test/segment");
    }
    
    // Test case for matching a property
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/segment");
        JsonPointer next = pointer.matchProperty("segment");
        assertNotNull(next);
    }
    
    // Test case for matching an element
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        JsonPointer next = pointer.matchElement(0);
        assertNotNull(next);
    }
    
    // Test case for accessing a sub-pointer
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/test/segment");
        JsonPointer next = pointer.tail();
        assertNotNull(next);
    }
    
    // Test case for parsing an index
    @Test
    public void testParseIndex() {
        int index = JsonPointer._parseIndex("123");
        assertEquals(123, index);
    }
    
    // Test case for parsing a quoted tail
    @Test
    public void testParseQuotedTail() {
        JsonPointer pointer = JsonPointer.compile("/test/~0~1");
        assertFalse(pointer.matches());
        assertEquals("/test/~0~1", pointer.toString());
    }
}