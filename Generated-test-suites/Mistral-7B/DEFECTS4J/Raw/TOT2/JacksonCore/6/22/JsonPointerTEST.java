import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {
    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testConstructorWithEmptyStringAndNullNextSegment() {
        JsonPointer jsonPointer = new JsonPointer("", "", null);
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testConstructorWithFullStringAndNullNextSegment() {
        JsonPointer jsonPointer = new JsonPointer("/a/b/c", "/c", null);
        assertFalse(jsonPointer.matches());
        assertEquals("/c", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/a/b/c", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidInput() {
        JsonPointer jsonPointer = new JsonPointer("invalid", null);
    }

    // Add more test cases here for other methods and edge cases
}