package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.internal.Normalizer.lowerCase;

public class AttributesTest {
    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get(null);
        fail("Expected IllegalStateException but got: " + result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        String result = attributes.getIgnoreCase("key");
        assertEquals("VALUE", result);
    }

    @Test
    public void testGetIgnoreCaseNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        String result = attributes.getIgnoreCase(null);
        assertEquals("", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();

        attributes.put(null, "value");
        fail("Expected IllegalArgumentException but got: " + attributes.get(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        fail("Expected IllegalArgumentException but got: " + attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");

        String result = attributes.getIgnoreCase("key");
        assertEquals("VALUE", result);
    }

    @Test
    public void testPutIgnoreCaseSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        String result1 = attributes.getIgnoreCase("key");
        assertEquals("value2", result1);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);

        String result = attributes.get("booleanKey");
        assertEquals("", result);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("BooleanKey", true);

        String result = attributes.getIgnoreCase("booleanKey");
        assertEquals("", result);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttributeValueFalse() {
        Attributes attributes = new Attributes();
        attributes.put("BooleanKey", true);
        attributes.put("BooleanKey", false);

        String result = attributes.getIgnoreCase("booleanKey");
        assertEquals("", result);
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttributeValueTrue() {
        Attributes attributes = new Attributes();
        attributes.put("BooleanKey", true);
        attributes.put("BooleanKey", false);
        attributes.put("BooleanKey", true);

        String result = attributes.getIgnoreCase("booleanKey");
        assertEquals("", result);
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutWithSameKeyAndSameValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "value");

        int size = attributes.size();
        assertEquals(1, size);
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testPutWithDifferentKeys() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int size = attributes.size();
        assertEquals(2, size);
        String result1 = attributes.get("key1");
        assertEquals("value1", result1);
        String result2 = attributes.get("key2");
        assertEquals("value2", result2);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        int sizeBeforeRemove = attributes.size();
        attributes.remove("key");

        int sizeAfterRemove = attributes.size();
        assertEquals(sizeBeforeRemove - 1, sizeAfterRemove);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        int sizeBeforeRemove = attributes.size();
        attributes.removeIgnoreCase("key");

        int sizeAfterRemove = attributes.size();
        assertEquals(sizeBeforeRemove - 1, sizeAfterRemove);
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testRemoveNonexistentKey() {
        Attributes attributes = new Attributes();

        int sizeBeforeRemove = attributes.size();
        attributes.remove("nonexistentKey");

        int sizeAfterRemove = attributes.size();
        assertEquals(sizeBeforeRemove, sizeAfterRemove);
        assertFalse(attributes.hasKey("nonexistentKey"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean hasKeyResult = attributes.hasKey("key");
        assertTrue(hasKeyResult);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        boolean hasKeyIgnoreCaseResult = attributes.hasKeyIgnoreCase("key");
        assertTrue(hasKeyIgnoreCaseResult);
    }

    @Test
    public void testHasKeyIgnoreCaseFalse() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        boolean hasKeyIgnoreCaseResult = attributes.hasKeyIgnoreCase("otherKey");
        assertFalse(hasKeyIgnoreCaseResult);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();

        int sizeEmptyAttributes = attributes.size();
        assertEquals(0, sizeEmptyAttributes);

        attributes.put("key", "value");
        int sizeAttributesWithOneAttribute = attributes.size();
        assertEquals(1, sizeAttributesWithOneAttribute);

        attributes.put("anotherKey", "anotherValue");
        int sizeAttributesWithTwoAttributes = attributes.size();
        assertEquals(2, sizeAttributesWithTwoAttributes);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributesResult = new Attributes();
        attributesResult.addAll(attributes1);
        attributesResult.addAll(attributes2);

        int sizeAttributesResult = attributesResult.size();
        assertEquals(4, sizeAttributesResult);

        String result1 = attributesResult.get("key1");
        assertEquals("value1", result1);

        String result2 = attributesResult.get("key2");
        assertEquals("value2", result2);

        String result3 = attributesResult.get("key3");
        assertEquals("value3", result3);

        String result4 = attributesResult.get("key4");
        assertEquals("value4", result4);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String result = attributes.toString();
        String expected = "Attributes{size=2}";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        int hashcodeAttributes1 = attributes1.hashCode();
        int hashcodeAttributes2 = attributes2.hashCode();
        int hashcodeAttributes3 = attributes3.hashCode();

        assertEquals(hashcodeAttributes1, hashcodeAttributes2);
        assertNotEquals(hashcodeAttributes1, hashcodeAttributes3);
    }

    @Test
    public void testClone() {
        Attributes attributesOriginal = new Attributes();
        attributesOriginal.put("key1", "value1");
        attributesOriginal.put("key2", "value2");

        Attributes attributesCloned = attributesOriginal.clone();

        int sizeOriginal = attributesOriginal.size();
        int sizeCloned = attributesCloned.size();

        assertEquals(sizeOriginal, sizeCloned);

        String result1Original = attributesOriginal.get("key1");
        String result1Cloned = attributesCloned.get("key1");
        assertEquals(result1Original, result1Cloned);

        String result2Original = attributesOriginal.get("key2");
        String result2Cloned = attributesCloned.get("key2");
        assertEquals(result2Original, result2Cloned);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");

        attributes.normalize();

        String result = attributes.get("key");
        assertEquals("value", result);
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attributes.dataset();

        int sizeDataset = dataset.size();
        assertEquals(2, sizeDataset);

        String result1 = dataset.get("dataKey1");
        assertEquals("dataValue1", result1);

        String result2 = dataset.get("dataKey2");
        assertEquals("dataValue2", result2);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);

        String result = accum.toString();
        String expected = "Attributes{size=2}";
        assertEquals(expected, result);
    }

    
}