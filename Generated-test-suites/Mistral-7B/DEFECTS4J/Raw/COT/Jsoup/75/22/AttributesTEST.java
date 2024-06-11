package org.jsoup.nodes; // make sure the test package matches the class package

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.internal.Normalizer.lowerCase;

/**
 * Test class for Attributes.
 */
public class AttributesTest {

    @Test
    public void testAttributesBasicFunctionality() {
        Attributes attributes = new Attributes();

        // Test putting and getting attributes
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));

        // Test putting and getting case-insensitive attributes
        attributes.putIgnoreCase("Key", "value");
        Assert.assertEquals("value", attributes.getIgnoreCase("Key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));

        // Test putting and getting boolean attributes
        attributes.put("booleanKey", true);
        Assert.assertFalse(attributes.get("booleanKey").isEmpty());
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertFalse(attributes.get("booleanKey").equals("false"));

        // Test removing an attribute
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertEquals(EmptyString, attributes.get("key"));

        // Test removing a boolean attribute
        attributes.put("booleanKey", false);
        attributes.remove("booleanKey");
        Assert.assertFalse(attributes.hasKey("booleanKey"));
        Assert.assertEquals(EmptyString, attributes.get("booleanKey"));

        // Test size
        attributes.put("sizeKey", "sizeValue");
        Assert.assertEquals(1, attributes.size());

        // Test hasKey
        Assert.assertTrue(attributes.hasKey("sizeKey"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));

        // Test iterator
        Map<String, String> map = new HashMap<>();
        map.putAll(attributes.asList());
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            attributes.put(entry.getKey(), entry.getValue());
        }
        Assert.assertEquals(map, attributes.asMap());

        // Test dataset
        Attributes datasetAttributes = new Attributes();
        datasetAttributes.put("dataKey", "dataValue");
        Assert.assertEquals(Collections.singletonMap("dataKey", "dataValue"), datasetAttributes.dataset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacity() {
        new Attributes().checkCapacity(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        new Attributes().put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        new Attributes().put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttribute() {
        new Attributes().put(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "anotherValue");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.remove(1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        new Attributes().get(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetIgnoreCaseNullKey() {
        new Attributes().getIgnoreCase(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertFalse(attributes.get(emptyString));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIgnoreCaseIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        Assert.assertFalse(attributes.getIgnoreCase(emptyString));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateIgnoreCaseKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        attributes.putIgnoreCase("key", "anotherValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyIgnoreCase() {
        new Attributes().putIgnoreCase(null, "value");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testHasKeyIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertFalse(attributes.hasKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testHasKeyIgnoreCaseIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        Assert.assertFalse(attributes.hasKeyIgnoreCase(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSizeIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertFalse(attributes.get(attributes.size()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringKey() {
        new Attributes().put(EmptyString, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringValue() {
        new Attributes().put("key", EmptyString);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullAttributes() {
        new Attributes().put((Attributes) null);
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        Map<String, String> map = new HashMap<>();
        map.putAll(attributes1.asList());
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value3");
        expectedMap.put("key3", "value3");

        Assert.assertEquals(expectedMap, attributes1.asMap());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes incoming = new Attributes();
        incoming.put("key", "value");
        attributes.addAll(1, incoming);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expectedToString = "Attributes{" +
                "size=" + attributes.size() +
                ", keys=" + Arrays.toString(attributes.keys) +
                ", vals=" + Arrays.toString(attributes.vals) +
                "}";
        Assert.assertEquals(expectedToString, attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        attributes4.put("key2", "value2");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
        Assert.assertNotEquals(attributes1, attributes4);

        Attributes attributes5 = new Attributes();
        Attributes attributes6 = new Attributes();
        attributes5.putAll(attributes1.asList());
        attributes6.putAll(attributes1.asList());
        Assert.assertEquals(attributes5, attributes6);

        Assert.assertNotEquals(attributes1, null);
        Assert.assertNotEquals(attributes1, new Object());

        Assert.assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        attributes4.put("key2", "value2");

        int attributes1HashCode = attributes1.hashCode();
        int attributes2HashCode = attributes2.hashCode();
        int attributes3HashCode = attributes3.hashCode();
        int attributes4HashCode = attributes4.hashCode();

        Attributes attributes5 = new Attributes();
        Attributes attributes6 = new Attributes();
        attributes5.putAll(attributes1.asList());
        attributes6.putAll(attributes1.asList());

        int attributes5HashCode = attributes5.hashCode();
        int attributes6HashCode = attributes6.hashCode();

        Assert.assertEquals(attributes1HashCode, attributes1.hashCode());
        Assert.assertEquals(attributes1HashCode, attributes2.hashCode());
        Assert.assertEquals(attributes5HashCode, attributes6.hashCode());
        Assert.assertNotEquals(attributes1HashCode, attributes3.hashCode());
        Assert.assertNotEquals(attributes1HashCode, attributes4.hashCode());
        Assert.assertNotEquals(attributes1HashCode, 123);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(attributes, clonedAttributes);
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertArrayEquals(attributes.keys, clonedAttributes.keys);
        Assert.assertArrayEquals(attributes.vals, clonedAttributes.vals);

        attributes.normalize();
        clonedAttributes.normalize();
        Assert.assertArrayEquals(attributes.keys, clonedAttributes.keys);
    }
}