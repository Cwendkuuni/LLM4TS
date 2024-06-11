import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Edge case - empty key
        assertEquals("", attributes.get(""));
    }

    @Test
    public void testGetIgnoreCase() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("keY"));

        // Edge case - attribute not found
        assertEquals("", attributes.getIgnoreCase("notfound"));
    }

    @Test
    public void testPut() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        // Error scenario - null attribute
        try {
            attributes.put(null, "value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRemove() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));

        // Edge case - attribute not found
        attributes.remove("notfound");
        assertFalse(attributes.hasKey("notfound"));
    }

    // Additional test methods for other public methods in the Attributes class

}