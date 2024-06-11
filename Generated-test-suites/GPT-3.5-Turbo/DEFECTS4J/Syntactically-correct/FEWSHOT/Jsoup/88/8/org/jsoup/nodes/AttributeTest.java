import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.internal.StringUtil;

public class AttributeTest {
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testAttributeConstructorWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        
        Attributes parent = new Attributes();
        parent.set("key", "value");
        Attribute childAttribute = parent.asList().get(0);
        childAttribute.setKey("newKey");
        assertEquals("newKey", childAttribute.getKey());
        assertEquals("newKey", parent.getKey("newKey"));
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
        
        Attributes parent = new Attributes();
        parent.set("key", "value");
        Attribute childAttribute = parent.asList().get(0);
        oldValue = childAttribute.setValue("newValue");
        assertEquals("newValue", childAttribute.getValue());
        assertEquals("value", oldValue);
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testHtmlWithOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertEquals(true, attribute.isDataAttribute());
        
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(false, attribute2.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("bool", "bool");
        boolean collapse = attribute.shouldCollapseAttribute(out);
        assertEquals(true, collapse);
        
        Attribute attribute2 = new Attribute("bool", null);
        boolean collapse2 = attribute2.shouldCollapseAttribute(out);
        assertEquals(true, collapse2);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("boolean", "value");
        boolean isBoolean = attribute.isBooleanAttribute();
        assertEquals(false, isBoolean);
    }
    
    @Test
    public void testEquals() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        boolean isEqual = attribute.equals(attribute2);
        assertEquals(true, isEqual);
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        Attribute attribute2 = new Attribute("key", "value");
        int hashCode2 = attribute2.hashCode();
        assertEquals(hashCode, hashCode2);
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute.getKey(), cloneAttribute.getKey());
        assertEquals(attribute.getValue(), cloneAttribute.getValue());
        assertEquals(attribute.parent, cloneAttribute.parent);
    }
}