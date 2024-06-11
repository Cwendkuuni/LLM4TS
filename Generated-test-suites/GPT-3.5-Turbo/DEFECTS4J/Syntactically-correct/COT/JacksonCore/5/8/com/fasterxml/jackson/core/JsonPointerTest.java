
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
        
        JsonPointer nextSegment = new JsonPointer();
        pointer._nextSegment = nextSegment;
        pointer._matchingPropertyName = "name";
        
        assertEquals(nextSegment, pointer.matchProperty("name"));
        assertNull(pointer.matchProperty("invalid_name"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        
        assertNull(pointer.matchElement(0));
        
        JsonPointer nextSegment = new JsonPointer();
        pointer._nextSegment = nextSegment;
        pointer._matchingElementIndex = 0;
        
        assertEquals(nextSegment, pointer.matchElement(0));
        assertNull(pointer.matchElement(1));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        
        assertNull(pointer.tail());
        
        JsonPointer nextSegment = new JsonPointer();
        pointer._nextSegment = nextSegment;
        
        assertEquals(nextSegment, pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("test");
        assertEquals("test", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("test");
        JsonPointer pointer2 = new JsonPointer("test");
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("test");
        JsonPointer pointer2 = new JsonPointer("test");
        JsonPointer pointer3 = new JsonPointer("different");
        
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals("test"));
        assertFalse(pointer1.equals(pointer3));
    }
}
