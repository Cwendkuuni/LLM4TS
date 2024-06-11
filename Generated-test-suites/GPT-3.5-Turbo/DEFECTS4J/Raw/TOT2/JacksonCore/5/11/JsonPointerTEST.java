import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test/123");
        assertNotNull(pointer);
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(123, pointer.getMatchingIndex());
        assertFalse(pointer.matches());
    }

    @Test
    public void testEmptyCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNotNull(pointer);
        assertTrue(pointer.matches());
    }

    @Test
    public void testInvalidCompile() {
        try {
            JsonPointer.compile("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer result = pointer.matchProperty("test");
        assertNotNull(result);
        assertTrue(result.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0", "", new JsonPointer());
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
        assertTrue(result.matches());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test/123", "test", new JsonPointer());
        JsonPointer result = pointer.tail();
        assertNotNull(result);
        assertTrue(result.matches());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer pointer2 = new JsonPointer("/test", "test", new JsonPointer());
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testNotEquals() {
        JsonPointer pointer1 = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer pointer2 = new JsonPointer("/test2", "test2", new JsonPointer());
        assertFalse(pointer1.equals(pointer2));
    }
}