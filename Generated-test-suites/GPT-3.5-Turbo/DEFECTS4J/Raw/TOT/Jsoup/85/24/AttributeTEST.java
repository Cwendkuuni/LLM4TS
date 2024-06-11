import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    
    private Attribute attribute;
    private static final String KEY = "key";
    private static final String VALUE = "value";
    
    @Before
    public void setUp() {
        attribute = new Attribute(KEY, VALUE);
    }
    
    @Test
    public void testGetKey() {
        assertEquals(KEY, attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        String newKey = "newKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        assertEquals(VALUE, attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals(VALUE, oldValue);
    }
    
    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue";
        Attribute createdAttribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, createdAttribute.getKey());
        assertEquals(encodedValue, createdAttribute.getValue());
        assertNull(createdAttribute.parent);
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.parent);
    }
    
    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", VALUE);
        assertTrue(dataAttribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document().outputSettings()));
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(new Document().outputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute(KEY, VALUE);
        assertTrue(attribute.equals(sameAttribute));
        
        Attribute differentKeyAttribute = new Attribute("differentKey", VALUE);
        assertFalse(attribute.equals(differentKeyAttribute));
        
        Attribute differentValueAttribute = new Attribute(KEY, "differentValue");
        assertFalse(attribute.equals(differentValueAttribute));
        
        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute(KEY, VALUE);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        
        Attribute differentKeyAttribute = new Attribute("differentKey", VALUE);
        assertNotEquals(attribute.hashCode(), differentKeyAttribute.hashCode());
        
        Attribute differentValueAttribute = new Attribute(KEY, "differentValue");
        assertNotEquals(attribute.hashCode(), differentValueAttribute.hashCode());
    }
}