import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches_EmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
    }

    @Test
    public void testMatches_NonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/test");
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/5");
        assertEquals(5, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/5");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty_MatchingProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        JsonPointer result = pointer.matchProperty("test");
        assertNotNull(result);
    }

    @Test
    public void testMatchProperty_NonMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        JsonPointer result = pointer.matchProperty("other");
        assertNull(result);
    }

    @Test
    public void testMatchElement_MatchingIndex() {
        JsonPointer pointer = new JsonPointer("/5");
        JsonPointer result = pointer.matchElement(5);
        assertNotNull(result);
    }

    @Test
    public void testMatchElement_NonMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/5");
        JsonPointer result = pointer.matchElement(10);
        assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test");
        JsonPointer result = pointer.tail();
        assertNull(result);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test");
        JsonPointer pointer2 = new JsonPointer("/test");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/test");
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer("/test");
        assertEquals("/test".hashCode(), pointer.hashCode());
    }
}