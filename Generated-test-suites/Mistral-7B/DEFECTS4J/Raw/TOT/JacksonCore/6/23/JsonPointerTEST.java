import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException, but no exception was thrown");
    }

    @Test
    public void testPointerWithIndex() {
        String input = "/test/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIndex() {
        String input = "/test/-1";
        JsonPointer pointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException, but no exception was thrown");
    }

    @Test
    public void testPointerWithPropertyAndIndex() {
        String input = "/test/property/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerWithQuote() {
        String input = "/test/property/~test";
        JsonPointer pointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException, but no exception was thrown");
    }

    @Test
    public void testPointerWithQuote() {
        String input = "/test/property/~0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String input = "/test/property1/property2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testPointerWithTail() {
        String input = "/test/property1/property2/";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNotNull(pointer.tail());
        assertEquals("/property2", pointer.tail().toString());
        assertEquals(input, pointer.toString());
    }
}