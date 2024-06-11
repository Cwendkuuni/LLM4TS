package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize_InitialCapacity() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutGetKeyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutIgnoreCaseGet() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutPut_SameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testCheckCapacity() {
        Attributes attributes = new Attributes();
        for (int i = 0; i < Attributes.InitialCapacity * Attributes.GrowthFactor; i++) {
            attributes.put("key" + i, "value" + i);
        }
        assertEquals(Attributes.InitialCapacity * Attributes.GrowthFactor, attributes.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalStateException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Other"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        assertEquals(2, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        for (Attribute attribute : list) {
            System.out.println(attribute.getKey() + "=" + attribute.getValue());
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertTrue(dataset.containsKey("data-key2"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("key=\"value\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        assertFalse(attributes1.equals(attributes3));

        Attributes attributes4 = new Attributes();
        assertFalse(attributes1.equals(attributes4));

        Attributes attributes5 = new Attributes();
        attributes5.put("key2", "value1");
        assertFalse(attributes1.equals(attributes5));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());

        Attributes attributes4 = new Attributes();
        assertNotEquals(attributes1.hashCode(), attributes4.hashCode());

        Attributes attributes5 = new Attributes();
        attributes5.put("key2", "value1");
        assertNotEquals(attributes1.hashCode(), attributes5.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = attributes1.clone();
        assertEquals(attributes1.size(), attributes2.size());
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.normalize();
        assertEquals("key", attributes.get("key"));
        assertEquals("value", attributes.get("key"));
    }
}