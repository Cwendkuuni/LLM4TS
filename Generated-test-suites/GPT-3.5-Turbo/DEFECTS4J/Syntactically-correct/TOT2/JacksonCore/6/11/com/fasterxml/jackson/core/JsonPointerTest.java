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
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertEquals("test", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("/1");
        assertEquals(1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("/1");
        assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        JsonPointer next = new JsonPointer();
        assertEquals(next, jsonPointer.matchProperty("test"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("/1");
        JsonPointer next = new JsonPointer();
        assertEquals(next, jsonPointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertEquals(new JsonPointer(), jsonPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertEquals("/test", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("/test1");
        JsonPointer jsonPointer2 = new JsonPointer("/test2");
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("/test1");
        JsonPointer jsonPointer2 = new JsonPointer("/test1");
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }
}