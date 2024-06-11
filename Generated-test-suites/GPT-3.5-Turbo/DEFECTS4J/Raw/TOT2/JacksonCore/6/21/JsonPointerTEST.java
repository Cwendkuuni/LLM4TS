import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test/segment");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("test/segment");
    }

    @Test
    public void testEqualsAndHashCode() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        JsonPointer pointer2 = new JsonPointer("test", "segment", null);

        assertEquals(pointer1, pointer2);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/test/~01/segment", 6);
        assertFalse(pointer.matches());
        assertEquals("test/~1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testExceptionHandlingForIndexParsing() {
        int index = JsonPointer._parseIndex("abc");
        assertEquals(-1, index);
    }
}