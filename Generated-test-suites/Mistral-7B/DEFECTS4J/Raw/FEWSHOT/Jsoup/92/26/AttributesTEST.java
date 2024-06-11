package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value1_updated");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonexistentkey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value1_updated");
        assertEquals("value1_updated", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.putIgnoreCase("key1", "value1_updated");
        assertEquals("value1_updated", attributes.get("key1"));

        attributes.putIgnoreCase("Key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertFalse(attributes.get("key1").isEmpty());
        assertFalse(attributes.get("key1").equals(""));
        assertNull(attributes.get("key1_nonexistent"));

        attributes.put("key1", false);
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));

        attributes.remove("nonexistentkey");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));

        attributes.removeIgnoreCase("KEY1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("nonexistentkey"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(4, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
        assertTrue(attributes3.hasKey("key3"));
        assertTrue(attributes3.hasKey("key4"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertTrue(dataset.containsKey("data-key2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2");
        attributes.put("style", "color: red; background-color: blue;");

        StringBuilder sb = StringUtil.borrowBuilder();
        attributes.html(sb, new Document.OutputSettings());

        assertEquals("<class=\"class1 class2\">", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = attributes1.clone();
        assertEquals(2, attributes2.size());
        assertTrue(attributes2.hasKey("key1"));
        assertTrue(attributes2.hasKey("key2"));
        assertEquals("value1", attributes2.get("key1"));
        assertEquals("value2", attributes2.get("key2"));
    }
}