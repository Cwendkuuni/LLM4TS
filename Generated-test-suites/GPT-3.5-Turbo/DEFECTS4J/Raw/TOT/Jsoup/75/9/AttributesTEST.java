package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testPutAndGet() {
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("selected", true);

        assertTrue(attributes.hasKey("selected"));
    }

    @Test
    public void testPutBooleanAttributeFalse() {
        attributes.put("selected", false);

        assertFalse(attributes.hasKey("selected"));
    }

    @Test
    public void testPutAttributeWithSameKey() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutAttributeWithDifferentCaseKey() {
        attributes.put("Key1", "value1");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "value1");
        attributes.removeIgnoreCase("key1");

        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key1", "value1");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
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

        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.dataset().get("key1"));
        assertFalse(attributes.dataset().containsKey("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "<div>value2</div>");

        String expectedHtml = " key1=\"value1\" key2=\"&lt;div&gt;value2&lt;/div&gt;\"";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        attributes.put("key2", "value2");
        attributes.put("key1", "value1");

        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        attributes.put("key2", "value2");
        attributes.put("key1", "value1");

        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        attributes.normalize();

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
    }

}