import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithProperty() {
        JsonPointer jsonPointer = new JsonPointer("/propertyName", "propertyName", JsonPointer.EMPTY);
        assertTrue(jsonPointer.matches());
        assertEquals("propertyName", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/propertyName", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithElement() {
        JsonPointer jsonPointer = new JsonPointer("/propertyName/0", "propertyName", JsonPointer.EMPTY);
        assertTrue(jsonPointer.matches());
        assertEquals("propertyName", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/propertyName/0", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithTail() {
        JsonPointer jsonPointer = new JsonPointer("/propertyName/0/element", "propertyName", new JsonPointer("/element", "element", JsonPointer.EMPTY));
        assertTrue(jsonPointer.matches());
        assertEquals("propertyName", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNotNull(jsonPointer.tail());
        assertEquals("/element", jsonPointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("invalidPointer", "", JsonPointer.EMPTY);
    }
}