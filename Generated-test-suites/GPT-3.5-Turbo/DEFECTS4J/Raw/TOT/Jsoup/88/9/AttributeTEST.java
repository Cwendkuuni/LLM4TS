import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.SerializationException;

import java.io.IOException;

import static org.junit.Assert.*;

public class AttributeTest {
    
    private Attribute attribute;
    private Attributes attributes;
    private Attribute booleanAttribute;
    
    @Before
    public void setup() {
        attribute = new Attribute("key", "value");
        attributes = new Attributes();
        booleanAttribute = new Attribute("checked", null, attributes);
    }
    
    @Test
    public void testCreateAttribute() {
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.html());
    }
    
    @Test(expected = SerializationException.class)
    public void testHtml_withSerializationException() throws IOException {
        Attribute invalidAttribute = new Attribute("invalidKey", "invalidValue");
        invalidAttribute.html();
    }
    
    @Test
    public void testToString() {
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "&lt;value&gt;";
        Attribute encodedAttribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        
        assertNotNull(encodedAttribute);
        assertEquals("key", encodedAttribute.getKey());
        assertEquals("<value>", encodedAttribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        
        String dataAttributeKey = "data-key";
        Attribute dataAttribute = new Attribute(dataAttributeKey, "value");
        assertTrue(dataAttribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        
        assertFalse(attribute.shouldCollapseAttribute(out));
        
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(out));
        
        Attribute booleanAttributeWithValue = new Attribute("checked", "checked");
        assertTrue(booleanAttributeWithValue.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        
        assertTrue(booleanAttribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        assertFalse(attribute.equals(null));
        assertTrue(attribute.equals(attribute));
        
        Attribute sameAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(sameAttribute));
        
        Attribute differentAttribute = new Attribute("key", "newValue");
        assertFalse(attribute.equals(differentAttribute));
        
        Attribute nullValueAttribute = new Attribute("checked", null);
        Attribute nullValueAttribute2 = new Attribute("checked", null);
        assertTrue(nullValueAttribute.equals(nullValueAttribute2));
    }
    
    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        
        Attribute nullValueAttribute = new Attribute("checked", null);
        Attribute nullValueAttribute2 = new Attribute("checked", null);
        assertEquals(nullValueAttribute.hashCode(), nullValueAttribute2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotSame(attribute, clonedAttribute);
    }
    
    @Test
    public void testBooleanAttributeParentUpdate() {
        booleanAttribute.setValue("checked");
        
        assertEquals("checked", booleanAttribute.getValue());
        assertEquals("checked", attributes.get("checked"));
    }
    
}