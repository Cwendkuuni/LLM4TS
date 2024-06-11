import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("test", "value");
        Assert.assertEquals("test", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("test", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("test", "value");
        Assert.assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("test", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        String expectedHtml = "href=\"index.html\"";
        Assert.assertEquals(expectedHtml, attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("href", "index.html");
        String expectedString = "href=\"index.html\"";
        Assert.assertEquals(expectedString, attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("src", "&#x2F;image.png");
        Assert.assertEquals("src", attribute.getKey());
        Assert.assertEquals("/image.png", attribute.getValue());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("test", "value");
        Attribute attribute2 = new Attribute("test", "value");
        Assert.assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("test", "value");
        int expectedHashCode = attribute.getKey().hashCode() + attribute.getValue().hashCode();
        Assert.assertEquals(expectedHashCode, attribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("test", "value");
        Attribute attribute2 = attribute1.clone();
        Assert.assertEquals(attribute1, attribute2);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        Assert.assertEquals("", attribute.getKey());
    }
    
    @Test
    public void testEmptyValue() {
        Attribute attribute = new Attribute("test", "");
        Assert.assertEquals("", attribute.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attribute attribute = new Attribute(null, "value");
    }
    
    @Test
    public void testNullValue() {
        Attribute attribute = new Attribute("test", null);
        Assert.assertNull(attribute.getValue());
    }
    
    @Test
    public void testSetKeyWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("test", "value", parent);
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
        Assert.assertEquals("newKey", parent.get(0));
    }
    
    @Test
    public void testSetValueWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("test", "value", parent);
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("newValue", parent.get("test"));
    }
}