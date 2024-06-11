package org.jsoup.nodes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("kEY"));
    }

    @Test
    public void testPut() {
        attributes.put("key2", "value2");
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testRemove() {
        attributes.put("key3", "value3");
        attributes.remove("key3");
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("KEY", "value4");
        attributes.removeIgnoreCase("kEy");
        assertFalse(attributes.hasKey("KEY"));
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key5", "value5");
        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("key5"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "dataValue");
        assertEquals("dataValue", attributes.dataset().get("key"));
    }

    @Test
    public void testSize() {
        attributes.put("key6", "value6");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertTrue(clone instanceof Attributes);
    }

    // Additional edge cases and exception handling scenarios can be added here

}