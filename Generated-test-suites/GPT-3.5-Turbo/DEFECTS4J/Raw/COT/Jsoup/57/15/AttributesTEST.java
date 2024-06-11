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
        // Test case for getting an existing attribute value
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Test case for getting a non-existent attribute value
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Test case for getting an existing attribute value
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KeY"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));

        // Test case for getting a non-existent attribute value
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPut() {
        // Test case for putting a new attribute
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Test case for replacing an existing attribute
        attributes.put("key", "newvalue");
        assertEquals("newvalue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        // Test case for putting a new boolean attribute
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));

        // Test case for removing an existing boolean attribute
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        // Test case for putting a new attribute
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));

        // Test case for replacing an existing attribute
        Attribute newAttribute = new Attribute("key", "newvalue");
        attributes.put(newAttribute);
        assertEquals("newvalue", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        // Test case for removing an existing attribute
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));

        // Test case for removing a non-existent attribute
        attributes.remove("nonexistent");
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Test case for removing an existing attribute
        attributes.put("key", "value");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));

        // Test case for removing a non-existent attribute
        attributes.removeIgnoreCase("nonexistent");
    }

    @Test
    public void testHasKey() {
        // Test case for checking an existing attribute key
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        // Test case for checking a non-existent attribute key
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // Test case for checking an existing attribute key
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("KeY"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));

        // Test case for checking a non-existent attribute key
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        // Test case for getting the size of an attribute set with 2 attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        // Test case for getting the size of an attribute set with no attributes
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAll() {
        // Test case for adding all attributes from another Attributes object
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        attributes.addAll(otherAttributes);

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        // Test case for iterating over attributes in an empty set
        assertFalse(attributes.iterator().hasNext());

        // Test case for iterating over attributes in a non-empty set
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertTrue(attributes.iterator().hasNext());
    }

    @Test
    public void testAsList() {
        // Test case for getting attributes as a list from an empty set
        assertEquals(0, attributes.asList().size());

        // Test case for getting attributes as a list from a non-empty set
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        // Test case for retrieving the dataset with no custom data attributes
        assertEquals(0, attributes.dataset().size());

        // Test case for retrieving the dataset with custom data attributes
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        // Test case for getting the HTML representation of attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testToString() {
        // Test case for getting the string representation of attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        // Test case for comparing attributes with equal content
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        assertTrue(attributes.equals(otherAttributes));

        // Test case for comparing attributes with different content
        Attributes differentAttributes = new Attributes();
        differentAttributes.put("key1", "value1");
        differentAttributes.put("key3", "value3");

        assertFalse(attributes.equals(differentAttributes));
    }

    @Test
    public void testHashCode() {
        // Test case for getting the hashcode of attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.hashCode();
    }

    @Test
    public void testClone() {
        // Test case for cloning attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));
    }
}


// Test cases go here

