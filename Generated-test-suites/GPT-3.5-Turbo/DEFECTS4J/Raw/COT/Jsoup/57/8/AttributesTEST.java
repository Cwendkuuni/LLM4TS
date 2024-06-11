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
        // Test for existing attribute
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Test for non-existing attribute
        assertEquals("", attributes.get("non-existing"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Test for existing attribute
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("KEY"));

        // Test for non-existing attribute
        assertEquals("", attributes.getIgnoreCase("non-existing"));
    }

    @Test
    public void testPut() {
        // Test putting a new attribute
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));

        // Test replacing an existing attribute
        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        // Test putting a boolean attribute with value true
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertEquals("true", attributes.get("key"));

        // Test putting a boolean attribute with value false
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        // Test removing an existing attribute
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));

        // Test removing a non-existing attribute
        attributes.remove("non-existing");
        assertFalse(attributes.hasKey("non-existing"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Test removing an existing attribute
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KEY");
        assertFalse(attributes.hasKey("key"));

        // Test removing a non-existing attribute
        attributes.removeIgnoreCase("non-existing");
        assertFalse(attributes.hasKey("non-existing"));
    }

    @Test
    public void testHasKey() {
        // Test for existing attribute
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        // Test for non-existing attribute
        assertFalse(attributes.hasKey("non-existing"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // Test for existing attribute
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));

        // Test for non-existing attribute
        assertFalse(attributes.hasKeyIgnoreCase("non-existing"));
    }

    @Test
    public void testSize() {
        // Test for empty attributes
        assertEquals(0, attributes.size());

        // Test for attributes with elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        // Test adding all attributes from another Attributes object
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        attributes.addAll(otherAttributes);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        // Test for empty attributes
        assertFalse(attributes.iterator().hasNext());

        // Test for attributes with elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertTrue(attributes.iterator().hasNext());
        assertNotNull(attributes.iterator().next());
    }

    @Test
    public void testAsList() {
        // Test for empty attributes
        assertEquals(0, attributes.asList().size());

        // Test for attributes with elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        // Test for empty attributes
        assertEquals(0, attributes.dataset().size());

        // Test for attributes with elements
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        // Test for empty attributes
        assertEquals("", attributes.html());

        // Test for attributes with elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        // Test for equal attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        assertTrue(attributes.equals(otherAttributes));

        // Test for different attributes
        attributes.put("key3", "value3");
        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        assertEquals(attributes.hashCode(), otherAttributes.hashCode());

        attributes.put("key1", "value1");
        otherAttributes.put("key1", "value1");

        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
        assertNotSame(attributes.asList(), clone.asList());
    }
}

