import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    private JsonPointer jsonPointer;

    @Before
    public void setUp() {
        jsonPointer = JsonPointer.EMPTY;
    }

    @Test
    public void testEmptyJsonPointer() {
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testNonEmptyJsonPointer() {
        jsonPointer = new JsonPointer("test", "segment", null);
        assertFalse(jsonPointer.matches());
        assertEquals("test/segment", jsonPointer.toString());
    }

    @Test
    public void testMatchingProperty() {
        JsonPointer nextSegment = new JsonPointer("test", "segment", null);
        jsonPointer = new JsonPointer("test", "segment", nextSegment);

        assertTrue(jsonPointer.mayMatchProperty());
        assertEquals("segment", jsonPointer.getMatchingProperty());

        JsonPointer result = jsonPointer.matchProperty("segment");
        assertNotNull(result);
        assertEquals(nextSegment, result);

        result = jsonPointer.matchProperty("other");
        assertNull(result);
    }

    @Test
    public void testMatchingElement() {
        JsonPointer nextSegment = new JsonPointer("test", "segment", null);
        jsonPointer = new JsonPointer("test", "segment", nextSegment);

        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(-1, jsonPointer.getMatchingIndex());

        JsonPointer result = jsonPointer.matchElement(5);
        assertNull(result);

        result = jsonPointer.matchElement(-1);
        assertNull(result);

        result = jsonPointer.matchElement(0);
        assertNotNull(result);
        assertEquals(nextSegment, result);
    }

    @Test
    public void testTail() {
        JsonPointer nextSegment = new JsonPointer("test", "segment", null);
        jsonPointer = new JsonPointer("test", "segment", nextSegment);

        JsonPointer result = jsonPointer.tail();
        assertNotNull(result);
        assertEquals(nextSegment, result);

        jsonPointer = new JsonPointer("test", "segment", null);
        result = jsonPointer.tail();
        assertNull(result);
    }

    @Test
    public void testEqualsAndHashCode() {
        JsonPointer jsonPointer2 = JsonPointer.EMPTY;

        assertTrue(jsonPointer.equals(jsonPointer));
        assertTrue(jsonPointer.equals(jsonPointer2));
        assertFalse(jsonPointer.equals(null));
        assertFalse(jsonPointer.equals("test"));
        assertEquals(jsonPointer.hashCode(), jsonPointer2.hashCode());

        jsonPointer = new JsonPointer("test", "segment", null);
        assertFalse(jsonPointer.equals(jsonPointer2));
        jsonPointer2 = new JsonPointer("test", "segment", null);
        assertTrue(jsonPointer.equals(jsonPointer2));
        assertEquals(jsonPointer.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testInvalidJsonPointer() {
        try {
            jsonPointer = JsonPointer.compile("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"test\"", e.getMessage());
        }

        try {
            jsonPointer = JsonPointer.compile("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"\"", e.getMessage());
        }
    }

    @Test
    public void testValidJsonPointer() {
        jsonPointer = JsonPointer.compile("/");
        assertTrue(jsonPointer.matches());

        jsonPointer = JsonPointer.compile("/test/segment");
        assertFalse(jsonPointer.matches());
        assertEquals("test/segment", jsonPointer.toString());

        jsonPointer = JsonPointer.compile("/test/0");
        assertFalse(jsonPointer.matches());
        assertEquals("test/0", jsonPointer.toString());
    }

}