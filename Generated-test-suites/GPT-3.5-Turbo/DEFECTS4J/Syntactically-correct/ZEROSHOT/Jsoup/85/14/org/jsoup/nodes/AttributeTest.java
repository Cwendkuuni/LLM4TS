import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        Assert.assertFalse(attribute.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Assert.assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertNotNull(attribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute cloneAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), cloneAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), cloneAttribute.getValue());
    }
}