import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchProperty("name"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(0, pointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/path/to/property");
        assertNotNull(pointer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCompile() {
        JsonPointer pointer = JsonPointer.compile("not/starting/with/slash");
        assertNull(pointer);
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/path/to/property");
        assertNotNull(pointer);
    }
    
    @Test
    public void testParseIndex() {
        int index = JsonPointer._parseIndex("10");
        assertEquals(10, index);
    }
    
    @Test
    public void testParseIndexWithLeadingZeroes() {
        int index = JsonPointer._parseIndex("00010");
        assertEquals(-1, index);
    }
    
    @Test
    public void testParseIndexWithInvalidCharacter() {
        int index = JsonPointer._parseIndex("abc");
        assertEquals(-1, index);
    }
    
    @Test
    public void testParseIndexWithLongIndex() {
        int index = JsonPointer._parseIndex("10000000000");
        assertEquals(-1, index);
    }
}