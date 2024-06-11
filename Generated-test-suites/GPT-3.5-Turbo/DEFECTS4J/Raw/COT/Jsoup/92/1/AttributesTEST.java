import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        String value = attributes.get("key1");

        Assert.assertEquals("value1", value);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        String value = attributes.getIgnoreCase("KEY1");

        Assert.assertEquals("value1", value);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        String value = attributes.get("key1");

        Assert.assertEquals("value1", value);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        boolean hasKey = attributes.hasKey("key1");

        Assert.assertTrue(hasKey);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        attributes.remove("key1");

        boolean hasKey = attributes.hasKey("key1");

        Assert.assertFalse(hasKey);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        boolean hasKey = attributes.hasKey("key1");

        Assert.assertTrue(hasKey);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        boolean hasKey = attributes.hasKeyIgnoreCase("KEY1");

        Assert.assertTrue(hasKey);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int size = attributes.size();

        Assert.assertEquals(2, size);
    }

    @Test
    public void testAddAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes newAttributes = new Attributes();
        newAttributes.put("key2", "value2");

        attributes.addAll(newAttributes);

        int size = attributes.size();

        Assert.assertEquals(2, size);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        List<Attribute> attributeList = attributes.asList();

        Assert.assertEquals(1, attributeList.size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals("value1", dataset.get("key1"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        Assert.assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        boolean equals = attributes1.equals(attributes2);

        Assert.assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clone = attributes.clone();

        boolean equals = attributes.equals(clone);

        Assert.assertTrue(equals);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");

        attributes.normalize();

        boolean hasKey = attributes.hasKey("key1");

        Assert.assertTrue(hasKey);
    }
}