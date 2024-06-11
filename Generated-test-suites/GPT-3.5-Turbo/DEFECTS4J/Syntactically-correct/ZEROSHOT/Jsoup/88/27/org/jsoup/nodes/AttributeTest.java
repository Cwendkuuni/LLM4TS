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
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        Assert.assertFalse(attribute1.shouldCollapseAttribute(new Document.OutputSettings()));
        
        Attribute attribute2 = new Attribute("key", "");
        Assert.assertTrue(attribute2.shouldCollapseAttribute(new Document.OutputSettings()));
        
        Attribute attribute3 = new Attribute("key", "key");
        Assert.assertTrue(attribute3.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        Assert.assertFalse(attribute1.isBooleanAttribute());
        
        Attribute attribute2 = new Attribute("checked", "");
        Assert.assertTrue(attribute2.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        
        Assert.assertNotEquals(attribute1, attribute2);
        Assert.assertEquals(attribute1, attribute3);
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        
        Assert.assertEquals(attribute.hashCode(), attribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        
        Assert.assertEquals(attribute, clonedAttribute);
        Assert.assertNotSame(attribute, clonedAttribute);
    }
}