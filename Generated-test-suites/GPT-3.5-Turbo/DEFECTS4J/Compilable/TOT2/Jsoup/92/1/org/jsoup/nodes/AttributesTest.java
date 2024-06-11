package org.jsoup.nodes;
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
    public void testPutAndGet() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("checked", true);
        assertTrue(attributes.hasKey("checked"));
        assertFalse(attributes.hasKey("unchecked"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        attributes.addAll(incoming);
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "data-value");
        assertEquals("data-value", attributes.dataset().get("key"));
    }

    @Test
    public void testHtml() {
        attributes.put("key", "value");
        assertTrue(attributes.html().contains("key=\"value\""));
    }
}