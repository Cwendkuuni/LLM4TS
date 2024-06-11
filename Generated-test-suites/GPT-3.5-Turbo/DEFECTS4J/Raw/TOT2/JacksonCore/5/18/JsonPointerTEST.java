import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());

        pointer = new JsonPointer("/test");
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

        pointer = new JsonPointer("/5");
        assertFalse(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/5");
        assertTrue(pointer.mayMatchElement());

        pointer = new JsonPointer("/test");
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        JsonPointer next = pointer.matchProperty("test");
        assertNotNull(next);

        pointer = new JsonPointer("/test");
        next = pointer.matchProperty("other");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/5");
        JsonPointer next = pointer.matchElement(5);
        assertNotNull(next);

        pointer = new JsonPointer("/5");
        next = pointer.matchElement(10);
        assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test/child");
        JsonPointer tail = pointer.tail();
        assertEquals(new JsonPointer("/child"), tail);
    }

}