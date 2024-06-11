import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("invalid");
    }
    
    @Test
    public void testMatchingProperty() {
        JsonPointer pointer = new JsonPointer("path/to/property", "property", null);
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("invalid"));
        assertNull(pointer.matchElement(0));
        assertEquals("/property", pointer.toString());
    }
    
    @Test
    public void testMatchingIndex() {
        JsonPointer pointer = new JsonPointer("path/to/0", "0", null);
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("property"));
        assertNull(pointer.matchElement(1));
        assertEquals("/0", pointer.toString());
    }
    
    @Test
    public void testMatchesProperty() {
        JsonPointer pointer = new JsonPointer("path/to/property", "property", null);
        JsonPointer next = pointer.matchProperty("property");
        assertTrue(next.matches());
        assertEquals("", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertNull(next.matchProperty("invalid"));
        assertNull(next.matchElement(0));
        assertEquals("", next.toString());
    }
    
    @Test
    public void testMatchesElement() {
        JsonPointer pointer = new JsonPointer("path/to/0", "0", null);
        JsonPointer next = pointer.matchElement(0);
        assertTrue(next.matches());
        assertEquals("", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertNull(next.matchProperty("property"));
        assertNull(next.matchElement(1));
        assertEquals("", next.toString());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("path/to/property", "property", null);
        assertEquals("/property", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("path/to/property", "property", null);
        JsonPointer pointer2 = new JsonPointer("path/to/property", "property", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("path/to/property", "property", null);
        JsonPointer pointer2 = new JsonPointer("path/to/property", "property", null);
        JsonPointer pointer3 = new JsonPointer("path/to/property", "", null);
        
        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals("path/to/property"));
    }
    
    @Test
    public void testParseIndexValid() {
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(1, JsonPointer._parseIndex("1"));
        assertEquals(10, JsonPointer._parseIndex("10"));
        assertEquals(12345678, JsonPointer._parseIndex("12345678"));
    }
    
    @Test
    public void testParseIndexInvalid() {
        assertEquals(-1, JsonPointer._parseIndex(""));
        assertEquals(-1, JsonPointer._parseIndex("abc"));
        assertEquals(-1, JsonPointer._parseIndex("123a"));
        assertEquals(-1, JsonPointer._parseIndex("1234567890"));
    }
    
    @Test
    public void testParseTail() {
        JsonPointer pointer1 = JsonPointer._parseTail("/path/to/property");
        assertFalse(pointer1.matches());
        assertEquals("property", pointer1.getMatchingProperty());
        assertNull(pointer1.matchProperty("invalid"));
        assertNull(pointer1.matchElement(0));
        assertEquals("/property", pointer1.toString());
        
        JsonPointer pointer2 = JsonPointer._parseTail("/path/to/0");
        assertFalse(pointer2.matches());
        assertEquals("", pointer2.getMatchingProperty());
        assertEquals(0, pointer2.getMatchingIndex());
        assertNull(pointer2.matchProperty("property"));
        assertNull(pointer2.matchElement(1));
        assertEquals("/0", pointer2.toString());
    }
    
    @Test
    public void testAppendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '~');
        assertEquals("~0", sb.toString());
        
        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '/');
        assertEquals("~1", sb.toString());
    }
}