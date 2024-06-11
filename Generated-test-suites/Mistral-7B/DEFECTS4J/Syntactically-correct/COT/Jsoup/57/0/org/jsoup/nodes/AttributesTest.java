import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "newValue");
        String result = attributes.get("key");
        Assert.assertEquals("newValue", result);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        String key = "key";
        Attribute attribute = new Attribute(key, "value");
        attributes.put(attribute);
        String result = attributes.get(key);
        Assert.assertEquals("value", result);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String result = attributes.get("key");
        Assert.assertEquals("", result);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        String result = attributes.getIgnoreCase("Key");
        Assert.assertEquals("", result);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size = attributes1.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        List<String> keys = new ArrayList<>();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            keys.add(attribute.getKey());
        }
        List<String> expectedKeys = Arrays.asList("key1", "key2");
        Assert.assertEquals(expectedKeys, keys);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        List<Attribute> expectedList = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
        Assert.assertEquals(expectedList, list);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        Map<String, String> result = attributes.dataset();
        Assert.assertEquals("data-value", result.get("key"));
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodThrowsSerializationException() {
        Attributes attributes = new Attributes();
        try {
            attributes.html(null, null);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        boolean result = attributes1.equals(attributes2);
        Assert.assertTrue(result);
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        int result1 = attributes1.hashCode();
        int result2 = attributes2.hashCode();
        Assert.assertNotEquals(result1, result2);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        String result = clonedAttributes.get("key1");
        Assert.assertEquals("value1", result);
    }

    @Test
    public void testDataKeyMethod() {
        String dataKey = Attributes.dataKey("key");
        Assert.assertEquals("data-key", dataKey);
    }
}