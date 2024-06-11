
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertEquals("", attributes.getIgnoreCase("NonExistent"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        Assert.assertEquals("", attributes.get("key")); // Boolean attributes should not have a value

        attributes.put("key", false);
        Assert.assertEquals("", attributes.get("key")); // Boolean attributes should be removed if value is false
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));

        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("key");

        Assert.assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.removeIgnoreCase("KeY");

        Assert.assertEquals("", attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("kEy"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        attributes1.addAll(attributes2);

        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertEquals("value2", attributes1.get("key2"));
    }
}
