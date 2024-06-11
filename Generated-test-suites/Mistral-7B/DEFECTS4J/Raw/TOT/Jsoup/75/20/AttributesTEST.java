package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        assertEquals("testId", attributes.get("id"));
        assertEquals("", attributes.get("nonExistent"));
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "testId");
        assertEquals("testId", attributes.getIgnoreCase("id"));
        assertEquals("", attributes.getIgnoreCase("NonExistent"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId1");
        attributes.put("id", "testId2");
        assertEquals("testId2", attributes.get("id"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAttributeInvalidKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "testValue");
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-checked", true);
        assertTrue(attributes.hasKey("data-checked"));
        assertFalse(attributes.get("data-checked").isEmpty());
        assertFalse(attributes.get("data-checked").equals("data-checked"));
        assertFalse(attributes.hasKeyIgnoreCase("data-checked"));
    }

    @Test
    public void testPutBooleanAttributeRemove() {
        Attributes attributes = new Attributes();
        attributes.put("data-checked", true);
        assertTrue(attributes.hasKey("data-checked"));
        attributes.put("data-checked", false);
        assertFalse(attributes.hasKey("data-checked"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("data-checked", true);
        assertTrue(attributes.hasKey("data-checked"));
        assertFalse(attributes.get("data-checked").isEmpty());
        assertFalse(attributes.get("data-checked").equals("data-checked"));
        assertTrue(attributes.hasKeyIgnoreCase("data-checked"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttributeRemove() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("data-checked", true);
        assertTrue(attributes.hasKey("data-checked"));
        attributes.putIgnoreCase("data-checked", false);
        assertFalse(attributes.hasKey("data-checked"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttributeSameKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("data-checked", true);
        attributes.putIgnoreCase("data-checked", false);
        assertFalse(attributes.hasKey("data-checked"));
        assertFalse(attributes.hasKey("data-checked"));
    }

    @Test
    public void testPutWithSameKeyAndValue() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId1");
        attributes.put("id", "testId1");
        assertEquals("testId1", attributes.get("id"));
    }

    @Test
    public void testPutWithSameKeyDifferentValue() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId1");
        attributes.put("id", "testId2");
        assertEquals("testId2", attributes.get("id"));
    }

    @Test
    public void testPutWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("id", "");
        assertEquals("", attributes.get("id"));
    }

    @Test
    public void testPutWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("id", null);
        assertEquals("", attributes.get("id"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        assertTrue(attributes.hasKey("id"));
        assertFalse(attributes.hasKey("nonExistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "testId");
        assertTrue(attributes.hasKeyIgnoreCase("id"));
        assertFalse(attributes.hasKeyIgnoreCase("NonExistent"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId1");
        attributes.put("class", "testClass");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("id", "testId1");
        attributes1.put("class", "testClass");
        attributes2.put("data-checked", true);
        attributes2.put("data-value", "testValue");
        attributes1.addAll(attributes2);
        assertTrue(attributes1.size() == 4);
        assertTrue(attributes1.hasKey("id"));
        assertTrue(attributes1.hasKey("class"));
        assertTrue(attributes1.hasKey("data-checked"));
        assertTrue(attributes1.hasKey("data-value"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId1");
        attributes.put("class", "testClass");
        attributes.remove("id");
        assertFalse(attributes.hasKey("id"));
        assertTrue(attributes.size() == 1);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "testId1");
        attributes.put("Class", "testClass");
        attributes.removeIgnoreCase("id");
        assertFalse(attributes.hasKey("Id"));
        assertTrue(attributes.size() == 1);
    }

    @Test
    public void testHasKeyAndGet() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId1");
        assertTrue(attributes.hasKey("id"));
        assertEquals("testId1", attributes.get("id"));
    }

    @Test
    public void testHasKeyAndGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "testId1");
        assertTrue(attributes.hasKeyIgnoreCase("id"));
        assertEquals("testId1", attributes.getIgnoreCase("Id"));
    }

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("nonExistent"));
    }

    @Test
    public void testEmptyAttributesGet() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.get("nonExistent"));
    }

    @Test
    public void testEmptyAttributesPut() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        assertEquals("testId", attributes.get("id"));
        assertEquals(1, attributes.size());
        attributes.clear();
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("id"));
    }

    @Test
    public void testEmptyAttributesPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "testId");
        assertEquals("testId", attributes.getIgnoreCase("Id"));
        assertEquals(1, attributes.size());
        attributes.clear();
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKeyIgnoreCase("Id"));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertEquals(attributes1, attributes1);
        assertNotEquals(attributes1, attributes2);
        attributes2.put("id", "testId");
        attributes1.put("id", "testId");
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
        attributes2.put("id", "testId");
        attributes1.put("id", "testId");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
    }
}