
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
    public void testPutBoolean_positive() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        boolean result = attributes.hasKey("checked");
        Assert.assertTrue(result);
    }

    @Test
    public void testPutBoolean_negative() {
        Attributes attributes = new Attributes();
        attributes.put("checked", false);
        boolean result = attributes.hasKey("checked");
        Assert.assertFalse(result);
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
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        boolean result1 = attributes1.hasKey("key3");
        boolean result2 = attributes1.hasKey("key4");

        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        Assert.assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();

        Assert.assertEquals(2, list.size());
        Assert.assertEquals("key1", list.get(0).getKey());
        Assert.assertEquals("value1", list.get(0).getValue());
        Assert.assertEquals("key2", list.get(1).getKey());
        Assert.assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String result = attributes.html();

        Assert.assertEquals(" key1=\"value1\" key2=\"value2\"", result);
    }

}

