import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testConstructorWithSegments() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testValueOf() {
        String input = "/segment";
        JsonPointer pointer = JsonPointer.valueOf(input);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "segment";
        JsonPointer pointer = JsonPointer.valueOf(input);
    }

    @Test
    public void testCompile() {
        String input = "/segment";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertNull(pointer.matchProperty("invalid"));
        assertNotNull(pointer.matchProperty("segment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertNull(pointer.matchElement(0));
        assertNotNull(pointer.matchElement(-1));
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        JsonPointer pointer3 = new JsonPointer("fullString", "segment", pointer2);
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));

        JsonPointer pointer3 = new JsonPointer("fullString", "segment", pointer2);
        assertFalse(pointer2.equals(pointer3));
    }

    @Test
    public void testParseIndexWithValidIndex() {
        String str = "12";
        int index = JsonPointer._parseIndex(str);
        assertEquals(12, index);
    }

    @Test
    public void testParseIndexWithInvalidIndex() {
        String str = "invalid";
        int index = JsonPointer._parseIndex(str);
        assertEquals(-1, index);
    }

    @Test
    public void testParseTailWithNoEscapeCharacter() {
        String input = "/segment";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testParseTailWithEscapeCharacter() {
        String input = "/seg~ment";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertFalse(pointer.matches());
        assertEquals("seg~ment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testParseQuotedTail() {
        String input = "/seg~0~1ent";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 4);
        assertFalse(pointer.matches());
        assertEquals("seg~~/ent", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }
    
}