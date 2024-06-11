import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {
    

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        Assert.assertTrue(actual);
        attributes.put("key", false);
        actual = attributes.hasKey("key");
        Assert.assertFalse(actual);
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        String key = "key";
        Attribute attribute = new Attribute(key, "value");
        attributes.put(attribute);
        String actual = attributes.get(key);
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        Assert.assertTrue(actual);
        attributes.remove("key");
        actual = attributes.hasKey("key");
        Assert.assertFalse(actual);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(actual);
        attributes.removeIgnoreCase("Key");
        actual = attributes.hasKeyIgnoreCase("key");
        Assert.assertFalse(actual);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey("key");
        Assert.assertFalse(actual);
        attributes.put("key", "value");
        actual = attributes.hasKey("key");
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKeyIgnoreCase("Key");
        Assert.assertFalse(actual);
        attributes.put("Key", "Value");
        actual = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        int actual = attributes.size();
        Assert.assertEquals(0, actual);
        attributes.put("key", "value");
        actual = attributes.size();
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int sizeBefore = attributes1.size();
        String key = "key2";
        String value = "value2";
        attributes1.put(key, value);
        int sizeAfter = attributes1.size();
        Assert.assertEquals(sizeBefore + 1, sizeAfter);
        String actual = attributes1.get(key);
        Assert.assertEquals(value, actual);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        for (Attribute attribute : attributes) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
        }
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        for (Attribute attribute : list) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
        }
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String expectedPrefix = "data-";
            String actualPrefix = key.substring(0, expectedPrefix.length());
            Assert.assertEquals(expectedPrefix, actualPrefix);
            String actual = attributes.get(key);
            Assert.assertEquals(value, actual);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKeyMethod() {
        Attributes attributes = new Attributes();
        String key = null;
        String actual = attributes.get(key);
        Assert.assertNull(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetIgnoreCaseNullKeyMethod() {
        Attributes attributes = new Attributes();
        String key = null;
        String actual = attributes.getIgnoreCase(key);
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        String key = null;
        attributes.put(key, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        String key = "key";
        attributes.put(key, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKeyMethod() {
        Attributes attributes = new Attributes();
        String key = null;
        attributes.remove(key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseNullKeyMethod() {
        Attributes attributes = new Attributes();
        String key = null;
        attributes.removeIgnoreCase(key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        String actual = clonedAttributes.get("key1");
        String expected = "value1";
        Assert.assertEquals(expected, actual);
    }

    
}