import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
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
        assertEquals("value", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertEquals("", attributes.getIgnoreCase("KEY"));
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
        attributes.removeIgnoreCase("KEY");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertFalse(attributes.hasKeyIgnoreCase("Nonexistent"));
    }

    @Test
    public void testEquals() {
        Attributes other = new Attributes();
        attributes.put("key", "value");
        other.put("key", "value");
        assertTrue(attributes.equals(other));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");
        attributes.put("key3", "value3");

        attributes.addAll(other);
        assertEquals(3, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testHtml() {
        attributes.put("key", "value");
        String expected = " key=\"value\"";
        assertEquals(expected, attributes.html());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
    }

    @Test
    public void testNormalize() {
        attributes.put("Key", "value");
        attributes.put("KEY", "value");
        attributes.normalize();
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

}