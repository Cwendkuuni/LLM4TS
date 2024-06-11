import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
    }
    
    @Test
    public void testNonEmptyConstructor() {
        String fullString = "/foo/bar";
        String segment = "foo";
        JsonPointer next = new JsonPointer();
        
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        
        assertNotNull(pointer);
        assertEquals(fullString, pointer.toString());
        assertEquals(segment, pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testCompile() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        
        assertNotNull(pointer);
        assertEquals(input, pointer.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        String input = "foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
    }

    @Test
    public void testValueOf() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.valueOf(input);
        
        assertNotNull(pointer);
        assertEquals(input, pointer.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testMatchProperty() {
        String fullString = "/foo/bar";
        String segment = "foo";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        
        JsonPointer matchPointer = pointer.matchProperty(segment);
        assertNotNull(matchPointer);
        assertEquals(fullString, matchPointer.toString());
        assertEquals(segment, matchPointer.getMatchingProperty());
        assertEquals(-1, matchPointer.getMatchingIndex());
        assertFalse(matchPointer.matches());
    }

    @Test
    public void testMatchElement() {
        String fullString = "/foo/bar";
        String segment = "bar";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        
        JsonPointer matchPointer = pointer.matchElement(0);
        assertNotNull(matchPointer);
        assertEquals(fullString, matchPointer.toString());
        assertEquals(-1, matchPointer.getMatchingIndex());
        assertFalse(matchPointer.matches());
    }
    
    @Test
    public void testTail() {
        String fullString = "/foo/bar";
        String segment = "foo";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        
        JsonPointer tailPointer = pointer.tail();
        assertNotNull(tailPointer);
        assertEquals("", tailPointer.toString());
        assertNull(tailPointer.getMatchingProperty());
        assertEquals(-1, tailPointer.getMatchingIndex());
        assertTrue(tailPointer.matches());
    }

    @Test
    public void testHashCode() {
        String fullString = "/foo/bar";
        String segment = "foo";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer1 = new JsonPointer(fullString, segment, next);
        JsonPointer pointer2 = new JsonPointer(fullString, segment, next);
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        String fullString = "/foo/bar";
        String segment = "foo";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer1 = new JsonPointer(fullString, segment, next);
        JsonPointer pointer2 = new JsonPointer(fullString, segment, next);
        
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testParseIndexValid() {
        String str = "1234";
        int index = JsonPointer._parseIndex(str);
        assertEquals(Integer.parseInt(str), index);
    }
    
    @Test
    public void testParseIndexInvalid() {
        String str = "abcd";
        int index = JsonPointer._parseIndex(str);
        assertEquals(-1, index);
    }
    
    @Test
    public void testParseTailFirstCharSegment() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer._parseTail(input);
        
        assertNotNull(pointer);
        assertEquals(input, pointer.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        JsonPointer tailPointer = pointer.tail();
        assertNotNull(tailPointer);
        assertEquals("/bar", tailPointer.toString());
        assertEquals("bar", tailPointer.getMatchingProperty());
        assertEquals(-1, tailPointer.getMatchingIndex());
        assertFalse(tailPointer.matches());
    }
    
    @Test
    public void testParseTailQuoted() {
        String input = "/foo/~0bar";
        JsonPointer pointer = JsonPointer._parseTail(input);
        
        assertNotNull(pointer);
        assertEquals(input, pointer.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        JsonPointer tailPointer = pointer.tail();
        assertNotNull(tailPointer);
        assertEquals("/~0bar", tailPointer.toString());
        assertEquals("~0bar", tailPointer.getMatchingProperty());
        assertEquals(-1, tailPointer.getMatchingIndex());
        assertFalse(tailPointer.matches());
    }

}