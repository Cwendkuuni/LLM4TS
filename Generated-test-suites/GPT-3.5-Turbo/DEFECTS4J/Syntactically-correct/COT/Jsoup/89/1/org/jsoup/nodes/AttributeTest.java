import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    
    // Testing constructor Attribute(String, String)
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    // Testing setKey(String)
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    // Testing setValue(String)
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    // Testing html()
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }
    
    // Testing toString()
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }
    
    // Testing createFromEncoded(String, String)
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    // Testing isDataAttribute()
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    // Testing shouldCollapseAttribute(Document.OutputSettings)
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings settings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(settings));
    }
    
    // Testing isBooleanAttribute(String)
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.isBooleanAttribute());
    }
    
    // Testing equals(Object)
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1, attribute2);
    }
    
    // Testing hashCode()
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        assertEquals(hashCode, attribute.hashCode());
    }
    
    // Testing clone()
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
    
}