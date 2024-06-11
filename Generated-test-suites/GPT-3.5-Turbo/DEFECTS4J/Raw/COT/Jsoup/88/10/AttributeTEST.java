import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {
    
    // Test case for Attribute(String key, String value)
    @Test
    public void testAttributeKeyValue() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.getKey().equals("key"));
        assert(attribute.getValue().equals("value"));
    }

    // Test case for Attribute(String key, String val, Attributes parent)
    @Test
    public void testAttributeKeyValParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assert(attribute.getKey().equals("key"));
        assert(attribute.getValue().equals("value"));
        assert(attribute.parent == parent);
    }

    // Test case for getKey()
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.getKey().equals("key"));
    }

    // Test case for setKey(String key)
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("new_key");
        assert(attribute.getKey().equals("new_key"));
    }

    // Test case for getValue()
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.getValue().equals("value"));
    }

    // Test case for setValue(String val)
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("new_value");
        assert(attribute.getValue().equals("new_value"));
    }

    // Test case for html()
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.html().equals("key=\"value\""));
    }

    // Test case for createFromEncoded(String unencodedKey, String encodedValue)
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&#34;encodedValue&#34;");
        assert(attribute.getKey().equals("key"));
        assert(attribute.getValue().equals("\"encodedValue\""));
    }

    // Test case for isDataAttribute()
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assert(attribute.isDataAttribute());
    }
     
    // Test case for shouldCollapseAttribute(Document.OutputSettings out)
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("attribute", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assert(!attribute.shouldCollapseAttribute(out));
    }
    
    // Test case for isBooleanAttribute(String key)
    @Test
    public void testIsBooleanAttribute() {
        assert(Attribute.isBooleanAttribute("checked"));
        assert(!Attribute.isBooleanAttribute("value"));
    }
    
    // Test case for equals(Object o)
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assert(attribute1.equals(attribute2));
    }
    
    // Test case for hashCode()
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.hashCode() == 1934918493);
    }
    
    // Test case for clone()
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assert(attribute1.equals(attribute2));
    }
}