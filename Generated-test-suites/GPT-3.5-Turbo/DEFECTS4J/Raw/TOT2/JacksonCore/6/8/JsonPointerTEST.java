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
        JsonPointer jsonPointer = new JsonPointer("test", "name", null);
        assertEquals("name", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("test", "", null, 5);
        assertEquals(5, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("test", "name", null);
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("test", "", null, 3);
        assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("test", "name", null);
        JsonPointer result = jsonPointer.matchProperty("name");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("test", "", null, 3);
        JsonPointer result = jsonPointer.matchElement(5);
        assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("test", "name", new JsonPointer("test/name", "last", null));
        JsonPointer tail = jsonPointer.tail();
        assertEquals("test/name", tail.toString());
    }

    @Test
    public void compileThrowExceptionOnInvalidInput() {
        try {
            JsonPointer.compile("invalid"); // invalid input, should throw an exception
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"invalid\"", e.getMessage());
        }
    }

    @Test
    public void compileReturnsEmptyOnNullInput() {
        JsonPointer jsonPointer = JsonPointer.compile(null);
        assertEquals(JsonPointer.EMPTY, jsonPointer);
    }

    @Test
    public void compileReturnsEmptyOnEmptyInput() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, jsonPointer);
    }

    @Test
    public void compileCreatesValidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/name");
        assertNotNull(jsonPointer);
        assertEquals("/test/name", jsonPointer.toString());
    }

}