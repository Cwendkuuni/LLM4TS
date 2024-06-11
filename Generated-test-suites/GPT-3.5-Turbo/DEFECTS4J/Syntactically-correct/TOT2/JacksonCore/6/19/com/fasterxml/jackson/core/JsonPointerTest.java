import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer("/test", "test", null);
        assertEquals("/test", jsonPointer.toString());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertEquals("/test", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("test");
    }

    @Test
    public void testMatchesMethod() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertEquals("test", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        assertEquals(1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertNull(jsonPointer.matchProperty("wrong"));
        assertNotNull(jsonPointer.matchProperty("test"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        assertNull(jsonPointer.matchElement(0));
        assertNotNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testTailMethod() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertNotNull(jsonPointer.tail());
    }

    @Test
    public void testEqualsMethod() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test");
        assertEquals(jsonPointer1, jsonPointer2);
    }

    @Test
    public void testHashCodeMethod() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertEquals("/test".hashCode(), jsonPointer.hashCode());
    }
}