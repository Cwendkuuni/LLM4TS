package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.getIgnoreCase("TESTkey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        attributes.remove("testKey");
        assertFalse(attributes.hasKey("testKey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", true);
        assertTrue(attributes.hasKey("testKey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue");
        attributes.put(attribute);
        assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "testValue");
        assertEquals("testValue", attributes.dataset().get("test"));
    }
}