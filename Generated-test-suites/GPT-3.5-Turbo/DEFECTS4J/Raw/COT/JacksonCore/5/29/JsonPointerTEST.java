import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        boolean result = jsonPointer.matches();
        assertTrue(result);
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        String result = jsonPointer.getMatchingProperty();
        assertEquals("", result);
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        int result = jsonPointer.getMatchingIndex();
        assertEquals(-1, result);
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        boolean result = jsonPointer.mayMatchProperty();
        assertTrue(result);
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        boolean result = jsonPointer.mayMatchElement();
        assertFalse(result);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.matchProperty("");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.matchElement(0);
        assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.tail();
        assertNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer();
        String result = jsonPointer.toString();
        assertEquals("", result);
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        int result = jsonPointer.hashCode();
        assertEquals(0, result);
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer = new JsonPointer();
        boolean result = jsonPointer.equals(jsonPointer);
        assertTrue(result);
    }

    @Test
    public void testCompile() {
        String input = "";
        JsonPointer result = JsonPointer.compile(input);
        assertEquals("", result.toString());
    }

    @Test
    public void testValueOf() {
        String input = "";
        JsonPointer result = JsonPointer.valueOf(input);
        assertEquals("", result.toString());
    }

}