import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.SerializationException;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeCreation_NullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeCreation_EmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeCreation_NullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKey_NullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKey_EmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValue_NullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertNull(clone.parent);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("differentKey", "value");
        Attribute attribute4 = new Attribute("key", "differentValue");
        
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

}