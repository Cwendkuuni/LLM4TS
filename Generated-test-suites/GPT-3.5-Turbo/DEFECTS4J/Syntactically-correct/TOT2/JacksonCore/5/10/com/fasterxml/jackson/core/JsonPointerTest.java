import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    // Test for matches() method
    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }

    // Test for getMatchingProperty() method
    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertEquals("segment", jsonPointer.getMatchingProperty());
    }

    // Test for getMatchingIndex() method
    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    // Test for matchProperty() method
    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        JsonPointer result = jsonPointer.matchProperty("segment");
        assertEquals(null, result);
    }

    // Test for matchElement() method
    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        JsonPointer result = jsonPointer.matchElement(10);
        assertEquals(null, result);
    }

    // Test for tail() method
    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        assertEquals(null, jsonPointer.tail());
    }

}