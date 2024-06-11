import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "Value");
        Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutString() {
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key", false);
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        Assert.assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        
        attributes.addAll(incoming);
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-test", "value");
        Assert.assertEquals("value", attributes.dataset().get("test"));
    }

    // More test cases for other methods can be added here

}