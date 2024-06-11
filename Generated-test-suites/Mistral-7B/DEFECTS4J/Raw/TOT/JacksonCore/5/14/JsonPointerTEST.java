import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/test", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("test/property");
        fail("Expected IllegalArgumentException: Invalid input: JSON Pointer expression must start with '/': \"test/property\"");
    }

    @Test
    public void testElementPointer() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/test/0", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidElementPointer() {
        JsonPointer pointer = JsonPointer.compile("/test/-1");
        fail("Expected IllegalArgumentException: [Issue#133]: beware of super long indexes; assume we never have arrays over 2 billion entries so ints are fine.");
    }

    @Test
    public void testPropertyMatch() {
        JsonPointer pointer1 = JsonPointer.compile("/test/property1");
        JsonPointer pointer2 = JsonPointer.compile("/test/property2");
        JsonPointer match = pointer1.matchProperty("/property");
        assertEquals(pointer2, match);
    }

    @Test
    public void testPropertyMismatch() {
        JsonPointer pointer1 = JsonPointer.compile("/test/property1");
        JsonPointer pointer2 = JsonPointer.compile("/test/property2");
        JsonPointer match = pointer1.matchProperty("/property3");
        assertNull(match);
    }

    @Test
    public void testElementMatch() {
        JsonPointer pointer1 = JsonPointer.compile("/test/0");
        JsonPointer pointer2 = JsonPointer.compile("/test/1");
        JsonPointer match = pointer1.matchElement(1);
        assertEquals(pointer2, match);
    }

    @Test
    public void testElementMismatch() {
        JsonPointer pointer1 = JsonPointer.compile("/test/0");
        JsonPointer pointer2 = JsonPointer.compile("/test/1");
        JsonPointer match = pointer1.matchElement(2);
        assertNull(match);
    }

    @Test
    public void testEmptyTail() {
        JsonPointer pointer = JsonPointer.compile("/test");
        JsonPointer tail = pointer.tail();
        assertEquals(JsonPointer.EMPTY, tail);
    }

    @Test
    public void testNonEmptyTail() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        JsonPointer tail = pointer.tail();
        assertFalse(tail.matches());
        assertEquals("property", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertNotNull(tail.tail());
        assertEquals("/test/property", pointer.toString());
        assertEquals("/property", tail.toString());
    }

    @Test
    public void testQuotedSegment() {
        JsonPointer pointer = JsonPointer.compile("/test/~1~2~3");
        assertFalse(pointer.matches());
        assertEquals("test/~1~2~3", pointer.toString());
        JsonPointer tail = pointer.tail();
        assertEquals("/~1~2~3", tail.toString());
    }

    @Test
    public void testQuotedElement() {
        JsonPointer pointer = JsonPointer.compile("/test/~0~1");
        assertFalse(pointer.matches());
        assertEquals("/test/~0~1", pointer.toString());
        JsonPointer tail = pointer.tail();
        assertEquals("/~0~1", tail.toString());
        JsonPointer match = tail.matchElement(1);
        assertEquals(JsonPointer.EMPTY, match);
    }
}