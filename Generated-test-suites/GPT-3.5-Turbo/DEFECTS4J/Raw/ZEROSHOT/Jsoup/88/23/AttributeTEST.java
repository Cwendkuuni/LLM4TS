import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {
    private Attribute attribute;
    private String key;
    private String value;
    private String newKey;
    private String newValue;
    
    @Before
    public void setUp() {
        key = "testKey";
        value = "testValue";
        newKey = "newKey";
        newValue = "newValue";
        
        attribute = new Attribute(key, value);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(attribute);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertNull(attribute.parent);
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey(newKey);
        
        assertEquals(newKey, attribute.getKey());
        assertNull(attribute.parent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNullKey() {
        attribute.setKey(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyKey() {
        attribute.setKey("");
    }
    
    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue(newValue);
        
        assertEquals(newValue, attribute.getValue());
        assertNull(attribute.parent);
        assertEquals(value, oldValue);
    }
    
    @Test
    public void testHtml() {
        String html = attribute.html();
        
        assertEquals(key + "=\"" + value + "\"", html);
    }
    
    @Test
    public void testToString() {
        String str = attribute.toString();
        
        assertEquals(key + "=\"" + value + "\"", str);
    }
    
    @Test
    public void testEquals() {
        Attribute newAttribute = new Attribute(key, value);
        Attribute differentKeyAttribute = new Attribute(newKey, value);
        Attribute differentValueAttribute = new Attribute(key, newValue);
        
        assertEquals(attribute, newAttribute);
        assertNotEquals(attribute, differentKeyAttribute);
        assertNotEquals(attribute, differentValueAttribute);
    }
    
    @Test
    public void testHashCode() {
        Attribute newAttribute = new Attribute(key, value);
        
        assertEquals(attribute.hashCode(), newAttribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}