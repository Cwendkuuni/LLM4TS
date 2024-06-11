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
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key4", true);
        assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key5", "value5");
        attributes.remove("key5");
        assertFalse(attributes.hasKey("key5"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key6", "value6");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key7", "value7");

        Attributes attributes2 = new Attributes();
        attributes2.put("key8", "value8");

        attributes1.addAll(attributes2);

        assertTrue(attributes1.hasKey("key8"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key9", "value9");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(1, count);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");

        assertEquals("data-value", attributes.dataset().get("key"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key10", "value10");

        Attributes clone = attributes.clone();

        assertEquals(attributes.size(), clone.size());
    }

    @Test
    public void testNormalization() {
        Attributes attributes = new Attributes();
        attributes.put("Key11", "value11");
        attributes.normalize();

        assertTrue(attributes.hasKey("key11"));
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

        assertEquals(attributes.hashCode(), attributes.hashCode());
    }

}