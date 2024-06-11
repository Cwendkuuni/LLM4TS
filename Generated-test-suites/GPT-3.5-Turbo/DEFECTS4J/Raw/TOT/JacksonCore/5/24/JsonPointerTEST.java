import com.fasterxml.jackson.core.JsonPointer;
import org.junit.*;
import static org.junit.Assert.*;

public class JsonPointerTest {

    // Test cases for constructor
    
    @Test
    public void testConstructorForEmptyString() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.toString());
    }
    
    @Test
    public void testConstructorForNonEmptyString() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertEquals("path", jsonPointer.toString());
    }
    
    // Test cases for compile method
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileMethodForNullInput() {
        JsonPointer jsonPointer = JsonPointer.compile(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileMethodForEmptyInput() {
        JsonPointer jsonPointer = JsonPointer.compile("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileMethodForInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("segment");
    }
    
    @Test
    public void testCompileMethodForValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/path/segment");
        assertEquals("/path/segment", jsonPointer.toString());
    }
    
    // Test cases for valueOf method
    
    @Test
    public void testValueOfMethod() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/path/segment");
        assertEquals("/path/segment", jsonPointer.toString());
    }
    
    // Test cases for matches method
    
    @Test
    public void testMatchesMethodForEmptyPointer() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }
    
    @Test
    public void testMatchesMethodForNonEmptyPointer() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertFalse(jsonPointer.matches());
    }
    
    // Test cases for getMatchingProperty method
    
    @Test
    public void testGetMatchingPropertyMethod() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertEquals("segment", jsonPointer.getMatchingProperty());
    }
    
    // Test cases for getMatchingIndex method
    
    @Test
    public void testGetMatchingIndexMethod() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }
    
    // Test cases for mayMatchProperty method
    
    @Test
    public void testMayMatchPropertyMethodForMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertTrue(jsonPointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchPropertyMethodForNonMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertFalse(jsonPointer.mayMatchProperty());
    }
    
    // Test cases for mayMatchElement method
    
    @Test
    public void testMayMatchElementMethodForMatchingElement() {
        JsonPointer jsonPointer = new JsonPointer("path", "0", null);
        assertTrue(jsonPointer.mayMatchElement());
    }
    
    @Test
    public void testMayMatchElementMethodForNonMatchingElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertFalse(jsonPointer.mayMatchElement());
    }
    
    // Test cases for matchProperty method
    
    @Test
    public void testMatchPropertyMethodForMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        JsonPointer newJsonPointer = jsonPointer.matchProperty("segment");
        assertEquals(null, newJsonPointer);
    }
    
    @Test
    public void testMatchPropertyMethodForNonMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        JsonPointer newJsonPointer = jsonPointer.matchProperty("nonMatchingSegment");
        assertEquals(null, newJsonPointer);
    }
    
    // Test cases for matchElement method
    
    @Test
    public void testMatchElementMethodForMatchingElement() {
        JsonPointer jsonPointer = new JsonPointer("path", "0", null);
        JsonPointer newJsonPointer = jsonPointer.matchElement(0);
        assertEquals(null, newJsonPointer);
    }
    
    @Test
    public void testMatchElementMethodForNonMatchingElement() {
        JsonPointer jsonPointer = new JsonPointer("path", "0", null);
        JsonPointer newJsonPointer = jsonPointer.matchElement(1);
        assertEquals(null, newJsonPointer);
    }
    
    // Test cases for tail method
    
    @Test
    public void testTailMethod() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        JsonPointer newJsonPointer = jsonPointer.tail();
        assertEquals(null, newJsonPointer);
    }
    
    // Test cases for toString method
    
    @Test
    public void testToStringMethod() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertEquals("path", jsonPointer.toString());
    }
    
    // Test cases for hashCode method
    
    @Test
    public void testHashCodeMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("path", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("path", "segment", null);
        assertEquals(jsonPointer2.hashCode(), jsonPointer1.hashCode());
    }
    
    // Test cases for equals method
    
    @Test
    public void testEqualsMethodForNullObject() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertFalse(jsonPointer.equals(null));
    }
    
    @Test
    public void testEqualsMethodForSameObject() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertTrue(jsonPointer.equals(jsonPointer));
    }
    
    @Test
    public void testEqualsMethodForDifferentClassObject() {
        JsonPointer jsonPointer = new JsonPointer("path", "segment", null);
        assertFalse(jsonPointer.equals("path"));
    }
    
    @Test
    public void testEqualsMethodForEqualObjects() {
        JsonPointer jsonPointer1 = new JsonPointer("path", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("path", "segment", null);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }
    
    @Test
    public void testEqualsMethodForNonEqualObjects() {
        JsonPointer jsonPointer1 = new JsonPointer("path", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("path", "nonMatchingSegment", null);
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }
    
    // Test cases for _parseIndex method
    
    @Test
    public void test_parseIndexMethodForEmptyString() {
        int index = JsonPointer._parseIndex("");
        assertEquals(-1, index);
    }
    
    @Test
    public void test_parseIndexMethodForInvalidString() {
        int index = JsonPointer._parseIndex("segment");
        assertEquals(-1, index);
    }
    
    @Test
    public void test_parseIndexMethodForValidString() {
        int index = JsonPointer._parseIndex("0");
        assertEquals(0, index);
    }
    
    // Test cases for _parseTail method
    
    @Test
    public void test_parseTailMethodForEmptyString() {
        JsonPointer jsonPointer = JsonPointer._parseTail("");
        assertEquals("", jsonPointer.toString());
    }
    
    @Test
    public void test_parseTailMethodForStringWithSlash() {
        JsonPointer jsonPointer = JsonPointer._parseTail("/path/segment");
        assertEquals("/path/segment", jsonPointer.toString());
    }
    
    // Test cases for _parseQuotedTail method
    
    @Test
    public void test_parseQuotedTailMethod() {
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail("~", 1);
        assertEquals("~", jsonPointer.toString());
    }
    
    // Test cases for _appendEscape method
    
    @Test
    public void test_appendEscapeMethod() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());
    }

}