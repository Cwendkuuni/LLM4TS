import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertEquals("", empty.getMatchingProperty());
        assertEquals(-1, empty.getMatchingIndex());
        assertNull(empty.tail());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertTrue(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("testproperty");
    }

    @Test
    public void testMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        JsonPointer match = new JsonPointer("/test/anotherProperty");
        JsonPointer matchProperty = pointer.matchProperty("property");
        assertEquals(match, matchProperty);
        assertNull(pointer.matchProperty("anotherProperty"));
    }

    @Test
    public void testMatchingElement() {
        Object[] array = new Object[3];
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer match = new JsonPointer("/test/array/0");
        JsonPointer matchElement = pointer.matchElement(0);
        assertEquals(match, matchElement);
        assertNull(pointer.matchElement(1));
        assertEquals(pointer, pointer.tail());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointer() {
        JsonPointer pointer = new JsonPointer();
        pointer.tail();
    }

    @Test
    public void testQuotedPointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property~1/anotherProperty");
        JsonPointer expected = new JsonPointer("/test/property/anotherProperty");
        assertEquals(expected, pointer);
    }
}