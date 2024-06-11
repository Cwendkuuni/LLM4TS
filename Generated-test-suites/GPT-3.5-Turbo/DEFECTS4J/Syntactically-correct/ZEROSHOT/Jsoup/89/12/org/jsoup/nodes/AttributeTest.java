import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    
    private Attribute attribute;
    
    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
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
        assertEquals("testHTML", attribute.html());
    }
    
    @Test
    public void testToString() {
        assertEquals("testString", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("newKey", "encodedValue");
        assertEquals("newKey", newAttribute.getKey());
        assertEquals("decodedValue", newAttribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute());
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attribute.isBooleanAttribute());
    }
    
    @Test
    public void testEquals() {
        Attribute newAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, newAttribute);
    }
    
    @Test
    public void testHashCode() {
        Attribute newAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), newAttribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute newAttribute = attribute.clone();
        assertEquals(attribute, newAttribute);
    }
}