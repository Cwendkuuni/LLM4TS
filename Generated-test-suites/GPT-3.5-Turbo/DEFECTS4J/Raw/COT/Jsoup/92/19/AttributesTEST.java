
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

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
        attributes.put("key", "value");
        String result = attributes.getIgnoreCase("KeY");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        boolean result = attributes.hasKeyIgnoreCase("checked");
        Assert.assertTrue(result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KeY");
        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKeyIgnoreCase("KeY");
        Assert.assertTrue(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        boolean result = attributes1.hasKey("key2");
        Assert.assertTrue(result);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        for (Attribute attribute : attributes) {
            Assert.assertEquals("key", attribute.getKey());
            Assert.assertEquals("value", attribute.getValue());
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        List<Attribute> attributeList = attributes.asList();
        Assert.assertEquals(1, attributeList.size());
        Attribute attribute = attributeList.get(0);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value", dataset.get("key"));
    }

}
