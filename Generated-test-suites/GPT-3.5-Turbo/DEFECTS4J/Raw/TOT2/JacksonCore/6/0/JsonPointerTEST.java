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
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/0", "", null);
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/1", "", null);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        assertNull(pointer.matchProperty("test2"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0", "", null);
        assertNull(pointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer nextPointer = new JsonPointer("/test", "test", null);
        JsonPointer pointer = new JsonPointer("/0", "", nextPointer);
        assertEquals(nextPointer, pointer.tail());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test1", "test1", null);
        JsonPointer pointer2 = new JsonPointer("/test2", "test2", null);
        assertTrue(pointer1.equals(pointer1));
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testCompileSuccess() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileFail() {
        JsonPointer.compile("test");
    }

}