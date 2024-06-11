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
        String property = "test";
        JsonPointer pointer = new JsonPointer("/test", "test", new JsonPointer());
        assertEquals(property, pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        int index = 1;
        JsonPointer pointer = new JsonPointer("/1", "1", new JsonPointer());
        assertEquals(index, pointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer result = pointer.matchProperty("test");
        assertNotNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/1", "1", new JsonPointer());
        JsonPointer result = pointer.matchElement(1);
        assertNotNull(result);
    }

    /* Add more test cases for other methods, edge cases, and exceptions */

}