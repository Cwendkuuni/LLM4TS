import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;

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
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(5));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/name");
        assertEquals("/name", pointer.toString());
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/name");
        assertEquals("/name", pointer.toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
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
    }
}