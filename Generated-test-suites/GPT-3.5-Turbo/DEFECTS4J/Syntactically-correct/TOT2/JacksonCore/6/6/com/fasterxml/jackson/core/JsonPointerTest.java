import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
        assertTrue(pointer.toString().equals("/test"));
    }

    @Test
    public void testEmpty() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNotNull(pointer);
        assertTrue(pointer.matches());
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertNotNull(pointer);
        assertTrue(pointer.toString().equals("/test"));
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test");
        JsonPointer result = pointer.matchProperty("test");
        assertNotNull(result);
        assertTrue(result.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
        assertTrue(result.matches());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/test");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertTrue(tail.matches());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.valueOf("/test");
        JsonPointer pointer2 = JsonPointer.valueOf("/test");
        assertEquals(pointer1, pointer2);
    }

    @Test
    public void testInvalidInput() {
        try {
            JsonPointer.compile("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}