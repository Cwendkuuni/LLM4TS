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
        
        JsonPointer pointerWithProperty = new JsonPointer("/property", "property", JsonPointer.EMPTY);
        assertEquals("property", pointerWithProperty.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());
        
        JsonPointer pointerWithIndex = new JsonPointer("/2", "2", JsonPointer.EMPTY);
        assertEquals(2, pointerWithIndex.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.mayMatchProperty());
        
        JsonPointer pointerWithProperty = new JsonPointer("/property", "property", JsonPointer.EMPTY);
        assertTrue(pointerWithProperty.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());
        
        JsonPointer pointerWithIndex = new JsonPointer("/2", "2", JsonPointer.EMPTY);
        assertTrue(pointerWithIndex.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchProperty("property"));
        
        JsonPointer pointerWithProperty = new JsonPointer("/property", "property", JsonPointer.EMPTY);
        JsonPointer nextSegment = pointerWithProperty.matchProperty("property");
        assertNotNull(nextSegment);
        assertEquals(JsonPointer.EMPTY, nextSegment);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchElement(2));
        
        JsonPointer pointerWithIndex = new JsonPointer("/2", "2", JsonPointer.EMPTY);
        JsonPointer nextSegment = pointerWithIndex.matchElement(2);
        assertNotNull(nextSegment);
        assertEquals(JsonPointer.EMPTY, nextSegment);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        
        JsonPointer pointerWithIndex = new JsonPointer("/2", "2", JsonPointer.EMPTY);
        assertEquals(JsonPointer.EMPTY, pointerWithIndex.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        
        JsonPointer pointerWithProperty = new JsonPointer("/property", "property", JsonPointer.EMPTY);
        assertEquals("/property", pointerWithProperty.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        JsonPointer pointerWithIndex1= new JsonPointer("/2", "2", JsonPointer.EMPTY);
        JsonPointer pointerWithIndex2 = new JsonPointer("/2", "2", JsonPointer.EMPTY);
        assertEquals(pointerWithIndex1.hashCode(), pointerWithIndex2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertTrue(pointer1.equals(pointer2));
        
        JsonPointer pointerWithIndex1 = new JsonPointer("/2", "2", JsonPointer.EMPTY);
        JsonPointer pointerWithIndex2 = new JsonPointer("/2", "2", JsonPointer.EMPTY);
        assertTrue(pointerWithIndex1.equals(pointerWithIndex2));
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/property");
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void test_parseIndex() {
        int index1 = JsonPointer._parseIndex("1");
        assertEquals(1, index1);
        
        int index10 = JsonPointer._parseIndex("10");
        assertEquals(10, index10);
        
        int index1000 = JsonPointer._parseIndex("1000");
        assertEquals(1000, index1000);
        
        int indexNegative = JsonPointer._parseIndex("-1");
        assertEquals(-1, indexNegative);
        
        int indexInvalid = JsonPointer._parseIndex("invalid");
        assertEquals(-1, indexInvalid);
    }

    @Test
    public void test_parseTail() {
        JsonPointer pointer = JsonPointer._parseTail("/property");
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void test_parseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/~0/~1", 1);
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("~", pointer.getMatchingProperty());
        assertEquals(JsonPointer._parseTail("/~1"), pointer.tail());
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
        assertEquals("~~", sb.toString());
    }
}