import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSegment() {
        JsonPointer jsonPointer = new JsonPointer("/segment1", null, JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("segment1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("/segment1", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("invalidPointer", null, JsonPointer.EMPTY);
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/segment1/segment2/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("segment1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.compile("/segment2/segment3"), jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithQuotedSegment() {
        String input = "/segment1/~1~/segment2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("segment1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.compile("/segment2"), jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }
}