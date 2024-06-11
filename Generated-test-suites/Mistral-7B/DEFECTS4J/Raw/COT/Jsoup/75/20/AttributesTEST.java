package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.internal.Normalizer.lowerCase;

public class AttributesTest {
    

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertNull(attributes.get("key"));
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testAddAndGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAndGetIgnoreCaseAttributes() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        Assert.assertEquals("value", attributes.get("Key"));
        Assert.assertTrue(attributes.hasKey("Key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertNull(attributes.get("key"));
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testPutBooleanAttributeRemoveExistingAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", true);
        Assert.assertNull(attributes.get("key"));
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testPutIgnoreCaseBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", true);
        Assert.assertNull(attributes.get("Key"));
        Assert.assertFalse(attributes.hasKey("Key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAndGetAttributesWithNullValues() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        Assert.assertNull(attributes.get("key"));
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testGetAndHasKeyIgnoreCaseWithNullValues() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", null);
        Assert.assertNull(attributes.get("Key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertNull(attributes.get("key"));
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        attributes.removeIgnoreCase("Key");
        Assert.assertNull(attributes.get("Key"));
        Assert.assertFalse(attributes.hasKey("Key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Key"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAttributeWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testRemoveAttributeWithNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("key");
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("other"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("other"));
        attributes.putIgnoreCase("other", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
        attributes.put("key3", "value3");
        Assert.assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.put("key3", "value3");
        attributes3.put("key4", "value4");

        attributes1.addAll(attributes2);
        Assert.assertEquals(3, attributes1.size());
        Assert.assertFalse(attributes1.hasKey("key1")); // should not override keys
        Assert.assertFalse(attributes1.hasKey("key2"));
        Assert.assertTrue(attributes1.hasKey("key3"));
        Assert.assertTrue(attributes1.hasKey("key4"));

        attributes1.addAll(attributes3);
        Assert.assertEquals(5, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("key1")); // should override keys
        Assert.assertTrue(attributes1.hasKey("key2"));
        Assert.assertTrue(attributes1.hasKey("key3"));
        Assert.assertTrue(attributes1.hasKey("key4"));
        Assert.assertTrue(attributes1.hasKey("key5")); // new key
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
            new AbstractMap.SimpleEntry<>("key1", "value1"),
            new AbstractMap.SimpleEntry<>("key2", "value2"),
            new AbstractMap.SimpleEntry<>("key3", "value3")
        );

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        List<Attribute> expectedList = Arrays.asList(
            new Attribute("key1", "value1"),
            new Attribute("key2", "value2"),
            new Attribute("key3", "value3")
        );

        List<Attribute> actualList = attributes.asList();

        assertThat(actualList, is(sameInstance(expectedList)));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("dataKey1", "dataValue1");
        expectedMap.put("dataKey2", "dataValue2");

        Map<String, String> actualMap = attributes.dataset();

        assertThat(actualMap, is(sameInstance(expectedMap)));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "Value");

        attributes.normalize();

        Assert.assertEquals("key", attributes.keys[0]);
        Assert.assertEquals("Value", attributes.vals[0]);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertTrue(clonedAttributes.hasKey("key1"));
        Assert.assertTrue(clonedAttributes.hasKey("key2"));
        Assert.assertFalse(attributes.equals(clonedAttributes));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    
}