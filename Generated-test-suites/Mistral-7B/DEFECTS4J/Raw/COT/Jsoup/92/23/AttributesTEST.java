package org.test;

import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for Attributes class
 */
public class AttributesTest {

    @Test
    public void testAttributesConstructor() {
        Attributes attributes = new Attributes();
        Assert.assertNotNull(attributes);
    }

    @Test
    public void testGetEmptyAttribute() {
        Attributes attributes = new Attributes();
        String emptyAttribute = attributes.get("empty");
        Assert.assertEquals(Attributes.EmptyString, emptyAttribute);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String attributeValue = attributes.get("key");
        Assert.assertEquals("value", attributeValue);
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        String attributeValue = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", attributeValue);
    }

    @Test
    public void testPutDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String attributeValue = attributes.get("key");
        Assert.assertEquals("value2", attributeValue);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        Assert.assertFalse(attributes.get("booleanKey").isEmpty());
        Assert.assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String emptyAttribute = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, emptyAttribute);
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value");
        attributes.removeIgnoreCase("Key");
        String emptyAttribute = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, emptyAttribute);
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean hasKey = attributes.hasKey("key");
        Assert.assertTrue(hasKey);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "value");
        boolean hasKeyIgnoreCase = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(hasKeyIgnoreCase);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int size = attributes.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testAddAllAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        String attributeValue1 = attributes1.get("key1");
        String attributeValue2 = attributes1.get("key2");
        Assert.assertEquals("value1", attributeValue1);
        Assert.assertEquals("value2", attributeValue2);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> entries = attributes.asList();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            Assert.assertThat(key, is(notNullValue()));
            Assert.assertThat(value, is(notNullValue()));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringParameterForPut() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringParameterForPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringParameterForKey() {
        Attributes attributes = new Attributes();
        attributes.get(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringParameterForRemove() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringParameterForRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringParameterForHasKey() {
        Attributes attributes = new Attributes();
        attributes.hasKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringParameterForHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.hasKeyIgnoreCase(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBoundsIndex() {
        Attributes attributes = new Attributes();
        attributes.remove(100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNegativeSize() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(-1);
    }

    @Test
    public void testPutDuplicateIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("Key1", "value2");
        String attributeValue = attributes.get("key1");
        Assert.assertEquals("value2", attributeValue);
    }
}