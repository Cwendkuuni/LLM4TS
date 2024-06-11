import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testConstructorEmpty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testConstructorNonEmpty() {
        JsonPointer pointer = new JsonPointer("/property/1", "property", new JsonPointer("/1"));
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testCompileEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("property/1");
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/property/1");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/property/1");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/property");
        JsonPointer next = pointer.matchProperty("property");
        assertNotNull(next);
        assertTrue(next.matches());
        assertEquals("", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertNull(next.tail());
    }

    @Test
    public void testMatchPropertyNoMatch() {
        JsonPointer pointer = JsonPointer.compile("/property");
        JsonPointer next = pointer.matchProperty("otherProperty");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/1");
        JsonPointer next = pointer.matchElement(1);
        assertNotNull(next);
        assertTrue(next.matches());
        assertEquals("", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertNull(next.tail());
    }

    @Test
    public void testMatchElementNoMatch() {
        JsonPointer pointer = JsonPointer.compile("/1");
        JsonPointer next = pointer.matchElement(0);
        assertNull(next);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/property/1");
        assertEquals("/property/1", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/property/1");
        JsonPointer pointer2 = JsonPointer.compile("/property/1");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/property/1");
        JsonPointer pointer2 = JsonPointer.compile("/property/1");
        assertTrue(pointer1.equals(pointer2));
    }
}