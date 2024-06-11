import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("/property");
        assertFalse(jsonPointer.matches());
        assertEquals("property", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("/1");
        assertFalse(jsonPointer.matches());
        assertEquals(1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer parentPointer = new JsonPointer("/parent");
        JsonPointer childPointer = new JsonPointer("/parent/child");
        
        assertNull(parentPointer.matchProperty("child"));
        assertEquals(childPointer, parentPointer.matchProperty("parent"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer arrayPointer = new JsonPointer("/2");
        JsonPointer objectPointer = new JsonPointer("/2");
        
        assertEquals(arrayPointer, objectPointer.matchElement(2));
        assertNull(objectPointer.matchElement(3));
    }

    @Test
    public void testTail() {
        JsonPointer parentPointer = new JsonPointer("/parent");
        JsonPointer childPointer = new JsonPointer("/parent/child");
        
        assertEquals(childPointer, parentPointer.tail());
        assertNull(childPointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer.compile("parent"); // Must start with '/'
    }

}