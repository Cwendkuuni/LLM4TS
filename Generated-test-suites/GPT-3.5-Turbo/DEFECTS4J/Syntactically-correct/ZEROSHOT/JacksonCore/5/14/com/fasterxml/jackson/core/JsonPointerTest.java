import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    private JsonPointer jsonPointer;
    
    @Before
    public void setup() {
        jsonPointer = new JsonPointer();
    }
    
    @Test
    public void testEmptyConstructor() {
        assertEquals(null, jsonPointer._nextSegment);
        assertEquals("", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("", jsonPointer._asString);
    }
    
    @Test
    public void testConstructorWithParameters() {
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        assertEquals(jsonPointer, jsonPointer._nextSegment);
        assertEquals("segment", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("test", jsonPointer._asString);
    }
    
    @Test
    public void testCompile() {
        jsonPointer = JsonPointer.compile(null);
        assertEquals(jsonPointer, JsonPointer.EMPTY);
        
        jsonPointer = JsonPointer.compile("");
        assertEquals(jsonPointer, JsonPointer.EMPTY);
        
        jsonPointer = JsonPointer.compile("/test/segment");
        JsonPointer expected = new JsonPointer("test/segment", "test", new JsonPointer("test/segment", "segment", JsonPointer.EMPTY));
        assertEquals(expected, jsonPointer);
    }
    
    @Test
    public void testValueOf() {
        jsonPointer = JsonPointer.valueOf(null);
        assertEquals(jsonPointer, JsonPointer.EMPTY);
        
        jsonPointer = JsonPointer.valueOf("");
        assertEquals(jsonPointer, JsonPointer.EMPTY);
        
        jsonPointer = JsonPointer.valueOf("/test/segment");
        JsonPointer expected = new JsonPointer("test/segment", "test", new JsonPointer("test/segment", "segment", JsonPointer.EMPTY));
        assertEquals(expected, jsonPointer);
    }
    
    @Test
    public void testMatches() {
        boolean result = jsonPointer.matches();
        assertTrue(result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.matches();
        assertFalse(result);
    }
    
    @Test
    public void testGetMatchingProperty() {
        String result = jsonPointer.getMatchingProperty();
        assertEquals("", result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.getMatchingProperty();
        assertEquals("segment", result);
    }
    
    @Test
    public void testGetMatchingIndex() {
        int result = jsonPointer.getMatchingIndex();
        assertEquals(-1, result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.getMatchingIndex();
        assertEquals(-1, result);
    }
    
    @Test
    public void testMayMatchProperty() {
        boolean result = jsonPointer.mayMatchProperty();
        assertTrue(result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.mayMatchProperty();
        assertTrue(result);
    }
    
    @Test
    public void testMayMatchElement() {
        boolean result = jsonPointer.mayMatchElement();
        assertFalse(result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.mayMatchElement();
        assertFalse(result);
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer result = jsonPointer.matchProperty("segment");
        assertNull(result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.matchProperty("segment");
        assertEquals(jsonPointer, result);
        
        result = jsonPointer.matchProperty("test");
        assertNull(result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer result = jsonPointer.matchElement(1);
        assertNull(result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.matchElement(1);
        assertNull(result);
        
        result = jsonPointer.matchElement(-1);
        assertNull(result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.matchElement(-1);
        assertNull(result);
        
        result = jsonPointer.matchElement(0);
        assertEquals(jsonPointer, result);
    }
    
    @Test
    public void testTail() {
        JsonPointer result = jsonPointer.tail();
        assertEquals(jsonPointer, result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.tail();
        assertEquals(jsonPointer._nextSegment, result);
    }
    
    @Test
    public void testToString() {
        String result = jsonPointer.toString();
        assertEquals("", result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.toString();
        assertEquals("test/segment", result);
    }
    
    @Test
    public void testHashCode() {
        int result = jsonPointer.hashCode();
        assertEquals(31, result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.hashCode();
        assertEquals(1392459, result);
    }
    
    @Test
    public void testEquals() {
        boolean result = jsonPointer.equals(null);
        assertFalse(result);
        
        result = jsonPointer.equals(jsonPointer);
        assertTrue(result);
        
        result = jsonPointer.equals("test");
        assertFalse(result);
        
        jsonPointer = new JsonPointer("test", "segment", jsonPointer);
        JsonPointer equalJsonPointer = new JsonPointer("test", "segment", jsonPointer);
        result = jsonPointer.equals(equalJsonPointer);
        assertTrue(result);
        
        JsonPointer unequalJsonPointer = new JsonPointer("test", "other", jsonPointer);
        result = jsonPointer.equals(unequalJsonPointer);
        assertFalse(result);
    }
    
    @Test
    public void test_parseIndex() {
        int result = jsonPointer._parseIndex("");
        assertEquals(-1, result);
        
        result = jsonPointer._parseIndex("test");
        assertEquals(-1, result);
        
        result = jsonPointer._parseIndex("12345678901");
        assertEquals(-1, result);
        
        result = jsonPointer._parseIndex("10");
        assertEquals(10, result);
    }
    
    @Test
    public void test_parseTail() {
        JsonPointer result = jsonPointer._parseTail("");
        assertEquals(jsonPointer, result);
        
        result = jsonPointer._parseTail("/test/segment");
        JsonPointer expected = new JsonPointer("test/segment", "test", new JsonPointer("test/segment", "segment", JsonPointer.EMPTY));
        assertEquals(expected, result);
    }
    
    @Test
    public void test_parseQuotedTail() {
        JsonPointer result = jsonPointer._parseQuotedTail("", 0);
        assertEquals(jsonPointer, result);
        
        result = jsonPointer._parseQuotedTail("~0~1test/segment", 2);
        JsonPointer expected = new JsonPointer("~0~1test/segment", "~0~1", new JsonPointer("~0~1test/segment", "segment", JsonPointer.EMPTY));
        assertEquals(expected, result);
    }
    
    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        jsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());
        
        sb = new StringBuilder();
        jsonPointer._appendEscape(sb, '1');
        assertEquals("~1", sb.toString());
        
        sb = new StringBuilder();
        jsonPointer._appendEscape(sb, '2');
        assertEquals("~2", sb.toString());
    }
}