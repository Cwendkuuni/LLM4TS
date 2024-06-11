import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#matches()}.
     */
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#getMatchingProperty()}.
     */
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#getMatchingIndex()}.
     */
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#mayMatchProperty()}.
     */
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#mayMatchElement()}.
     */
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#matchProperty(java.lang.String)}.
     */
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchProperty("name");
        assertNull(result);
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#matchElement(int)}.
     */
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchElement(0);
        assertNull(result);
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#getAsString()}.
     */
    @Test
    public void testGetAsString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getAsString());
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
    }

    /**
     * Test method for {@link com.fasterxml.jackson.core.JsonPointer#hashCode()}.
     */
    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer.hashCode());
    }

}