import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    // Test cases for constructor used for creating "empty" instance
    
    @Test
    public void testEmptyInstanceConstruction() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    // Test cases for constructor used for creating non-empty Segments
    
    @Test
    public void testNonEmptyInstanceConstruction_withMatchingPropertyName() {
        String fullString = "/fullString";
        String segment = "matchingPropertyName";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        assertFalse(pointer.matches());
        assertEquals(segment, pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testNonEmptyInstanceConstruction_withMatchingElementIndex() {
        String fullString = "/fullString";
        String segment = "0";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
    }

    // Test cases for factory method that parses given input and construct matching pointer instance
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompile_withInvalidInput() {
        String input = "invalidInput";
        JsonPointer pointer = JsonPointer.compile(input);
    }
    
    @Test
    public void testCompile_withEmptyInput() {
        String input = "";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testCompile_withValidInput_matchingPropertyName() {
        String input = "/fullString/matchingPropertyName";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("matchingPropertyName", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testCompile_withValidInput_matchingElementIndex() {
        String input = "/fullString/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
    }

    // Test cases for accessor methods
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/fullString/matchingPropertyName", "matchingPropertyName", null);
        assertEquals("matchingPropertyName", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/fullString/0", "0", null);
        assertEquals(0, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/fullString/matchingPropertyName", "matchingPropertyName", null);
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/fullString/0", "0", null);
        assertTrue(pointer.mayMatchElement());
    }
    
    // Test cases for matchProperty method
    
    @Test
    public void testMatchProperty_withMatchingProperty() {
        String name = "matchingPropertyName";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("/fullString/matchingPropertyName", name, next);
        JsonPointer result = pointer.matchProperty(name);
        assertEquals(next, result);
    }
    
    @Test
    public void testMatchProperty_withNonMatchingProperty() {
        String name = "nonMatchingPropertyName";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("/fullString/matchingPropertyName", "matchingPropertyName", next);
        JsonPointer result = pointer.matchProperty(name);
        assertNull(result);
    }
    
    // Test cases for matchElement method
    
    @Test
    public void testMatchElement_withMatchingIndex() {
        int index = 0;
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("/fullString/0", "0", next);
        JsonPointer result = pointer.matchElement(index);
        assertEquals(next, result);
    }
    
    @Test
    public void testMatchElement_withNonMatchingIndex() {
        int index = 1;
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("/fullString/0", "0", next);
        JsonPointer result = pointer.matchElement(index);
        assertNull(result);
    }
    
    // Test cases for tail method
    
    @Test
    public void testTail() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("/fullString/0", "0", next);
        JsonPointer result = pointer.tail();
        assertEquals(next, result);
    }
    
    // Test cases for toString method
    
    @Test
    public void testToString() {
        String fullString = "/fullString/0";
        JsonPointer pointer = new JsonPointer(fullString, "0", null);
        String result = pointer.toString();
        assertEquals(fullString, result);
    }

    // Test cases for equals method
    
    @Test
    public void testEquals_withEqualObjects() {
        JsonPointer pointer1 = new JsonPointer("/fullString/0", "0", null);
        JsonPointer pointer2 = new JsonPointer("/fullString/0", "0", null);
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testEquals_withDifferentObjects() {
        JsonPointer pointer1 = new JsonPointer("/fullString/0", "0", null);
        JsonPointer pointer2 = new JsonPointer("/fullString", "", null);
        assertFalse(pointer1.equals(pointer2));
    }
    
    // Test cases for hashCode method
    
    @Test
    public void testHashCode_withEqualObjects() {
        JsonPointer pointer1 = new JsonPointer("/fullString/0", "0", null);
        JsonPointer pointer2 = new JsonPointer("/fullString/0", "0", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    // Test cases for parseIndex method
    
    @Test
    public void testParseIndex_withValidIndex() {
        String str = "0";
        int result = JsonPointer._parseIndex(str);
        assertEquals(0, result);
    }
    
    @Test
    public void testParseIndex_withInvalidIndex() {
        String str = "invalidIndex";
        int result = JsonPointer._parseIndex(str);
        assertEquals(-1, result);
    }

    // Test cases for private _parseTail method
    
    @Test
    public void testParseTail_withMultipleSegments() {
        String input = "/fullString/matchingPropertyName/0";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertFalse(pointer.matches());
        assertEquals("0", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testParseTail_withQuotedTilde() {
        String input = "/fullString/matchingPropertyName/0~0";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertFalse(pointer.matches());
        assertEquals("0~0", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    // Test cases for private _parseQuotedTail method
    
    @Test
    public void testParseQuotedTail_withMultipleSegments() {
        String input = "/fullString/matchingPropertyName/0";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 1);
        assertFalse(pointer.matches());
        assertEquals("fullString", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testParseQuotedTail_withQuotedTilde() {
        String input = "/fullString/matchingPropertyName/0~0";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 1);
        assertFalse(pointer.matches());
        assertEquals("fullStringmatchingPropertyName0~0", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
}