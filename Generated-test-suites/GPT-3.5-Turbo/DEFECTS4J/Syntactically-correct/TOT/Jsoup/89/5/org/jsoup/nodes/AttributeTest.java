
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
    
    private Attribute attribute;
    private Attributes parent;
    
    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("name", "value", parent);
    }
    
    @Test
    public void testConstructor() {
        Assert.assertEquals("name", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertEquals(parent, attribute.parent);
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey("newName");
        Assert.assertEquals("newName", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertEquals(parent, attribute.parent);
    }
    
    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("name", attribute.getKey());
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals(parent, attribute.parent);
        Assert.assertEquals("value", oldValue);
    }
    
    @Test
    public void testHtml() throws SerializationException {
        String expected = "name=\"value\"";
        String result = attribute.html();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToString() {
        String expected = "name=\"value\"";
        String result = attribute.toString();
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "value");
        Assert.assertEquals("key", encodedAttribute.getKey());
        Assert.assertEquals("value", encodedAttribute.getValue());
        Assert.assertNull(encodedAttribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Assert.assertFalse(attribute.isDataAttribute());
        Assert.assertTrue(Attribute.isDataAttribute("data-key"));
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean result = attribute.shouldCollapseAttribute(outputSettings);
        Assert.assertFalse(result);
        
        Attribute booleanAttribute = new Attribute("checked", null, null);
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        result = booleanAttribute.shouldCollapseAttribute(outputSettings);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Assert.assertFalse(attribute.isBooleanAttribute());
        Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("name", "value", null);
        Assert.assertTrue(attribute.equals(attribute2));
        
        Attribute attribute3 = new Attribute("name", "newValue", null);
        Assert.assertFalse(attribute.equals(attribute3));
        
        Assert.assertFalse(attribute.equals(null));
        Assert.assertFalse(attribute.equals("String"));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("name", "value", null);
        Assert.assertEquals(attribute.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
        Assert.assertEquals(attribute.parent, clonedAttribute.parent);
        Assert.assertNotSame(attribute, clonedAttribute);
    }
}
