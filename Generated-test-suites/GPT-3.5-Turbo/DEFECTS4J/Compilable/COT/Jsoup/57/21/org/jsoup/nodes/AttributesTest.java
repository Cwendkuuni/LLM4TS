package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testGetIgnoreCase() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertEquals(value, attributes.getIgnoreCase(key.toUpperCase()));
    }

    @Test
    public void testPut() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertTrue(attributes.hasKey(key));
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testPutBoolean() {
        String key = "testKey";
        attributes.put(key, true);
        assertTrue(attributes.hasKey(key));
        assertEquals("", attributes.get(key));
        attributes.put(key, false);
        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testPutAttribute() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        attributes.put(attribute);
        assertTrue(attributes.hasKey(key));
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testRemove() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertTrue(attributes.hasKey(key));
        attributes.remove(key);
        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testRemoveIgnoreCase() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertTrue(attributes.hasKey(key));
        attributes.removeIgnoreCase(key.toUpperCase());
        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testHasKey() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertTrue(attributes.hasKey(key));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertTrue(attributes.hasKeyIgnoreCase(key.toUpperCase()));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        String key = "testKey";
        String value = "testValue";
        otherAttributes.put(key, value);
        attributes.addAll(otherAttributes);
        assertTrue(attributes.hasKey(key));
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testIterator() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        attributes.put(key1, value1);
        attributes.put(key2, value2);
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        attributes.put(key1, value1);
        attributes.put(key2, value2);
        assertEquals(2, attributes.asList().size());
        assertEquals(value1, attributes.asList().get(0).getValue());
        assertEquals(value2, attributes.asList().get(1).getValue());
    }

    @Test
    public void testDataset() {
        String key = "data-testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertTrue(attributes.dataset().containsKey("testKey"));
        assertEquals(value, attributes.dataset().get("testKey"));
    }

    @Test
    public void testHtml() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertEquals(" " + key + "=\"" + value + "\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        assertFalse(attributes.equals(otherAttributes));

        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertFalse(attributes.equals(otherAttributes));

        otherAttributes.put(key, value);
        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        assertEquals(attributes.hashCode(), otherAttributes.hashCode());

        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        assertNotEquals(attributes.hashCode(), otherAttributes.hashCode());

        otherAttributes.put(key, value);
        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        String key = "testKey";
        String value = "testValue";
        attributes.put(key, value);
        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));
        assertFalse(attributes == clone);
        assertTrue(attributes.asList().equals(clone.asList()));
    }

    @Test
    public void testCustomCases() {
        // Edge cases and exception handling scenarios specific to the Attributes class can be tested here
    }
}