import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {
    
    // Test case for Attribute constructor with unencoded key and value
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }
    
    // Test case for Attribute constructor with unencoded key, value and parent
    @Test
    public void testAttributeConstructorWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    // Test case for getKey() method
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    // Test case for setKey() method
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertNull(attribute.parent);
    }

    // Test case for getValue() method
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    // Test case for setValue() method
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
        assertNull(attribute.parent);
    }

    // Test case for html() method
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    // Test case for createFromEncoded() static method
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
        assertNull(attribute.parent);
    }

    // Test case for isDataAttribute() method
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    // Test case for shouldCollapseAttribute() method with Document.OutputSettings.HTML syntax
    @Test
    public void testShouldCollapseAttributeHtmlSyntax() {
        Document.OutputSettings out = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "key", out));
    }
    
    // Test case for shouldCollapseAttribute() method with Document.OutputSettings.XHTML syntax
    @Test
    public void testShouldCollapseAttributeXhtmlSyntax() {
        Document.OutputSettings out = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xhtml);
        assertFalse(Attribute.shouldCollapseAttribute("key", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "key", out));
    }

    // Test case for equals() method
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "newValue");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(64289698, attribute.hashCode());
    }

    // Test case for clone() method
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertNotNull(clone);
        assertNotSame(attribute, clone);
        assertEquals("key", clone.getKey());
        assertEquals("value", clone.getValue());
        assertEquals(attribute.parent, clone.parent);
    }
}