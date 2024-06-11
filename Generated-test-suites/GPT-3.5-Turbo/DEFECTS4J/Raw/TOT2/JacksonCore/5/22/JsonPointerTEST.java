import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testCompileValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testCompileInvalidPointer() {
        try {
            JsonPointer pointer = JsonPointer.compile("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/inner");
        JsonPointer inner = pointer.matchProperty("test");
        assertNotNull(inner);
        assertEquals("inner", inner.getMatchingProperty());
        assertTrue(inner.matches());
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/array/0");
        JsonPointer next = pointer.matchElement(0);
        assertNotNull(next);
        assertEquals("0", next.getMatchingProperty());
        assertTrue(next.matches());
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/parent/child");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("child", tail.getMatchingProperty());
        assertTrue(tail.matches());
    }
}