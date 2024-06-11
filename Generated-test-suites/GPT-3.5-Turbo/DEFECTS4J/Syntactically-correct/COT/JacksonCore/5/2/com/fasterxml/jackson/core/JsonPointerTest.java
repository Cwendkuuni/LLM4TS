import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    private JsonPointer jsonPointer;

    @Before
    public void setUp() {
        jsonPointer = new JsonPointer();
    }

    @Test
    public void testMatches() {
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        assertNull(jsonPointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        assertNull(jsonPointer.matchElement(0));
    }

    @Test
    public void testTail() {
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testToString() {
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, jsonPointer.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(jsonPointer.equals(jsonPointer));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(jsonPointer.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(jsonPointer.equals("test"));
    }

    @Test
    public void testEqualsDifferentInstance() {
        JsonPointer other = new JsonPointer();
        assertFalse(jsonPointer.equals(other));
    }

    @Test
    public void testEquals() {
        JsonPointer other = new JsonPointer();
        assertTrue(jsonPointer.equals(other));
    }
}