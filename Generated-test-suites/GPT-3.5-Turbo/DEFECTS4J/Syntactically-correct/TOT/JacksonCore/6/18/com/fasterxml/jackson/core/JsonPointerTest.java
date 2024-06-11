import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    private JsonPointer jsonPointer;
    
    @Before
    public void setUp() {
        jsonPointer = new JsonPointer();
    }
    
    @Test
    public void testMatches() {
        assertTrue(jsonPointer.matches());
    }
    
    @Test
    public void testEmptyConstructor() {
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("", jsonPointer.toString());
    }
    
    @Test
    public void testCompile_ValidPointer() {
        String input = "/segment";
        jsonPointer = JsonPointer.compile(input);
        assertEquals(input, jsonPointer.toString());
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompile_InvalidPointer() {
        String input = "segment";
        jsonPointer = JsonPointer.compile(input);
    }
    
    @Test
    public void testValueOf() {
        String input = "/segment";
        jsonPointer = JsonPointer.valueOf(input);
        assertEquals(input, jsonPointer.toString());
    }
    
    @Test
    public void testMatchProperty_Matching() {
        jsonPointer = new JsonPointer("/segment", "segment", null);
        JsonPointer result = jsonPointer.matchProperty("segment");
        assertNull(result);
    }
    
    @Test
    public void testMatchProperty_NotMatching() {
        jsonPointer = new JsonPointer("/segment", "segment", null);
        JsonPointer result = jsonPointer.matchProperty("abc");
        assertNull(result);
    }
    
    @Test
    public void testMatchElement_Matching() {
        jsonPointer = new JsonPointer("/1", "", null);
        JsonPointer result = jsonPointer.matchElement(1);
        assertNull(result);
    }
    
    @Test
    public void testMatchElement_NotMatching() {
        jsonPointer = new JsonPointer("/1", "", null);
        JsonPointer result = jsonPointer.matchElement(2);
        assertNull(result);
    }
    
    @Test
    public void testTail() {
        jsonPointer = new JsonPointer("/segment", "segment", null);
        JsonPointer result = jsonPointer.tail();
        assertNull(result);
    }
    
    @Test
    public void testToString() {
        jsonPointer = new JsonPointer("/segment", "segment", null);
        assertEquals("/segment", jsonPointer.toString());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, jsonPointer.hashCode());
    }
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(jsonPointer.equals(jsonPointer));
    }
    
    @Test
    public void testEquals_NullObject() {
        assertFalse(jsonPointer.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        assertFalse(jsonPointer.equals("String"));
    }
    
    @Test
    public void testEquals_EqualJsonPointer() {
        JsonPointer jsonPointer2 = new JsonPointer("/segment", "segment", null);
        assertTrue(jsonPointer.equals(jsonPointer2));
    }
    
    @Test
    public void testEquals_NotEqualJsonPointer() {
        JsonPointer jsonPointer2 = new JsonPointer("/segment", "abc", null);
        assertFalse(jsonPointer.equals(jsonPointer2));
    }
    
    @Test
    public void test_parseIndex_ValidIndex() {
        String str = "1";
        int result = jsonPointer._parseIndex(str);
        assertEquals(1, result);
    }
    
    @Test
    public void test_parseIndex_ZeroIndex() {
        String str = "0";
        int result = jsonPointer._parseIndex(str);
        assertEquals(0, result);
    }
    
    @Test
    public void test_parseIndex_InvalidIndex() {
        String str = "abc";
        int result = jsonPointer._parseIndex(str);
        assertEquals(-1, result);
    }

    @Test
    public void test_parseTail_SegmentWithSlash() {
        String input = "/segment";
        jsonPointer = jsonPointer._parseTail(input);
        assertEquals(input, jsonPointer.toString());
    }
    
    @Test
    public void test_parseTail_QuotedSegment() {
        String input = "/~0~1";
        jsonPointer = jsonPointer._parseTail(input);
        assertEquals(input, jsonPointer.toString());
    }
    
    @Test
    public void test_parseTail_InvalidSegment() {
        String input = "/~0~";
        jsonPointer = jsonPointer._parseTail(input);
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void test_parseQuotedTail_WithSlash() {
        String input = "/~1";
        jsonPointer = jsonPointer._parseQuotedTail(input, 1);
        assertEquals(input, jsonPointer.toString());
    }
    
    @Test
    public void test_parseQuotedTail_QuotedSegment() {
        String input = "/~0~1";
        jsonPointer = jsonPointer._parseQuotedTail(input, 1);
        assertEquals(input, jsonPointer.toString());
    }
    
    @Test
    public void test_parseQuotedTail_InvalidSegment() {
        String input = "/~0~";
        jsonPointer = jsonPointer._parseQuotedTail(input, 1);
        assertEquals(input, jsonPointer.toString());
    }
    
    private static void _appendEscape() {
        StringBuilder sb = new StringBuilder();
        char c = '0';
        jsonPointer._appendEscape(sb, c);
        assertEquals("~0", sb.toString());
    }
}