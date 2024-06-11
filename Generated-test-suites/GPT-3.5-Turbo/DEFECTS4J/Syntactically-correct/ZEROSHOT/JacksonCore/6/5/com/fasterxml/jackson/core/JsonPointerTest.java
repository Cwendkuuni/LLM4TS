import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testCompile() {
        // Test empty input
        JsonPointer empty = JsonPointer.compile("");
        assertTrue(empty.matches());
        
        // Test valid input
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("/foo/bar", pointer.toString());
        
        // Test invalid input
        try {
            JsonPointer.compile("foo/bar");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testValueOf() {
        // Test empty input
        JsonPointer empty = JsonPointer.valueOf("");
        assertTrue(empty.matches());
        
        // Test valid input
        JsonPointer pointer = JsonPointer.valueOf("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("/foo/bar", pointer.toString());
        
        // Test invalid input
        try {
            JsonPointer.valueOf("foo/bar");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testMatches() {
        JsonPointer empty = JsonPointer.compile("");
        assertTrue(empty.matches());
        
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer empty = JsonPointer.compile("");
        assertNull(empty.getMatchingProperty());
        
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("foo", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer empty = JsonPointer.compile("");
        assertEquals(-1, empty.getMatchingIndex());
        
        JsonPointer pointer = JsonPointer.compile("/foo/1");
        assertEquals(1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer empty = JsonPointer.compile("");
        assertFalse(empty.mayMatchProperty());
        
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer empty = JsonPointer.compile("");
        assertFalse(empty.mayMatchElement());
        
        JsonPointer pointer = JsonPointer.compile("/foo/1");
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer empty = JsonPointer.compile("");
        assertNull(empty.matchProperty("foo"));
        
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer nextSegment = pointer.matchProperty("foo");
        assertNotNull(nextSegment);
        assertEquals("/bar", nextSegment.toString());
        
        assertNull(pointer.matchProperty("baz"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer empty = JsonPointer.compile("");
        assertNull(empty.matchElement(1));
        
        JsonPointer pointer = JsonPointer.compile("/foo/1");
        JsonPointer nextSegment = pointer.matchElement(1);
        assertNotNull(nextSegment);
        assertEquals("", nextSegment.toString());
        
        assertNull(pointer.matchElement(2));
    }
    
    @Test
    public void testTail() {
        JsonPointer empty = JsonPointer.compile("");
        assertEquals(empty, empty.tail());
        
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer nextSegment = pointer.tail();
        assertNotNull(nextSegment);
        assertEquals("/bar", nextSegment.toString());
    }
    
    @Test
    public void testToString() {
        JsonPointer empty = JsonPointer.compile("");
        assertEquals("", empty.toString());
        
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer empty1 = JsonPointer.compile("");
        JsonPointer empty2 = JsonPointer.compile("");
        assertEquals(empty1.hashCode(), empty2.hashCode());
        
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer empty1 = JsonPointer.compile("");
        JsonPointer empty2 = JsonPointer.compile("");
        assertTrue(empty1.equals(empty2));
        
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));
        
        assertFalse(empty1.equals(pointer1));
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(new Object()));
    }
    
}