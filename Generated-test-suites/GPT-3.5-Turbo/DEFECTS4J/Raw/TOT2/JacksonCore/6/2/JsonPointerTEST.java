import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }
    
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("/test");

        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("wrong"));
        assertNull(pointer.matchElement(0));
        assertNotNull(pointer.tail());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test");

        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("wrong"));
        assertNull(pointer.matchElement(0));
        assertNotNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test");
        JsonPointer pointer2 = new JsonPointer("/test");

        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testNotEquals() {
        JsonPointer pointer1 = new JsonPointer("/test1");
        JsonPointer pointer2 = new JsonPointer("/test2");

        assertFalse(pointer1.equals(pointer2));
    }

}