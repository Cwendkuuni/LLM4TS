import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("test", "segment", null);
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        assertEquals("property", pointer.getMatchingProperty());
        
        pointer = new JsonPointer();
        assertNull(pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("test", "", 3);
        assertEquals(3, pointer.getMatchingIndex());
        
        pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer();
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "", 3);
        assertTrue(pointer.mayMatchElement());
        
        pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        JsonPointer next = new JsonPointer();
        
        assertEquals(next, pointer.matchProperty("property"));
        
        assertNull(pointer.matchProperty("other"));
        assertNull(next.matchProperty("property"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "", 3);
        JsonPointer next = new JsonPointer();
        
        assertEquals(next, pointer.matchElement(3));
        
        assertNull(pointer.matchElement(2));
        assertNull(next.matchElement(3));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        JsonPointer next = new JsonPointer();
        
        assertEquals(next, pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        assertEquals("test", pointer.toString());
        
        pointer = new JsonPointer();
        assertEquals("", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        
        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer1));
        
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(new String()));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test/property/1");
        assertEquals("test", pointer.toString());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test/property/1");
        assertEquals("test", pointer.toString());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
    }
    
}