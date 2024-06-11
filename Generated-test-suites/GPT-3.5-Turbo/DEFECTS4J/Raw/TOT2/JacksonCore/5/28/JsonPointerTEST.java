import org.junit.Test;
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
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(0));
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("/test/1");
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("wrong"));
        assertNull(pointer.matchElement(2));
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
        assertTrue(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("test");
    }

    @Test
    public void testQuotedTail() {
        JsonPointer pointer = new JsonPointer("/~01");
        assertNotNull(pointer);
        assertTrue(pointer.matches());
        assertEquals("~1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertTrue(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test");
        JsonPointer pointer2 = new JsonPointer("/test");
        assertEquals(pointer1, pointer2);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer("/test");
        assertEquals("/test".hashCode(), pointer.hashCode());
    }

}