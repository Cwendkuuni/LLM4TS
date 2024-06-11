
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        attributes.put("key2", "value2");

        assertEquals("value", attributes.get("key"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        attributes.put("KEY2", "value2");

        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
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
        assertEquals("key", attributes.get("key"));

        attributes.put("key", false);

        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attr = new Attribute("key", "value");
        attributes.put(attr);

        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");

        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        attributes.put("KEY2", "value2");

        attributes.removeIgnoreCase("key");

        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));

        attributes.removeIgnoreCase("key2");

        assertFalse(attributes.hasKey("key2"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key", "value");
        attributes.put("KEY2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testSize() {
        attributes.put("key", "value");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());

        attributes.remove("key");

        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key", "value");
        attributes2.put("key2", "value2");

        attributes.addAll(attributes2);

        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key", "value");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();

        assertTrue(iterator.hasNext());
        Attribute attr1 = iterator.next();
        assertTrue(attr1.getKey().equals("key") || attr1.getKey().equals("key2"));

        assertTrue(iterator.hasNext());
        Attribute attr2 = iterator.next();
        assertFalse(attr2.getKey().equals(attr1.getKey()));

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        attributes.put("key", "value");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();

        assertEquals(2, list.size());
        assertTrue(list.contains(new Attribute("key", "value")));
        assertTrue(list.contains(new Attribute("key2", "value2")));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "value");
        attributes.put("data-key2", "value2");
        attributes.put("not-data-key", "value3");
        attributes.put("data-key3", "value4");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("key"));
        assertTrue(dataset.containsKey("key2"));
        assertFalse(dataset.containsKey("not-data-key"));
        assertTrue(dataset.containsKey("key3"));

        assertEquals("value", dataset.get("key"));
        assertEquals("value2", dataset.get("key2"));
        assertEquals("value4", dataset.get("key3"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        assertTrue(html.contains("key1=\"value1\""));
        assertTrue(html.contains("key2=\"value2\""));
    }

    @Test
    public void testIteratorRemove() {
        attributes.put("key", "value");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        iterator.next();
        iterator.remove();

        assertFalse(attributes.hasKey("key"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();

        attributes.put("key1", "value1");
        attributes2.put("key1", "value1");

        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();

        attributes.put("key1", "value1");
        attributes2.put("key1", "value1");

        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes.size(), clone.size());
        assertEquals(attributes.get("key1"), clone.get("key1"));
        assertEquals(attributes.get("key2"), clone.get("key2"));

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
        assertTrue(clone.hasKey("key1"));
    }

}
