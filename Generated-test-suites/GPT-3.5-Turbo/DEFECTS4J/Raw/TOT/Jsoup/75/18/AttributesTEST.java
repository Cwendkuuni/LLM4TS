import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("checked", true);
        assertTrue(attributes.hasKey("checked"));

        attributes.put("unchecked", false);
        assertFalse(attributes.hasKey("unchecked"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        assertEquals("value", attributes.get("key"));
        assertTrue(attribute.getParent() == attributes);
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
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incomingAttributes = new Attributes();
        incomingAttributes.put("key1", "value1");
        incomingAttributes.put("key2", "value2");

        attributes.addAll(incomingAttributes);
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
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
        attributes.put("data-key2", "value2");

        assertEquals(2, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        attributes.put("key", "value");
        attributes.put("checked", true);
        attributes.put("data-key", "dataValue");

        assertEquals(" key=\"value\" checked=\"\" data-key=\"dataValue\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes, clonedAttributes);
        assertFalse(attributes == clonedAttributes);
    }

    @Test
    public void testNormalize() {
        attributes.put("Key", "value");
        attributes.put("Key2", "value2");
        attributes.normalize();

        assertFalse(attributes.hasKey("Key"));
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKey("key2"));
    }
}