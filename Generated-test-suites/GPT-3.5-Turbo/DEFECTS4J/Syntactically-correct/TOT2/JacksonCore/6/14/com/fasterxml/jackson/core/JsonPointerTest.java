import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    // Test cases for constructor JsonPointer()
    @Test
    public void testJsonPointerConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.matches());
    }

    // Test cases for method compile(String input)
    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertNotNull(jsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("foo/bar");
    }

    // Test cases for method matchProperty(String name)
    @Test
    public void testMatchPropertyValid() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer result = jsonPointer.matchProperty("foo");
        assertNotNull(result);
    }

    @Test
    public void testMatchPropertyInvalid() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer result = jsonPointer.matchProperty("baz");
        assertNull(result);
    }

    // Test cases for method matchElement(int index)
    @Test
    public void testMatchElementValid() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
        JsonPointer result = jsonPointer.matchElement(0);
        assertNotNull(result);
    }

    @Test
    public void testMatchElementInvalid() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
        JsonPointer result = jsonPointer.matchElement(1);
        assertNull(result);
    }

    // Test cases for method tail()
    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer result = jsonPointer.tail();
        assertNotNull(result);
    }
}