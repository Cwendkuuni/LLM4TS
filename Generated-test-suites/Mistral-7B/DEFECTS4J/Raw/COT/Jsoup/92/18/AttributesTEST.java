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
        attributes.put("Key", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanAttr", false);
        Assert.assertFalse(attributes.get("booleanAttr").isEmpty());
        attributes.remove("booleanAttr");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("booleanAttr"));
        attributes.put("booleanAttr", true);
        Assert.assertFalse(attributes.get("booleanAttr").isEmpty());
        Assert.assertTrue(attributes.hasKeyIgnoreCase("booleanAttr"));
        Assert.assertEquals("true", attributes.getIgnoreCase("booleanAttr"));
    }

    @Test
    public void testPutWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes expected = new Attributes();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        attributes1.addAll(attributes2);
        Assert.assertEquals(expected.size(), attributes1.size());
        Assert.assertEquals(expected.get("key1"), attributes1.get("key1"));
        Assert.assertEquals(expected.get("key2"), attributes1.get("key2"));
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> expected = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );
        List<Attribute> actual = attributes.asList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        Map<String, String> actual = attributes.dataset();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlThrowsSerializationException() {
        Attributes attributes = new Attributes();
        StringBuilder sb = StringUtil.borrowBuilder();
        attributes.html(sb, null);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");
        attributes.normalize();
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }
}