package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(true, attributes.hasKey("key"));
        assertEquals(false, attributes.hasKey("otherKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(true, attributes.hasKeyIgnoreCase("KEY"));
        assertEquals(false, attributes.hasKeyIgnoreCase("OTHERKEY"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes = new Attributes();
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key", "value");
        attributes.addAll(otherAttributes);
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        for (Attribute attribute : attributes) {
            assertEquals("key", attribute.getKey());
            assertEquals("value", attribute.getValue());
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.asList().get(0).getValue());
        assertEquals(1, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        assertEquals("value", attributes.dataset().get("key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(" key=\"value\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key", "value");
        Attributes attributes2 = new Attributes();
        attributes2.put("key", "value");
        assertEquals(true, attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(962, attributes.hashCode()); // replace with actual hashcode value
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key", "value");
        Attributes attributes2 = attributes1.clone();
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.normalize();
        assertEquals("value", attributes.get("key"));
    }

}