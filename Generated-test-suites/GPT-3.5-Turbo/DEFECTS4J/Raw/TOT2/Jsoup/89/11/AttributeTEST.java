import org.jsoup.nodes.Attribute;
import org.jsoup.AttributeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    
    @Test
    public void testCreateAttribute() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "testValue");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "oldValue");
        String updatedValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("oldValue", updatedValue);
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String html = attribute.html();
        assertEquals("testKey=\"testValue\"", html);
    }
    
    @Test(expected = SerializationException.class)
    public void testHtmlException() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.html();
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("lt", "&lt;");
        assertEquals("lt", attribute.getKey());
        assertEquals("<", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-test"));
        assertFalse(Attribute.isDataAttribute("test"));
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("test", "");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("hidden"));
        assertFalse(Attribute.isBooleanAttribute("test"));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}