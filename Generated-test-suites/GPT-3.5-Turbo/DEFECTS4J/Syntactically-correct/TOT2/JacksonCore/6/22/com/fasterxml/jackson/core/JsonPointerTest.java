import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testConstructorEmpty() {
        JsonPointer jp = new JsonPointer();
        assertEquals("", jp.toString());
        assertEquals(JsonPointer.EMPTY, jp);
        assertEquals(-1, jp.getMatchingIndex());
        assertFalse(jp.mayMatchElement());
        assertFalse(jp.mayMatchProperty());
    }

    @Test
    public void testCompileValid() {
        JsonPointer jp = JsonPointer.compile("/test/sub");
        assertEquals("/test/sub", jp.toString());
        assertEquals("sub", jp.getMatchingProperty());
        assertEquals(-1, jp.getMatchingIndex());
        assertTrue(jp.mayMatchProperty());
        assertFalse(jp.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalid() {
        JsonPointer.compile("test/sub");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jp = JsonPointer.compile("/test");
        assertNull(jp.matchProperty("sub"));
        assertEquals(JsonPointer.EMPTY, jp.matchProperty("test"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jp = JsonPointer.compile("/0");
        assertNull(jp.matchElement(1));
        assertEquals(JsonPointer.EMPTY, jp.matchElement(0));
    }

    // Add more test cases for other methods as needed

}