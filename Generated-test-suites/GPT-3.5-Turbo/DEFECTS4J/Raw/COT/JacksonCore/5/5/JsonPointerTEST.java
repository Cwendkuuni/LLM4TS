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
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(0, pointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testCompile() {
        String input = "/test";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testValueOf() {
        String input = "/test";
        JsonPointer pointer = JsonPointer.valueOf(input);
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void test_parseIndex() {
        String str = "10";
        int index = JsonPointer._parseIndex(str);
        assertEquals(10, index);
    }

    @Test
    public void test_parseTail() {
        String input = "/test";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void test_parseQuotedTail() {
        String input = "/~0~1";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 2);
        assertEquals("/~/", pointer.toString());
    }
}