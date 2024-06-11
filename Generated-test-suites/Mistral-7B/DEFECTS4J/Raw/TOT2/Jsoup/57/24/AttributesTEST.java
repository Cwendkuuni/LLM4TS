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
        String expected = "Value";
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
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        String actual1 = attributes1.get("key1");
        String expected1 = "value1";
        Assert.assertEquals(expected1, actual1);
        String actual2 = attributes1.get("key2");
        String expected2 = "value2";
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        for (Attribute attribute : attributesList) {
            String actual = attribute.getValue();
            String expected = attribute.getKey();
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        String actual1 = dataset.get("data-key1");
        String expected1 = "value1";
        Assert.assertEquals(expected1, actual1);
        String actual2 = dataset.get("data-key2");
        String expected2 = "value2";
        Assert.assertEquals(expected2, actual2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKeyMethod() {
        Attributes attributes = new Attributes();
        String actual = attributes.get(null);
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetIgnoreCaseNullKeyMethod() {
        Attributes attributes = new Attributes();
        String actual = attributes.getIgnoreCase(null);
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase(null, "value");
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", null);
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasKeyNullKeyMethod() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey(null);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasKeyIgnoreCaseNullKeyMethod() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKeyIgnoreCase(null);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase(null);
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }
}