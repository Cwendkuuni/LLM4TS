import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.jsoup.internal.StringUtil.copyOf;
import static org.jsoup.internal.StringUtil.isEmpty;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void testGetEmptyStringAndBoolean() {
        Attributes attributes = new Attributes();
        String result1 = attributes.get("key");
        Assert.assertEquals(isEmpty(result1), true);
        attributes.put("key", true);
        String result2 = attributes.get("key");
        Assert.assertEquals(isEmpty(result2), true);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testPutSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String result = attributes.get("key");
        Assert.assertEquals("value2", result);
    }

    @Test
    public void testPutEmptyArray() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        String result = attributes.get("key");
        Assert.assertEquals(isEmpty(result), true);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyEmptyString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        Assert.assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int size = attributes.size();
        Assert.assertEquals(size, 2);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        String result1 = attributes1.get("key1");
        String result2 = attributes1.get("key2");
        Assert.assertEquals("value1", result1);
        Assert.assertEquals("value2", result2);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            String key = attribute.getKey();
            String value = attribute.getValue();
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        int size = list.size();
        Assert.assertEquals(size, 2);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        int size = dataset.size();
        Assert.assertEquals(size, 2);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        Attributes htmlAttributes = new Attributes();
        htmlAttributes.put("key1", "value1");
        htmlAttributes.put("key2", "value2");
        StringBuilder expectedHtml = StringUtil.borrowBuilder();
        htmlAttributes.html(expectedHtml, null);
        String resultHtml = new Attributes().html();
        Assert.assertEquals(StringUtil.releaseBuilder(expectedHtml), resultHtml);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String expectedToString = attributes.html();
        String resultToString = attributes.toString();
        Assert.assertEquals(expectedToString, resultToString);
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
        attributes3.put("key3", "value3");
        Assert.assertEquals(attributes1, attributes3);
        Assert.assertNotEquals(attributes1, attributes2);
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
        attributes3.put("key3", "value3");
        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        int hashCode3 = attributes3.hashCode();
        Assert.assertEquals(hashCode1, hashCode3);
        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes clonedAttributes = attributes.clone();
        String result1 = attributes.get("key1");
        String result2 = clonedAttributes.get("key1");
        Assert.assertEquals(result1, result2);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "Value");
        attributes.normalize();
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        String result1 = attributes.get("key1");
        Assert.assertEquals(isEmpty(result1), true);
        String result2 = attributes.get("key2");
        Assert.assertEquals("value2", result2);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.removeIgnoreCase("key1");
        String result1 = attributes.get("Key1");
        Assert.assertEquals(isEmpty(result1), true);
        String result2 = attributes.get("Key2");
        Assert.assertEquals("Value2", result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckCapacityWithNegativeSize() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckCapacityWithNullKeys() {
        Attributes attributes = new Attributes();
        String[] nullKeys = null;
        attributes.checkCapacity(nullKeys.length);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckCapacityWithNullVals() {
        Attributes attributes = new Attributes();
        String[] keys = new String[1];
        String[] nullVals = null;
        attributes.checkCapacity(keys.length, nullVals);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckCapacityWithInvalidMinNewSize() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(attributes.size() - 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveWithInvalidIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put(EmptyString, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", EmptyString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutWithFullCapacity() {
        Attributes attributes = new Attributes();
        String[] keys = new String[Attributes.InitialCapacity];
        String[] vals = new String[Attributes.InitialCapacity];
        for (int i = 0; i < Attributes.InitialCapacity; i++) {
            attributes.put(keys[i], vals[i] = Integer.toString(i));
        }
        String newKey = "newKey";
        String newValue = "newValue";
        int newSize = Attributes.InitialCapacity + 1;
        attributes.checkCapacity(newSize);
        attributes.put(newKey, newValue);
        String result = attributes.get(newKey);
        Assert.assertEquals(newValue, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNotFoundIndex() {
        Attributes attributes = new Attributes();
        String result = attributes.get(Integer.MIN_VALUE);
        Assert.assertEquals(isEmpty(result), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        String result = attributes.get(null);
        Assert.assertEquals(isEmpty(result), true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testHasKeyWithNotFoundIndex() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey(Integer.MIN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasKeyWithNullKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey(null));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSizeWithNegativeSize() {
        Attributes attributes = new Attributes();
        int result = attributes.size();
        Assert.assertEquals(Attributes.InitialCapacity, result);
        attributes.size = Attributes.InitialCapacity - 1;
        result = attributes.size();
        Assert.assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllWithNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIteratorWithNullAttributes() {
        Attributes attributes = null;
        Iterator<Attribute> iterator = attributes.iterator();
        Assert.assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveDuringIteration() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            if (attribute.getKey().equals("key1")) {
                iterator.remove();
                break;
            }
        }
        String result1 = attributes.get("key1");
        Assert.assertEquals(isEmpty(result1), true);
        String result2 = attributes.get("key2");
        Assert.assertEquals("value2", result2);
    }

    
}