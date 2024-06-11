import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("kEy");
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testSize() {
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key", "value");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKey("key2"));
    }

    // Add more tests for the remaining methods...

}