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
        String input = "/property1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/property1", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointer() {
        String input = "/object1/property1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("/object1", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "invalid";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFirstSegmentPointer() {
        String input = "property/";
        JsonPointer.compile(input);
    }

    @Test
    public void testQuotedSegmentsPointer() {
        String input = "/property1/~0/property2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("/~0/property2", tail.toString());
    }

    @Test
    public void testIndexedSegmentPointer() {
        String input = "/array/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertNull(tail);
        assertEquals("/array", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIndexedSegmentPointer() {
        String input = "/array/-1";
        JsonPointer.compile(input);
    }
}