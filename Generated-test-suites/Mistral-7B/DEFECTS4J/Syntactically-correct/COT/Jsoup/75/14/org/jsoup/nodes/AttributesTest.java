package org.test; // replace with your test package if necessary

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetEmptyString() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("key");
        Assert.assertEquals(Attributes.EMPTY_STRING, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.get(-1);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        attributes.put(key, value);
        String actual = attributes.get(key);
        String expected = value;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        String key = "KEY";
        String value = "VALUE";
        attributes.putIgnoreCase(key, value);
        String actual = attributes.getIgnoreCase(key);
        String expected = value;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        String key = "key";
        attributes.put(key, null);
        String actual = attributes.get(key);
        Assert.assertEquals(Attributes.EMPTY_STRING, actual);
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";
        attributes.put(key, value1);
        attributes.put(key, value2);
        String actual1 = attributes.get(key);
        String expected1 = value2; // the last value overwrites the previous one
        Assert.assertEquals(expected1, actual1);
    }

    @Test
    public void testPutWithSameInstance() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        String key = "key";
        String value = "value";
        attributes1.put(key, value);
        attributes2.put(attributes1);
        String actual = attributes2.get(key);
        String expected = value;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        String key = "checked";
        attributes.put(key, "true");
        boolean actual = attributes.hasKey("checked");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
        actual = attributes.getBoolean("checked");
        expected = true;
        Assert.assertEquals(expected, actual);
        attributes.put(key, "false");
        actual = attributes.hasKey("checked");
        expected = false;
        Assert.assertEquals(expected, actual);
        actual = attributes.getBoolean("checked");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseBooleanAttribute() {
        Attributes attributes = new Attributes();
        String key = "CHECKED";
        attributes.putIgnoreCase(key, "true");
        boolean actual = attributes.hasKey("checked");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
        actual = attributes.getBoolean("checked");
        expected = true;
        Assert.assertEquals(expected, actual);
        attributes.putIgnoreCase(key, "false");
        actual = attributes.hasKey("checked");
        expected = false;
        Assert.assertEquals(expected, actual);
        actual = attributes.getBoolean("checked");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutWithSameKeyDifferentValues() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";
        attributes.put(key, value1);
        attributes.put(key, value2);
        String actual = attributes.get(key);
        String expected = value2; // the last value overwrites the previous one
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutWithSameKeySameValue() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        attributes.put(key, value);
        attributes.put(key, value);
        String actual = attributes.get(key);
        String expected = value;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        String key = "key";
        attributes.put(key, "value");
        boolean actual = attributes.hasKey(key);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        String key = "KEY";
        attributes.put(key, "value");
        boolean actual = attributes.hasKeyIgnoreCase(key);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyEmptyString() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey(Attributes.EMPTY_STRING);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyNull() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey(null);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyNegativeIndex() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey(-1);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSizeEmpty() {
        Attributes attributes = new Attributes();
        int actual = attributes.size();
        int expected = 0;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");
        attributes1.addAll(attributes2);
        int actualSize = attributes1.size();
        int expectedSize = 4;
        Assert.assertEquals(expectedSize, actualSize);
        String actualKey1 = attributes1.get("key1");
        String expectedKey1 = "value1";
        Assert.assertEquals(expectedKey1, actualKey1);
        String actualValue1 = attributes1.get("key2");
        String expectedValue1 = "value2";
        Assert.assertEquals(expectedValue1, actualValue1);
        String actualKey3 = attributes1.get("key3");
        String expectedKey3 = "key3";
        Assert.assertEquals(expectedKey3, actualKey3);
        String actualValue3 = attributes1.get("key3");
        String expectedValue3 = "value3";
        Assert.assertEquals(expectedValue3, actualValue3);
        String actualKey4 = attributes1.get("key4");
        String expectedKey4 = "key4";
        Assert.assertEquals(expectedKey4, actualKey4);
        String actualValue4 = attributes1.get("key4");
        String expectedValue4 = "value4";
        Assert.assertEquals(expectedValue4, actualValue4);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        Attribute actual;
        while (iterator.hasNext()) {
            actual = iterator.next();
            String key = actual.getKey();
            String value = actual.getValue();
            if (key.equals("key1")) {
                String expectedKey = "key1";
                String expectedValue = "value1";
                Assert.assertEquals(expectedKey, key);
                Assert.assertEquals(expectedValue, value);
            } else if (key.equals("key2")) {
                String expectedKey = "key2";
                String expectedValue = "value2";
                Assert.assertEquals(expectedKey, key);
                Assert.assertEquals(expectedValue, value);
            }
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> actual = attributes.asList();
        int actualSize = actual.size();
        int expectedSize = 2;
        Assert.assertEquals(expectedSize, actualSize);
        Attribute actualAttribute1 = actual.get(0);
        Attribute expectedAttribute1 = new Attribute("key1", "value1", attributes);
        String actualKey1 = actualAttribute1.getKey();
        String actualValue1 = actualAttribute1.getValue();
        String expectedKey1 = "key1";
        String expectedValue1 = "value1";
        Assert.assertEquals(expectedKey1, actualKey1);
        Assert.assertEquals(expectedValue1, actualValue1);
        Attribute actualAttribute2 = actual.get(1);
        Attribute expectedAttribute2 = new Attribute("key2", "value2", attributes);
        String actualKey2 = actualAttribute2.getKey();
        String actualValue2 = actualAttribute2.getValue();
        String expectedKey2 = "key2";
        String expectedValue2 = "value2";
        Assert.assertEquals(expectedKey2, actualKey2);
        Assert.assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> actual = attributes.dataset();
        int actualSize = actual.size();
        int expectedSize = 2;
        Assert.assertEquals(expectedSize, actualSize);
        String actualKey1 = actual.keySet().iterator().next();
        String expectedKey1 = "data-key1";
        String actualValue1 = actual.get(expectedKey1);
        String expectedValue1 = "value1";
        Assert.assertEquals(expectedKey1, actualKey1);
        Assert.assertEquals(expectedValue1, actualValue1);
        String actualKey2 = actual.keySet().iterator().next();
        String expectedKey2 = "data-key2";
        String actualValue2 = actual.get(expectedKey2);
        String expectedValue2 = "value2";
        Assert.assertEquals(expectedKey2, actualKey2);
        Assert.assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String expectedHtml = " key1=\"value1\"";
        String actualHtml = attributes.html();
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String expectedToString = "Attributes{attributes=[key1=value1], size=1}";
        String actualToString = attributes.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        attributes3.put("key4", "value4");
        boolean actual1 = attributes1.equals(attributes2);
        boolean expected1 = true;
        Assert.assertEquals(expected1, actual1);
        boolean actual2 = attributes1.equals(attributes3);
        boolean expected2 = false;
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        attributes3.put("key4", "value4");
        int actual1 = attributes1.hashCode();
        int expected1 = 31 * (31 * (Attributes.class.hashCode() + size()) + Arrays.hashCode(keys)) + Arrays.hashCode(vals);
        Assert.assertEquals(expected1, actual1);
        int actual2 = attributes2.hashCode();
        int expected2 = 31 * (31 * (Attributes.class.hashCode() + size()) + Arrays.hashCode(new String[]{"key1", "key3"})) + Arrays.hashCode(new String[]{"value1", null});
        Assert.assertEquals(expected2, actual2);
        int actual3 = attributes3.hashCode();
        int expected3 = 31 * (31 * (Attributes.class.hashCode() + size()) + Arrays.hashCode(new String[]{"key1", "key2", "key4"})) + Arrays.hashCode(new String[]{"value1", "value2", "value4"});
        Assert.assertEquals(expected3, actual3);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        int actualSize = clonedAttributes.size();
        int expectedSize = 1;
        Assert.assertEquals(expectedSize, actualSize);
        String actualKey1 = clonedAttributes.get("key1");
        String expectedKey1 = "key1";
        String actualValue1 = clonedAttributes.get("key1");
        String expectedValue1 = "value1";
        Assert.assertEquals(expectedKey1, actualKey1);
        Assert.assertEquals(expectedValue1, actualValue1);
    }

    
}