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
        JsonPointer pointer = new JsonPointer("/test");
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/0");
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/0");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        JsonPointer result = pointer.matchProperty("test");
        assertEquals(pointer.tail(), result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0");
        JsonPointer result = pointer.matchElement(0);
        assertEquals(pointer.tail(), result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test");
        assertEquals(pointer.tail(), pointer.tail());
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

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test");
        JsonPointer pointer2 = new JsonPointer("/test");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("test");
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertEquals("/test", pointer.toString());
    }
}