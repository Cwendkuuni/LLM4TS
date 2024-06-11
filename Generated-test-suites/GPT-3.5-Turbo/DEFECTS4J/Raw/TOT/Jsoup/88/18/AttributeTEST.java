import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testFromString() {
        Attribute newAttribute = Attribute.createFromEncoded("newKey", "new&amp;Value");
        assertEquals("newKey", newAttribute.getKey());
        assertEquals("new&Value", newAttribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "data-value");
        assertTrue(dataAttribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Attribute collapsibleAttribute = new Attribute("key", "key");
        assertTrue(collapsibleAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("allowfullscreen", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute, equalAttribute);
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
    
    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }
}