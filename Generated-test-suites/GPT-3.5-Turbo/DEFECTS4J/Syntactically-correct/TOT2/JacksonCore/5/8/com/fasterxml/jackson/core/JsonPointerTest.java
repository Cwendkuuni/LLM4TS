import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertEquals("test", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("/2");
        assertEquals(2, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("/");
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("/");
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("/name");
        JsonPointer next = jsonPointer.matchProperty("name");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("/2");
        JsonPointer next = jsonPointer.matchElement(2);
        assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("/test", "test", new JsonPointer("/next"));
        assertEquals("/next", jsonPointer.tail().toString());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertEquals("/test", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertEquals("/test".hashCode(), jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testCompile_validInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertNotNull(jsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_invalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("test");
    }

    // Add more test cases to cover edge cases and exception scenarios

}