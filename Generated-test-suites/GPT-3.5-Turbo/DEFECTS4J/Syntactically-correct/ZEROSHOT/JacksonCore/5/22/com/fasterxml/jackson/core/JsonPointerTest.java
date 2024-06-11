import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("test", "property", null);
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("test", null, 5);
        assertEquals(5, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("test", null, 5);
        assertFalse(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("test", null, 5);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        JsonPointer next = pointer.matchProperty("property");
        assertNotNull(next);
        
        next = pointer.matchProperty("other");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("test", null, 5);
        JsonPointer next = pointer.matchElement(5);
        assertNotNull(next);
        
        next = pointer.matchElement(10);
        assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        JsonPointer next = pointer.tail();
        assertNull(next);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        assertEquals("test", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        assertTrue(pointer1.equals(pointer2));
    }

}