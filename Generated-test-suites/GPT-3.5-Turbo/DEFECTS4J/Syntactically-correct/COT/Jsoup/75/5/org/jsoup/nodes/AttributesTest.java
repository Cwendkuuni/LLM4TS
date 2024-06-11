
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("bool", true);

        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("", attributes.get("bool"));
        Assert.assertEquals("", attributes.get("notFound"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("KEY"));
        Assert.assertEquals("", attributes.getIgnoreCase("notFound"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertEquals("value", attributes.get("key"));

        attributes.put("key", "new value");

        Assert.assertEquals("new value", attributes.get("key"));
    }

    @Test
    public void testPutWithBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("bool", true);

        Assert.assertEquals("", attributes.get("bool"));

        attributes.put("bool", false);

        Assert.assertFalse(attributes.hasKey("bool"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        Assert.assertEquals("value", attributes.get("key"));

        attribute.setValue("new value");
        attributes.put(attribute);

        Assert.assertEquals("new value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertTrue(attributes.hasKey("key"));

        attributes.remove("key");

        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertTrue(attributes.hasKey("key"));

        attributes.removeIgnoreCase("KEY");

        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("notFound"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("notFound"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
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

        attributes1.addAll(attributes2);

        Assert.assertTrue(attributes1.hasKey("key1"));
        Assert.assertTrue(attributes1.hasKey("key2"));
    }
}
