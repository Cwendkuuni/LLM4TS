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
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer("/test", "test", null);
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("invalid");
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("/test1", "test1", null);
        JsonPointer jsonPointer2 = new JsonPointer("/test1", "test1", null);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testNotEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("/test1", "test1", null);
        JsonPointer jsonPointer2 = new JsonPointer("/test2", "test2", null);
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }

}