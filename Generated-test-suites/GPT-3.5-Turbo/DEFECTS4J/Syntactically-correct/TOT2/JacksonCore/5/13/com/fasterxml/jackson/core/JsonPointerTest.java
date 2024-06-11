import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/property");
        assertNotNull(jsonPointer);
        assertEquals("test", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testCompileInvalidInput() {
        try {
            JsonPointer jsonPointer = JsonPointer.compile("test/property");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/property");
        JsonPointer nextPointer = jsonPointer.matchProperty("test");
        assertNotNull(nextPointer);
        assertEquals("property", nextPointer.getMatchingProperty());
    }

    @Test
    public void testMatchInvalidProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/property");
        JsonPointer nextPointer = jsonPointer.matchProperty("invalid");
        assertNull(nextPointer);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/property");
        JsonPointer nextPointer = jsonPointer.tail();
        assertNotNull(nextPointer);
        assertEquals("test/property", nextPointer.toString());
    }

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        JsonPointer nextPointer = jsonPointer.matchElement(1);
        assertNotNull(nextPointer);
        assertTrue(nextPointer.matches());
    }

    @Test
    public void testMatchInvalidElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        JsonPointer nextPointer = jsonPointer.matchElement(0);
        assertNull(nextPointer);
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/test");
        assertNotNull(jsonPointer);
        assertEquals("test", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.valueOf("/test");
        JsonPointer jsonPointer2 = JsonPointer.valueOf("/test");
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/test");
        assertEquals("/test", jsonPointer.toString());
    }

}