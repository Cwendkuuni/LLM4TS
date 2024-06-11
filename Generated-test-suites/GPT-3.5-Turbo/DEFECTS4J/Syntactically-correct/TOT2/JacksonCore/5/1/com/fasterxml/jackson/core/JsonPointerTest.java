import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNotNull(jsonPointer);
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("test"));
        assertNull(jsonPointer.matchElement(0));
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertNotNull(jsonPointer);
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchElement(0));
        assertEquals("/test", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("test");
    }

    @Test
    public void testParseIndex() {
        int index = JsonPointer._parseIndex("123");
        assertEquals(123, index);

        index = JsonPointer._parseIndex("invalid");
        assertEquals(-1, index);
    }

    @Test
    public void testEqualsAndHashCode() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test");
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

}