import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("test", pointer.toString());
        assertEquals("segment", pointer.getMatchingProperty());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertNull(pointer.matchProperty("invalid"));
        assertEquals(null, pointer.matchProperty("segment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertNull(pointer.matchElement(5));
        assertEquals(null, pointer.matchElement(0));
        assertEquals(null, pointer.matchElement(-1));
    }

    @Test
    public void testParseValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
        assertEquals("/test", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidPointer() {
        JsonPointer.compile("test");
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        JsonPointer pointer2 = new JsonPointer("test", "segment", null);
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertEquals("test".hashCode(), pointer.hashCode());
    }

    @Test
    public void testInvalidIndexParsing() {
        assertEquals(-1, JsonPointer._parseIndex("invalid"));
    }

    @Test
    public void testEdgeIndexParsing() {
        assertEquals(-1, JsonPointer._parseIndex("10000000000"));
    }

}