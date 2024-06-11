import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testJsonPointerMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        
        pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        
        pointer = JsonPointer.compile("/foo/bar/0");
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testJsonPointerGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.getMatchingProperty());
        
        pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
        
        pointer = JsonPointer.compile("/foo/0");
        assertNull(pointer.getMatchingProperty());
    }
    
    @Test
    public void testJsonPointerGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = JsonPointer.compile("/0");
        assertEquals(0, pointer.getMatchingIndex());
        
        pointer = JsonPointer.compile("/foo");
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testJsonPointerMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchProperty());
        
        pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.mayMatchProperty());
        
        pointer = JsonPointer.compile("/foo/0");
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testJsonPointerMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());
        
        pointer = JsonPointer.compile("/0");
        assertTrue(pointer.mayMatchElement());
        
        pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testJsonPointerMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchProperty("foo"));
        
        pointer = JsonPointer.compile("/foo");
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty("foo"));
        
        pointer = JsonPointer.compile("/foo/bar");
        assertNull(pointer.matchProperty("foo"));
    }
    
    @Test
    public void testJsonPointerMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchElement(0));
        
        pointer = JsonPointer.compile("/0");
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(0));
        
        pointer = JsonPointer.compile("/0/1");
        assertNull(pointer.matchElement(0));
    }
    
    @Test
    public void testJsonPointerTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        
        pointer = JsonPointer.compile("/foo");
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        
        pointer = JsonPointer.compile("/foo/bar");
        assertEquals(JsonPointer.compile("/bar"), pointer.tail());
    }
    
    @Test
    public void testJsonPointerToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        
        pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        
        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
    }
    
    @Test
    public void testJsonPointerHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = JsonPointer.compile("/foo/bar");
        pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testJsonPointerEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = JsonPointer.compile("/foo/bar");
        pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));
        
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals("foo"));
    }
}