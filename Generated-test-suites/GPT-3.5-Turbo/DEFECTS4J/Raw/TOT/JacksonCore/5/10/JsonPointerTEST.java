import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testConstructEmptyPointer() {
        JsonPointer pointer = new JsonPointer();
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testCompileEmptyString() {
        JsonPointer pointer = JsonPointer.compile("");
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testCompileInvalidString() {
        try {
            JsonPointer pointer = JsonPointer.compile("invalid");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCompileValidString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/0");
        
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("invalid"));
        assertNotNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchElement(-1));
        assertNotNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/foo/bar/0");
        
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("invalid"));
        assertNotNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchElement(-1));
        assertNotNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("invalid"));
        assertNotNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0");
        
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("invalid"));
        assertNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchElement(-1));
        assertNotNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/foo/bar").tail();
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        
        assertEquals("/foo/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar");
        JsonPointer pointer2 = new JsonPointer("/foo/bar");
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar");
        JsonPointer pointer2 = new JsonPointer("/foo/bar");
        JsonPointer pointer3 = new JsonPointer("/foo");
        
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals("Pointer"));
    }
}