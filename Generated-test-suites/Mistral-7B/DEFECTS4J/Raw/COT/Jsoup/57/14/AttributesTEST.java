import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetMethod_ValidKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetMethod_InvalidKey() {
        Attributes attributes = new Attributes();

        String result = attributes.get("invalidKey");
        Assert.assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCaseMethod_ValidKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCaseMethod_InvalidKey() {
        Attributes attributes = new Attributes();

        String result = attributes.getIgnoreCase("InvalidKey");
        Assert.assertEquals("", result);
    }

    @Test
    public void testPutMethod_ValidString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethod_NullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethod_NullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutMethod_Boolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);

        attributes.put("key", false);
        result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethod_InvalidBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", -1);
    }

    @Test
    public void testPutMethod_Attribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        Attribute attribute = new Attribute(key, "value");

        attributes.put(attribute);
        String result = attributes.get(key);
        Assert.assertEquals("value", result);
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethod_NullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(null);
    }

    @Test
    public void testRemoveMethod_ValidKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("key");
        String result = attributes.get("key");
        Assert.assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethod_InvalidKey() {
        Attributes attributes = new Attributes();
        attributes.remove("invalidKey");
    }

    @Test
    public void testRemoveIgnoreCaseMethod_ValidKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.removeIgnoreCase("key");
        String result = attributes.get("key");
        Assert.assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseMethod_InvalidKey() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase("invalidKey");
    }

    @Test
    public void testHasKeyMethod_ValidKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyMethod_InvalidKey() {
        Attributes attributes = new Attributes();

        boolean result = attributes.hasKey("invalidKey");
        Assert.assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod_ValidKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod_InvalidKey() {
        Attributes attributes = new Attributes();

        boolean result = attributes.hasKeyIgnoreCase("invalidKey");
        Assert.assertFalse(result);
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
        int sizeResult = attributes1.size();
        Assert.assertEquals(2, sizeResult);

        boolean key1Result = attributes1.hasKey("key1");
        Assert.assertTrue(key1Result);

        boolean key2Result = attributes1.hasKey("key2");
        Assert.assertTrue(key2Result);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "value");

        Map<String, String> result = attributes.dataset();
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("data-test", "value");

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.html();
        String expected = " key=\"value\"";

        Assert.assertEquals(expected, result);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethod_IOException() {
        Attributes attributes = new Attributes();

        attributes.html(null, null);
    }

    @Test
    public void testEqualsMethod_SameObject() {
        Attributes attributes = new Attributes();

        boolean result = attributes.equals(attributes);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsMethod_DifferentObjects() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        boolean result = attributes1.equals(attributes2);
        Assert.assertFalse(result);
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        int attributes1HashCode = attributes1.hashCode();
        int attributes2HashCode = attributes2.hashCode();

        Assert.assertNotEquals(attributes1HashCode, attributes2HashCode);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Attributes clonedAttributes = attributes.clone();
        String clonedValue = clonedAttributes.get("key");

        Assert.assertEquals("value", clonedValue);
    }
}