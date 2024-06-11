import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchProperty("test"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
    }

    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertNotNull(jsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("test");
    }
}