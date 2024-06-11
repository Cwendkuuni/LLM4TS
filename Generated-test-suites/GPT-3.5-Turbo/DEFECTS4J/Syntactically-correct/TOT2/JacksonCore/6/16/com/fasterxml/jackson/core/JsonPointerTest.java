import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jp = new JsonPointer();
        assertTrue(jp.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jp = new JsonPointer();
        assertEquals("", jp.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jp = new JsonPointer();
        assertEquals(-1, jp.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jp = new JsonPointer();
        assertFalse(jp.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jp = new JsonPointer();
        assertFalse(jp.mayMatchElement());
    }

    @Test
    public void testEquals() {
        JsonPointer jp1 = new JsonPointer();
        JsonPointer jp2 = new JsonPointer();
        assertTrue(jp1.equals(jp2));
    }

    @Test
    public void testHashCode() {
        JsonPointer jp = new JsonPointer();
        assertEquals(jp.toString().hashCode(), jp.hashCode());
    }
}