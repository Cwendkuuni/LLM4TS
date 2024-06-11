import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
        assertTrue(jsonPointer.equals(jsonPointer));
        assertTrue(jsonPointer.equals(new JsonPointer()));
    }

    @Test
    public void testConstructorSegmentAndNext() {
        JsonPointer jsonPointer = new JsonPointer("a/b/c", "a", new JsonPointer("b/c", "b", new JsonPointer("c", "c", null)));
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchElement(0));
        assertNotNull(jsonPointer.tail());
        assertEquals("b/c", jsonPointer.tail().toString());
        assertEquals("a/b/c", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(new JsonPointer()));
    }

    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/a/b/c");
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchElement(0));
        assertNotNull(jsonPointer.tail());
        assertEquals("b/c", jsonPointer.tail().toString());
        assertEquals("/a/b/c", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(new JsonPointer()));
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/a/b/c");
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchElement(0));
        assertNotNull(jsonPointer.tail());
        assertEquals("b/c", jsonPointer.tail().toString());
        assertEquals("/a/b/c", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(new JsonPointer()));
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/a/b/c");
        JsonPointer jsonPointer2 = jsonPointer1.matchProperty("a");
        assertFalse(jsonPointer2.matches());
        assertEquals("b", jsonPointer2.getMatchingProperty());
        assertEquals(-1, jsonPointer2.getMatchingIndex());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertFalse(jsonPointer2.mayMatchElement());
        assertNull(jsonPointer2.matchProperty("foo"));
        assertNull(jsonPointer2.matchElement(0));
        assertNotNull(jsonPointer2.tail());
        assertEquals("b/c", jsonPointer2.tail().toString());
        assertEquals("/a/b/c", jsonPointer2.toString());
        assertEquals(jsonPointer2.hashCode(), jsonPointer2.hashCode());
        assertTrue(jsonPointer2.equals(jsonPointer2));
        assertFalse(jsonPointer2.equals(new JsonPointer()));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/a/b/c");
        JsonPointer jsonPointer2 = jsonPointer1.matchElement(0);
        assertFalse(jsonPointer2.matches());
        assertEquals("a", jsonPointer2.getMatchingProperty());
        assertEquals(0, jsonPointer2.getMatchingIndex());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertTrue(jsonPointer2.mayMatchElement());
        assertNull(jsonPointer2.matchProperty("foo"));
        assertNull(jsonPointer2.matchElement(1));
        assertNotNull(jsonPointer2.tail());
        assertEquals("b/c", jsonPointer2.tail().toString());
        assertEquals("/a/b/c", jsonPointer2.toString());
        assertEquals(jsonPointer2.hashCode(), jsonPointer2.hashCode());
        assertTrue(jsonPointer2.equals(jsonPointer2));
        assertFalse(jsonPointer2.equals(new JsonPointer()));
    }

    @Test
    public void testParseQuotedTail() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/~0/~1/~2");
        JsonPointer jsonPointer2 = jsonPointer1.matchProperty("~0");
        assertFalse(jsonPointer2.matches());
        assertEquals("~1", jsonPointer2.getMatchingProperty());
        assertEquals(-1, jsonPointer2.getMatchingIndex());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertFalse(jsonPointer2.mayMatchElement());
        assertNull(jsonPointer2.matchProperty("foo"));
        assertNull(jsonPointer2.matchElement(0));
        assertNotNull(jsonPointer2.tail());
        assertEquals("~1/~2", jsonPointer2.tail().toString());
        assertEquals("/~0/~1/~2", jsonPointer2.toString());
        assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
        assertTrue(jsonPointer1.equals(jsonPointer1));
        assertFalse(jsonPointer1.equals(new JsonPointer()));
    }
}