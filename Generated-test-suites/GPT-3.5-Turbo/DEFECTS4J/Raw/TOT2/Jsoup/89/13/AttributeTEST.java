import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.setValue("newValue"));
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("name", "John");
        assertEquals("name=\"John\"", attribute.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "&quot;John&quot;");
        assertEquals("name", attribute.getKey());
        assertEquals("\"John\"", attribute.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("checked", "checked");
        assertTrue(attribute.shouldCollapseAttribute());
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("selected"));
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("id", "123");
        Attribute attribute2 = new Attribute("id", "123");
        assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("class", "test");
        assertEquals(3054, attribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("test", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}