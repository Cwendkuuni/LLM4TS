import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testCompileEmptyPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testCompileInvalidPointer() {
        try {
            JsonPointer.compile("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCompileValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
    }

    @Test
    public void testMatchesForEmptyPointer() {
        assertTrue(JsonPointer.EMPTY.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/3");
        assertEquals(3, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/3");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test");
        JsonPointer next = pointer.matchProperty("test");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/3");
        JsonPointer next = pointer.matchElement(3);
        assertNull(next);
    }

}