
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertNull(pointer.matchProperty("name"));
        JsonPointer matchingPropertyPointer = pointer.matchProperty("segment");
        assertNotNull(matchingPropertyPointer);
        assertTrue(matchingPropertyPointer.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertNull(pointer.matchElement(10));
        JsonPointer matchingElementPointer = pointer.matchElement(-1);
        assertNotNull(matchingElementPointer);
        assertTrue(matchingElementPointer.matches());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertNotNull(pointer.tail());
        JsonPointer emptyPointer = new JsonPointer();
        assertNull(emptyPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
        JsonPointer nonEmptyPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals("fullString", nonEmptyPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1, pointer2);
        assertNotEquals(pointer1, null);
        assertNotEquals(pointer1, "string");
    }
}

