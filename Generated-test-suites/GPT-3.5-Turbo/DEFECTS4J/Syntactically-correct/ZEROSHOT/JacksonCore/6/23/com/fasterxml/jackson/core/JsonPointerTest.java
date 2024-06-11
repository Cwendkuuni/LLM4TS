import static org.junit.Assert.*;
import org.junit.Test;

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
        JsonPointer pointer = new JsonPointer("example", "first", null);
        assertNull(pointer.matchProperty("second"));
        assertEquals(pointer, pointer.matchProperty("first"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("example", "", null);
        assertNull(pointer.matchElement(1));
        assertEquals(pointer, pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("example", "", null);
        assertNull(pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("example", "", null);
        assertEquals("example", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(pointer._asString.hashCode(), pointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer1));
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(new Object()));
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/example");
        assertNotNull(pointer);
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/example");
        assertNotNull(pointer);
    }
    
    @Test
    public void test_parseIndex() {
        int index = JsonPointer._parseIndex("0");
        assertEquals(0, index);
        index = JsonPointer._parseIndex("-1");
        assertEquals(-1, index);
        index = JsonPointer._parseIndex("10");
        assertEquals(10, index);
        index = JsonPointer._parseIndex("1000000000");
        assertEquals(1000000000, index);
    }
    
    @Test
    public void test_parseTail() {
        JsonPointer pointer = JsonPointer._parseTail("/example");
        assertNotNull(pointer);
    }
    
    @Test
    public void test_parseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/~0~1example", 2);
        assertNotNull(pointer);
    }
    
    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());
        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '1');
        assertEquals("~1", sb.toString());
        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '2');
        assertEquals("~2", sb.toString());
    }
}