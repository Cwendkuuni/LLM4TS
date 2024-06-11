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
    public void testConstructSimplePointer() {
        JsonPointer pointer = new JsonPointer("/property");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/property", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = new JsonPointer("invalid");
    }

    @Test
    public void testConstructComplexPointer() {
        JsonPointer pointer = new JsonPointer("/array/0/property");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/array/0/property", pointer.toString());
    }

    // Add more test cases here for other methods, edge cases, and error scenarios
}