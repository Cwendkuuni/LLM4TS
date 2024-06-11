import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches_EmptyPointer_Success() {
        JsonPointer pointer = JsonPointer.compile("");
        boolean result = pointer.matches();
        assertTrue(result);
    }
    
    @Test
    public void testGetMatchingProperty_ValidProperty_Success() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        String result = pointer.getMatchingProperty();
        assertEquals("foo", result);
    }
    
    @Test
    public void testGetMatchingIndex_ValidIndex_Success() {
        JsonPointer pointer = JsonPointer.compile("/0");
        int result = pointer.getMatchingIndex();
        assertEquals(0, result);
    }
    
    @Test
    public void testMayMatchProperty_ValidProperty_Success() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        boolean result = pointer.mayMatchProperty();
        assertTrue(result);
    }
    
    @Test
    public void testMayMatchElement_ValidIndex_Success() {
        JsonPointer pointer = JsonPointer.compile("/0");
        boolean result = pointer.mayMatchElement();
        assertTrue(result);
    }
    
    @Test
    public void testMatchProperty_ValidProperty_Success() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer result = pointer.matchProperty("foo");
        assertNotNull(result);
    }
    
    @Test
    public void testMatchProperty_InvalidProperty_Failure() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer result = pointer.matchProperty("bar");
        assertNull(result);
    }
    
    @Test
    public void testMatchElement_ValidIndex_Success() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
    }
    
    @Test
    public void testMatchElement_InvalidIndex_Failure() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer result = pointer.matchElement(1);
        assertNull(result);
    }
    
    @Test
    public void testTail_NonEmptyPointer_Success() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer result = pointer.tail();
        assertNotNull(result);
    }
    
    @Test
    public void testTail_EmptyPointer_Success() {
        JsonPointer pointer = JsonPointer.compile("");
        JsonPointer result = pointer.tail();
        assertNull(result);
    }
    
    @Test
    public void testToString_ValidPointer_Success() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        String result = pointer.toString();
        assertEquals("/foo/bar", result);
    }
    
    // Additional test cases for edge and error scenarios
    
    @Test
    public void testMatches_NonEmptyPointer_Failure() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        boolean result = pointer.matches();
        assertFalse(result);
    }
    
    @Test
    public void testGetMatchingProperty_InvalidProperty_Failure() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        String result = pointer.getMatchingProperty();
        assertNotEquals("bar", result);
    }
    
    @Test
    public void testGetMatchingIndex_InvalidIndex_Failure() {
        JsonPointer pointer = JsonPointer.compile("/0");
        int result = pointer.getMatchingIndex();
        assertNotEquals(1, result);
    }
    
    @Test
    public void testMayMatchProperty_InvalidProperty_Failure() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        boolean result = pointer.mayMatchProperty();
        assertFalse(result);
    }
    
    @Test
    public void testMayMatchElement_InvalidIndex_Failure() {
        JsonPointer pointer = JsonPointer.compile("/0");
        boolean result = pointer.mayMatchElement();
        assertFalse(result);
    }
    
    @Test
    public void testMatchProperty_NullProperty_Failure() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer result = pointer.matchProperty(null);
        assertNull(result);
    }
    
    @Test
    public void testMatchElement_NegativeIndex_Failure() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer result = pointer.matchElement(-1);
        assertNull(result);
    }
    
    @Test
    public void testMatchElement_ZeroIndex_Success() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
    }
    
    @Test
    public void testTail_OneSegment_Success() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer result = pointer.tail();
        assertNull(result);
    }
    
    @Test
    public void testToString_EmptyPointer_Success() {
        JsonPointer pointer = JsonPointer.compile("");
        String result = pointer.toString();
        assertEquals("", result);
    }
    
}