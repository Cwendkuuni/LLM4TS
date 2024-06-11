package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testPutAndGet() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key2", "value2");
        attributes.put(attribute);
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put("KEY3", "value3");
        assertEquals("value3", attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testRemove() {
        attributes.put("key4", "value4");
        attributes.remove("key4");
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "dataValue");
        assertEquals("dataValue", attributes.dataset().get("key"));
    }

}