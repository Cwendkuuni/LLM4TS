import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("/segment1/segment2/segment3");
        assertFalse(pointer.matches());
        assertEquals("segment1", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("invalidPointer");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/segment1/segment2");
        JsonPointer matchedPointer = pointer.matchProperty("segment1");
        assertNotNull(matchedPointer);
        assertEquals("segment2", matchedPointer.getMatchingProperty());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0/1");
        JsonPointer matchedPointer = pointer.matchElement(0);
        assertNotNull(matchedPointer);
        assertEquals(1, matchedPointer.getMatchingIndex());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/segment1/segment2");
        JsonPointer tailPointer = pointer.tail();
        assertNotNull(tailPointer);
        assertEquals("segment2", tailPointer.getMatchingProperty());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/segment1");
        JsonPointer pointer2 = JsonPointer.compile("/segment1");
        assertEquals(pointer1, pointer2);
    }

}