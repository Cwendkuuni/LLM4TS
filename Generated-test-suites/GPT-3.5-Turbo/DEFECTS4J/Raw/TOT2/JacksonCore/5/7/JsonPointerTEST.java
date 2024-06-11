import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer ptr = JsonPointer.EMPTY;
        assertTrue(ptr.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer ptr = new JsonPointer("/test", "test", JsonPointer.EMPTY);
        assertEquals("test", ptr.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer ptr = new JsonPointer("/5", "5", JsonPointer.EMPTY);
        assertEquals(5, ptr.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer ptr1 = new JsonPointer("/test", "test", JsonPointer.EMPTY);
        assertTrue(ptr1.mayMatchProperty());
        
        JsonPointer ptr2 = new JsonPointer("/5", "5", JsonPointer.EMPTY);
        assertFalse(ptr2.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer ptr1 = new JsonPointer("/test", "test", JsonPointer.EMPTY);
        assertFalse(ptr1.mayMatchElement());
        
        JsonPointer ptr2 = new JsonPointer("/5", "5", JsonPointer.EMPTY);
        assertTrue(ptr2.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer ptr = new JsonPointer("/test", "test", JsonPointer.EMPTY);
        JsonPointer nextPtr = ptr.matchProperty("test");
        assertEquals(JsonPointer.EMPTY, nextPtr);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer ptr = new JsonPointer("/5", "5", JsonPointer.EMPTY);
        JsonPointer nextPtr = ptr.matchElement(5);
        assertEquals(JsonPointer.EMPTY, nextPtr);
    }

    // Additional test cases for edge cases and exceptions can be added here
    
}