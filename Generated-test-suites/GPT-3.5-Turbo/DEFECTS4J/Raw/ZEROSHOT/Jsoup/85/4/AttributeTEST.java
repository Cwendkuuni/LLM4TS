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
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.html());
    }
    
    @Test
    public void testToString() {
        String expectedString = "key=\"value\"";
        assertEquals(expectedString, attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertEquals("encodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        
        Attribute dataAttribute = new Attribute("data-key", "data-value");
        assertTrue(dataAttribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute());
        
        Attribute collapsibleAttribute = new Attribute("name", "name");
        assertTrue(collapsibleAttribute.shouldCollapseAttribute());
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute, sameAttribute);
        
        Attribute differentAttribute = new Attribute("differentKey", "differentValue");
        assertNotEquals(attribute, differentAttribute);
    }
    
    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        
        Attribute differentAttribute = new Attribute("differentKey", "differentValue");
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}