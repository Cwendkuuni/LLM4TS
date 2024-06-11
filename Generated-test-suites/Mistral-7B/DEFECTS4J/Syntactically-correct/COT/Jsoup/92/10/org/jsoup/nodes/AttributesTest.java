import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "value");
        String result = attributes.get("name");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Name", "value");
        String result = attributes.getIgnoreCase("name");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testAdd() {
        Attributes attributes = new Attributes();
        Attributes result = attributes.add("key", "value");
        String expectedValue = "value";
        Assert.assertEquals(attributes, result);
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals(expectedValue, attributes.get("key"));
    }

    // Test other methods similarly
}