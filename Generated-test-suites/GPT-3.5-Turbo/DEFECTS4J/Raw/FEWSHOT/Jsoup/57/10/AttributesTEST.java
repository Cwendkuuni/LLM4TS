package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

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
    public void testPutBooleanTrue() {
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertEquals("true", attributes.get("key"));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
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
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("kEy");
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("invalidKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertFalse(attributes.hasKeyIgnoreCase("invalidKey"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        attributes.addAll(otherAttributes);

        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));

        assertTrue(attributes.hasKey("key2"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributeList = attributes.asList();

        assertEquals(2, attributeList.size());

        assertEquals("key1", attributeList.get(0).getKey());
        assertEquals("value1", attributeList.get(0).getValue());

        assertEquals("key2", attributeList.get(1).getKey());
        assertEquals("value2", attributeList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertNotNull(dataset);
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        assertNotNull(html);
        assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertNotNull(clonedAttributes);
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
        assertEquals(attributes.get("key2"), clonedAttributes.get("key2"));
        assertFalse(attributes == clonedAttributes);
    }

    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes sameAttributes = attributes.clone();
        assertTrue(attributes.equals(sameAttributes));

        Attributes anotherAttributes = new Attributes();
        assertFalse(attributes.equals(anotherAttributes));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int hashCode = attributes.hashCode();

        assertNotEquals(0, hashCode);
    }
}