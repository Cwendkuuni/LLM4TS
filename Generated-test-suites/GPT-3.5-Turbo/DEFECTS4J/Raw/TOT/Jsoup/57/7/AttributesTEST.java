package org.jsoup.nodes;

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
        // typical use case
        attributes.put("name", "value");
        assertEquals("value", attributes.get("name"));

        // edge case - empty key
        attributes.put("", "empty");
        assertEquals("empty", attributes.get(""));

        // error scenario - attribute not set
        assertEquals("", attributes.get("notSet"));
    }

    @Test
    public void testGetIgnoreCase() {
        // typical use case
        attributes.put("CaseSensitive", "value");
        assertEquals("value", attributes.getIgnoreCase("casesensitive"));

        // edge case - empty key
        attributes.put("", "empty");
        assertEquals("empty", attributes.getIgnoreCase(""));

        // error scenario - attribute not set
        assertEquals("", attributes.getIgnoreCase("notSet"));
    }

    @Test
    public void testPut() {
        // typical use case
        attributes.put("name", "value");
        assertTrue(attributes.hasKey("name"));

        // edge case - empty key
        attributes.put("", "empty");
        assertTrue(attributes.hasKey(""));

        // error scenario - null attribute
        try {
            attributes.put(null, "nullValue");
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testPutBoolean() {
        // typical use case
        attributes.put("boolean", true);
        assertTrue(attributes.hasKey("boolean"));

        // edge case - value is false
        attributes.put("false", false);
        assertFalse(attributes.hasKey("false"));
    }

    @Test
    public void testRemove() {
        // typical use case
        attributes.put("name", "value");
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));

        // edge case - empty key
        attributes.put("", "empty");
        attributes.remove("");
        assertFalse(attributes.hasKey(""));

        // edge case - attribute not set
        attributes.remove("notSet");
        assertFalse(attributes.hasKey("notSet"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // typical use case
        attributes.put("CaseSensitive", "value");
        attributes.removeIgnoreCase("casesensitive");
        assertFalse(attributes.hasKeyIgnoreCase("casesensitive"));

        // edge case - empty key
        attributes.put("", "empty");
        attributes.removeIgnoreCase("");
        assertFalse(attributes.hasKeyIgnoreCase(""));

        // edge case - attribute not set
        attributes.removeIgnoreCase("notSet");
        assertFalse(attributes.hasKeyIgnoreCase("notSet"));
    }

    @Test
    public void testHasKey() {
        // typical use case
        attributes.put("name", "value");
        assertTrue(attributes.hasKey("name"));

        // edge case - empty key
        attributes.put("", "empty");
        assertTrue(attributes.hasKey(""));

        // edge case - attribute not set
        assertFalse(attributes.hasKey("notSet"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // typical use case
        attributes.put("CaseSensitive", "value");
        assertTrue(attributes.hasKeyIgnoreCase("casesensitive"));

        // edge case - empty key
        attributes.put("", "empty");
        assertTrue(attributes.hasKeyIgnoreCase(""));

        // edge case - attribute not set
        assertFalse(attributes.hasKeyIgnoreCase("notSet"));
    }

    @Test
    public void testSize() {
        // typical use case
        attributes.put("name", "value");
        assertEquals(1, attributes.size());

        // edge case - empty key
        attributes.put("", "empty");
        assertEquals(2, attributes.size());

        // edge case - attribute not set
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAll() {
        // typical use case
        Attributes incoming = new Attributes();
        incoming.put("name", "value");
        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("name"));

        // edge case - empty attributes
        Attributes empty = new Attributes();
        attributes.addAll(empty);
        assertFalse(attributes.hasKey(""));

        // edge case - incoming attributes empty
        incoming = new Attributes();
        attributes.addAll(incoming);
        assertFalse(attributes.hasKey("notSet"));
    }

    @Test
    public void testAsList() {
        // typical use case
        attributes.put("name", "value");
        assertEquals(1, attributes.asList().size());

        // edge case - empty key
        attributes.put("", "empty");
        assertEquals(2, attributes.asList().size());

        // edge case - attribute not set
        assertEquals(0, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        // typical use case
        attributes.put("data-key", "value");
        assertEquals("value", attributes.dataset().get("key"));

        // edge case - empty key
        attributes.put("data-", "empty");
        assertEquals("empty", attributes.dataset().get(""));

        // error scenario - attribute not set
        assertNull(attributes.dataset().get("notSet"));
    }

    @Test
    public void testHtml() {
        // typical use case
        attributes.put("name", "value");
        assertEquals(" name=\"value\"", attributes.html());

        // edge case - empty key
        attributes.put("", "empty");
        assertEquals(" name=\"value\"  empty=\"empty\"", attributes.html());

        // edge case - attribute not set
        assertEquals("", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attrib1 = new Attributes();
        attrib1.put("name1", "value1");
        Attributes attrib2 = new Attributes();
        attrib2.put("name2", "value2");

        // typical use case
        assertFalse(attrib1.equals(attrib2));

        // edge case - same attributes
        attrib2.put("name1", "value1");
        assertFalse(attrib1.equals(attrib2));

        attrib1.put("name2", "value2");
        assertTrue(attrib1.equals(attrib2));

        // edge case - attribute not set
        assertFalse(attributes.equals(attrib1));
    }

    @Test
    public void testHashCode() {
        // typical use case
        attributes.put("name", "value");
        assertEquals(attributes.hashCode(), attributes.hashCode());

        // edge case - empty key
        attributes.put("", "empty");
        assertEquals(attributes.hashCode(), attributes.hashCode());

        // edge case - attribute not set
        assertEquals(0, attributes.clone().hashCode());
    }

    @Test
    public void testClone() {
        // typical use case
        attributes.put("name", "value");
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);

        // edge case - empty key
        attributes.put("", "empty");
        clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);

        // edge case - attribute not set
        clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }
}