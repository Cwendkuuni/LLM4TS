import org.junit.*;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
    }

    @Test
    public void testConstructorWithParameters() {
        String fullString = "/foo/bar";
        String segment = "bar";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        assertNotNull(pointer);
        assertEquals(fullString, pointer._asString);
        assertEquals(segment, pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals(next, pointer._nextSegment);
    }

    @Test
    public void testCompile() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertNotNull(pointer);
        assertEquals(input, pointer._asString);
        assertEquals("foo", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertNull(pointer._nextSegment);
    }

    @Test
    public void testValueOf() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.valueOf(input);
        assertNotNull(pointer);
        assertEquals(input, pointer._asString);
        assertEquals("foo", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertNull(pointer._nextSegment);
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        String segment = "bar";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", segment, next);
        assertEquals(segment, pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        int index = 1;
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", String.valueOf(index), next);
        assertEquals(index, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("", "0", null);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        String segment = "bar";
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", segment, next);
        assertEquals(next, pointer.matchProperty(segment));
    }

    @Test
    public void testMatchElement() {
        int index = 0;
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", String.valueOf(index), next);
        assertEquals(next, pointer.matchElement(index));
    }

    @Test
    public void testTail() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", "bar", next);
        assertEquals(next, pointer.tail());
    }

    @Test
    public void testToString() {
        String fullString = "/foo/bar";
        JsonPointer pointer = new JsonPointer(fullString, "bar", null);
        assertEquals(fullString, pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void test_parseIndex() {
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(1, JsonPointer._parseIndex("1"));
    }

    @Test
    public void test_parseTail() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertNotNull(pointer);
        assertEquals(input, pointer._asString);
        assertEquals("foo", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertNotNull(pointer._nextSegment);
    }

    @Test
    public void test_parseQuotedTail() {
        String input = "/~1foo%2Fbar";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 1);
        assertNotNull(pointer);
        assertEquals(input, pointer._asString);
        assertEquals("/foo/bar", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertNull(pointer._nextSegment);
    }

    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());
    }
}