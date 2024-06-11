import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testCompileWithValueOf() {
        JsonPointer pointer1 = JsonPointer.compile("/segment");
        JsonPointer pointer2 = JsonPointer.valueOf("/segment");
        assertEquals(pointer1, pointer2);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer next = pointer.matchProperty("segment");
        assertNotNull(next);
        assertTrue(next.matches());
    }

    @Test
    public void testNonMatchingProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer next = pointer.matchProperty("name");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "0", null);
        JsonPointer next = pointer.matchElement(0);
        assertNotNull(next);
        assertTrue(next.matches());
    }

    @Test
    public void testNonMatchingElement() {
        JsonPointer pointer = new JsonPointer("fullString", "0", null);
        JsonPointer next = pointer.matchElement(1);
        assertNull(next);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", null);
        assertEquals(pointer1, pointer2);
    }

    @Test
    public void testParseIndexWithValidInput() {
        int index = JsonPointer._parseIndex("123");
        assertEquals(123, index);
    }

    @Test
    public void testParseIndexWithInvalidInput() {
        int index = JsonPointer._parseIndex("abc");
        assertEquals(-1, index);
    }

    @Test
    public void testParseTailWithSlashSegment() {
        JsonPointer pointer = JsonPointer._parseTail("/segment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testParseTailWithQuotedSegment() {
        JsonPointer pointer = JsonPointer._parseTail("~1segment");
        assertFalse(pointer.matches());
        assertEquals("/segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testParseTailWithQuotedSlashSegment() {
        JsonPointer pointer = JsonPointer._parseTail("~0segment");
        assertFalse(pointer.matches());
        assertEquals("~segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testParseTailWithEmptyInput() {
        JsonPointer pointer = JsonPointer._parseTail("");
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
}