package org.jsoup.nodes;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        assertTrue(attributes.hasKey("key3"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key4", true);
        assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key5", "value5");
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key5"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key6", "value6");
        attributes.remove("key6");
        assertFalse(attributes.hasKey("key6"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key7", "value7");
        attributes.removeIgnoreCase("key7");
        assertFalse(attributes.hasKeyIgnoreCase("key7"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key8", "value8");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key9", "value9");
        Attributes attributes2 = new Attributes();
        attributes2.put("key10", "value10");
        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("key10"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        assertEquals("data-value", attributes.dataset().get("key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key11", "value11");
        assertEquals(" key11=\"value11\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key12", "value12");
        Attributes attributes2 = new Attributes();
        attributes2.put("key12", "value12");
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key13", "value13");
        assertNotNull(attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key14", "value14");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key15", "value15");
        attributes.normalize();
        assertTrue(attributes.hasKey("key15"));
    }
}