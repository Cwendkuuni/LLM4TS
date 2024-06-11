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
        JsonPointer jsonPointer = new JsonPointer("", "test", null);
        assertEquals("test", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("", "", null);
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("", "test", null);
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("", "", -1);
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("", "test", null);
        JsonPointer next = jsonPointer.matchProperty("test");
        assertNotNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("", "", 1);
        JsonPointer next = jsonPointer.matchElement(1);
        assertNotNull(next);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("", "test", null);
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("", "test", null);
        JsonPointer jsonPointer2 = new JsonPointer("", "test", null);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("", "test", null);
        JsonPointer jsonPointer2 = new JsonPointer("", "test", null);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }
}