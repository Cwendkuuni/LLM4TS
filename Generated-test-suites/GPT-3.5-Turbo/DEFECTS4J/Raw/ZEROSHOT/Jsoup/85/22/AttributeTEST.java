import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("test", "value");
        assertEquals("test", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("test", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("test", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("test", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("test", "value");
        assertEquals("test=\"value\"", attribute.html());
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("test", "value");
        Attribute attribute2 = new Attribute("test", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("test", "value");
        assertEquals(3535409, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("test", "value");
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute.getKey(), cloneAttribute.getKey());
        assertEquals(attribute.getValue(), cloneAttribute.getValue());
    }
}