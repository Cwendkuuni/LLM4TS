
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        // Typical use case: attribute exists
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Error scenario: empty attribute key
        assertEquals("", attributes.get(""));

        // Edge case: attribute does not exist
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Typical use case: attribute exists
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("KEY"));

        // Error scenario: empty attribute key
        assertEquals("", attributes.getIgnoreCase(""));

        // Edge case: attribute does not exist
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPut() {
        // Typical use case: set attribute value
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Edge case: set empty attribute value
        attributes.put("key", "");
        assertEquals("", attributes.get("key"));

        // Error scenario: null attribute
        try {
            attributes.put(null, "value");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @Test
    public void testPutBoolean() {
        // Typical use case: set boolean attribute value as true
        attributes.put("key", true);
        assertEquals("true", attributes.get("key"));

        // Typical use case: set boolean attribute value as false
        attributes.put("key", false);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        // Typical use case: set attribute using an Attribute object
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));

        // Error scenario: null attribute
        try {
            attributes.put(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @Test
    public void testRemove() {
        // Typical use case: remove existing attribute
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));

        // Typical use case: remove non-existing attribute
        attributes.remove("nonexistent");
        assertFalse(attributes.hasKey("nonexistent"));

        // Error scenario: empty attribute key
        attributes.put("key", "value");
        attributes.remove("");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Typical use case: remove existing attribute
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KEY");
        assertFalse(attributes.hasKey("key"));

        // Typical use case: remove non-existing attribute
        attributes.removeIgnoreCase("nonexistent");
        assertFalse(attributes.hasKey("nonexistent"));

        // Error scenario: empty attribute key
        attributes.put("key", "value");
        attributes.removeIgnoreCase("");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        // Typical use case: attribute exists
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        // Edge case: attribute does not exist
        assertFalse(attributes.hasKey("nonexistent"));

        // Error scenario: empty attribute key
        assertFalse(attributes.hasKey(""));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // Typical use case: attribute exists
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));

        // Edge case: attribute does not exist
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));

        // Error scenario: empty attribute key
        assertFalse(attributes.hasKeyIgnoreCase(""));
    }

    @Test
    public void testSize() {
        // Typical use case: attributes exist
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        // Edge case: no attributes
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAll() {
        // Typical use case: add all attributes from another Attributes object
        Attributes incoming = new Attributes();
        incoming.put("key", "value");
        incoming.put("key2", "value2");
        attributes.addAll(incoming);
        assertEquals("value", attributes.get("key"));
        assertEquals("value2", attributes.get("key2"));

        // Typical use case: add all attributes from empty Attributes object
        attributes.addAll(new Attributes());
        assertEquals("value", attributes.get("key"));
        assertEquals("value2", attributes.get("key2"));

        // Error scenario: null Attributes object
        try {
            attributes.addAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @Test
    public void testIterator() {
        // Typical use case: iterate over attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(2, count);

        // Edge case: no attributes
        count = 0;
        attributes = new Attributes();
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(0, count);
    }

    @Test
    public void testAsList() {
        // Typical use case: convert attributes to List
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());

        // Edge case: no attributes
        attributes = new Attributes();
        assertEquals(0, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        // Typical use case: retrieve custom data attributes
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.dataset().get("key1"));

        // Edge case: no custom data attributes
        attributes = new Attributes();
        assertEquals(0, attributes.dataset().size());
    }

    @Test
    public void testHtml() {
        // Typical use case: convert attributes to HTML string
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());

        // Typical use case: no attributes
        attributes = new Attributes();
        assertEquals("", attributes.html());
    }

    @Test
    public void testEquals() {
        // Typical use case: attributes are equal
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertTrue(attributes.equals(attributes2));

        // Edge case: attributes are null
        attributes = new Attributes();
        attributes2 = new Attributes();
        assertTrue(attributes.equals(attributes2));

        // Error scenario: attributes are not equal
        attributes2.put("key1", "value2");
        assertFalse(attributes.equals(attributes2));

        // Error scenario: comparing with a different object type
        assertFalse(attributes.equals("not an attributes object"));
    }

    @Test
    public void testHashCode() {
        // Typical use case: same attributes have same hash code
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        // Typical use case: clone attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }
}
