import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/property");
        assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/1");
        assertEquals(1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/property");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/1");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/property");
        JsonPointer result = pointer.matchProperty("property");
        assertEquals("/property", result.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/1");
        JsonPointer result = pointer.matchElement(1);
        assertEquals("/1", result.toString());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test/1");
        JsonPointer result = pointer.tail();
        assertEquals("/1", result.toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/test");
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/test1");
        JsonPointer pointer2 = new JsonPointer("/test1");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test1");
        JsonPointer pointer2 = new JsonPointer("/test1");
        assertTrue(pointer1.equals(pointer2));
    }
}