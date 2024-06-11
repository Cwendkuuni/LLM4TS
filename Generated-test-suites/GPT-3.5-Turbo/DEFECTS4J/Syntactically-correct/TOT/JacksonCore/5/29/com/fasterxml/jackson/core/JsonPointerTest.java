import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        // Create an empty JsonPointer
        JsonPointer pointer = new JsonPointer();
        
        // Verify that the JsonPointer matches
        assertTrue(pointer.matches());
        
        // Verify that the matching property name and index are empty
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        // Verify that it cannot match a property or an element
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        
        // Verify that the tail is null
        assertNull(pointer.tail());
    }
    
    @Test
    public void testJsonPointerWithMatchingSegment() {
        // Create a JsonPointer with a matching segment
        JsonPointer pointer = new JsonPointer("/matchingSegment");
        
        // Verify that the JsonPointer does not match
        assertFalse(pointer.matches());
        
        // Verify that the matching property name is "matchingSegment" and the matching index is -1
        assertEquals("matchingSegment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        // Verify that it may match a property but not an element
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        
        // Verify that the tail is null
        assertNull(pointer.tail());
    }
    
    @Test
    public void testJsonPointerWithMatchingIndex() {
        // Create a JsonPointer with a matching index
        JsonPointer pointer = new JsonPointer("/3");
        
        // Verify that the JsonPointer does not match
        assertFalse(pointer.matches());
        
        // Verify that the matching property name is empty and the matching index is 3
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(3, pointer.getMatchingIndex());
        
        // Verify that it may match an element but not a property
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        
        // Verify that the tail is null
        assertNull(pointer.tail());
    }
    
    @Test
    public void testJsonPointerWithMatchingSegmentAndTail() {
        // Create a JsonPointer with a matching segment and a tail
        JsonPointer pointer = new JsonPointer("/matchingSegment/tail");
        
        // Verify that the JsonPointer does not match
        assertFalse(pointer.matches());
        
        // Verify that the matching property name is "matchingSegment" and the matching index is -1
        assertEquals("matchingSegment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        // Verify that it may match a property but not an element
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        
        // Verify that the tail is not null
        assertNotNull(pointer.tail());
    }
    
    @Test
    public void testJsonPointerWithMatchingIndexAndTail() {
        // Create a JsonPointer with a matching index and a tail
        JsonPointer pointer = new JsonPointer("/3/tail");
        
        // Verify that the JsonPointer does not match
        assertFalse(pointer.matches());
        
        // Verify that the matching property name is empty and the matching index is 3
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(3, pointer.getMatchingIndex());
        
        // Verify that it may match an element but not a property
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        
        // Verify that the tail is not null
        assertNotNull(pointer.tail());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidExpression() {
        // Create a JsonPointer with an invalid expression
        JsonPointer pointer = new JsonPointer("invalid");
        
        // This line should throw an IllegalArgumentException
    }
}