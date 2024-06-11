package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;
import static org.jsoup.nodes.Attributes.NotFound;

public class AttributesTest {

    @Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(InitialCapacity, attributes.keys.length);
        Assert.assertEquals(InitialCapacity, attributes.vals.length);
        Assert.assertEquals(0, attributes.size);
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testAddAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("booleanKey"));
        Assert.assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testAddAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");

        attributes1.addAll(attributes2);

        Assert.assertEquals(2, attributes1.size());
        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertEquals("value2", attributes1.get("key2"));
        Assert.assertTrue(attributes1.hasKey("key1"));
        Assert.assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        attributes.remove("key1");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        attributes.removeIgnoreCase("key1");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertNull(attributes.get(NotFound));
    }

    @Test
    public void testGetIgnoreCaseOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertNull(attributes.getIgnoreCase(NotFound));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testCloning() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(1, clonedAttributes.size());
        Assert.assertEquals("value1", clonedAttributes.get("key1"));
    }

    @Test
    public void testDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataAttributes = attributes.dataset();
        Assert.assertEquals(1, dataAttributes.size());
        Assert.assertEquals("value2", dataAttributes.get("data-key2"));
    }
}