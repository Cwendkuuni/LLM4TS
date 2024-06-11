import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

public class AttributeTest {
    
    private Attribute attribute;
    
    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
    }
    
    @After
    public void tearDown() {
        attribute = null;
    }
    
    @Test
    public void testGetKey() {
        assertEquals("testKey", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testGetValue() {
        assertEquals("testValue", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        assertEquals("testKey=\"testValue\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("testKey", "testValue");
        assertEquals("testKey", newAttribute.getKey());
        assertEquals("testValue", newAttribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, sameAttribute);
        
        Attribute differentAttribute = new Attribute("differentKey", "testValue");
        assertNotEquals(attribute, differentAttribute);
    }
    
    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        
        Attribute differentAttribute = new Attribute("differentKey", "testValue");
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute, cloneAttribute);
    }
}