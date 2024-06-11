import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer._nextSegment);
        assertEquals("", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("", jsonPointer._asString);
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertNull(jsonPointer._nextSegment);
        assertEquals("segment", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("test", jsonPointer._asString);
    }

    @Test
    public void testCompileValid() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertNotNull(jsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalid() {
        JsonPointer.compile("test");
    }

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertNull(jsonPointer.matchProperty("invalid"));
        assertNotNull(jsonPointer.matchProperty("segment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertNull(jsonPointer.matchElement(-1));
        assertNotNull(jsonPointer.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertEquals("test".hashCode(), jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("test", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("test", "segment", null);
        assertEquals(jsonPointer1, jsonPointer2);
    }
}