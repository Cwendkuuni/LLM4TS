import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("segment", null);
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("segment", null);
        assertEquals("segment", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("segment", null);
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("1", null);
        assertEquals(1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("segment", null);
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("1", null);
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("segment", null);
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("1", null);
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty(""));
        
        pointer = new JsonPointer("segment", null);
        assertNull(pointer.matchProperty(""));
        assertNotNull(pointer.matchProperty("segment"));
        
        pointer = new JsonPointer("1", null);
        assertNull(pointer.matchProperty("1"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(-1));
        
        pointer = new JsonPointer("segment", null);
        assertNull(pointer.matchElement(-1));
        assertNotNull(pointer.matchElement(0));
        
        pointer = new JsonPointer("1", null);
        assertNull(pointer.matchElement(0));
        assertNotNull(pointer.matchElement(1));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
        
        pointer = new JsonPointer("segment", null);
        assertNull(pointer.tail());
        
        pointer = new JsonPointer("1", new JsonPointer());
        assertNotNull(pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
        
        pointer = new JsonPointer("segment", null);
        assertEquals("segment", pointer.toString());
        
        pointer = new JsonPointer("1", null);
        assertEquals("1", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = new JsonPointer("segment", null);
        pointer2 = new JsonPointer("segment", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = new JsonPointer("1", null);
        pointer2 = new JsonPointer("1", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("segment", null);
        pointer2 = new JsonPointer("segment", null);
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("1", null);
        pointer2 = new JsonPointer("1", null);
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        
        pointer = JsonPointer.compile("/segment");
        assertFalse(pointer.matches());
        
        pointer = JsonPointer.compile("/1");
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        assertTrue(pointer.matches());
        
        pointer = JsonPointer.valueOf("/segment");
        assertFalse(pointer.matches());
        
        pointer = JsonPointer.valueOf("/1");
        assertFalse(pointer.matches());
    }
}