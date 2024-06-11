package org.jsoup.nodes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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
        // Test when attributes is null
        assertEquals("", attributes.get("key"));

        // Test when attribute value is set
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Test when attribute value is not set
        assertEquals("", attributes.get("nonexistent_key"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Test when attributes is null
        assertEquals("", attributes.getIgnoreCase("key"));

        // Test when attribute value is set
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));

        // Test when attribute value is not set
        assertEquals("", attributes.getIgnoreCase("nonexistent_key"));
    }

    @Test
    public void testPut() {
        // Test when attribute is correctly put
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        // Test when attribute is replaced
        attributes.put("key", "new_value");
        assertEquals("new_value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        // Test when attribute is correctly put with true value
        attributes.put("boolean_key", true);
        assertTrue(attributes.hasKey("boolean_key"));

        // Test when attribute is removed with false value
        attributes.put("boolean_key", false);
        assertFalse(attributes.hasKey("boolean_key"));
    }

    @Test
    public void testRemove() {
        // Test when attribute is correctly removed
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));

        // Test when attribute is not found
        attributes.remove("nonexistent_key");
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Test when attribute is correctly removed
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        attributes.removeIgnoreCase("KEY");
        assertFalse(attributes.hasKey("key"));

        // Test when attribute is not found
        attributes.removeIgnoreCase("nonexistent_key");
    }

    @Test
    public void testHasKey() {
        // Test when attribute is present
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        // Test when attribute is not present
        assertFalse(attributes.hasKey("nonexistent_key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // Test when attribute is present
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));

        // Test when attribute is not present
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent_key"));
    }

    @Test
    public void testSize() {
        // Test when attributes is empty
        assertEquals(0, attributes.size());

        // Test when attributes has elements
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        // Test adding all attributes from empty incoming
        Attributes incoming = new Attributes();
        attributes.put("existing_key", "existing_value");
        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("existing_key"));

        // Test adding all attributes from non-empty incoming
        incoming.put("new_key", "new_value");
        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("new_key"));
    }

    @Test
    public void testIterator() {
        // Test when attributes is null
        assertFalse(attributes.iterator().hasNext());

        // Test when attributes is empty
        assertFalse(attributes.iterator().hasNext());

        // Test when attributes has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertTrue(attributes.iterator().hasNext());
    }

    @Test
    public void testAsList() {
        // Test when attributes is null
        assertEquals(0, attributes.asList().size());

        // Test when attributes is empty
        assertEquals(0, attributes.asList().size());

        // Test when attributes has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        // Test when attributes is null
        assertEquals(0, attributes.dataset().size());

        // Test when attributes is empty
        attributes.put("key", "value");
        assertEquals(0, attributes.dataset().size());

        // Test when attributes has custom data attributes
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        // Test when attributes is null
        assertEquals("", attributes.html());

        // Test when attributes is empty
        attributes.put("key", "value");
        assertEquals(" key=\"value\"", attributes.html());

        // Test when attributes has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key=\"value\" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        // Test when attributes are equal
        attributes.put("key", "value");
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key", "value");
        assertTrue(attributes.equals(otherAttributes));

        // Test when attributes are not equal
        otherAttributes.put("other_key", "other_value");
        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        // Test when attributes is null
        assertEquals(0, attributes.hashCode());

        // Test when attributes is empty
        assertEquals(0, attributes.hashCode());

        // Test when attributes has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertNotEquals(0, attributes.hashCode());
    }

    @Test
    public void testClone() {
        // Test cloning empty attributes
        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));

        // Test cloning non-empty attributes
        attributes.put("key", "value");
        clone = attributes.clone();
        assertTrue(attributes.equals(clone));
    }
}