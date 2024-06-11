import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {

    // Test getKey() method
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    // Test setKey() method
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Test getValue() method
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    // Test setValue() method
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    // Test html() method
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    // Test createFromEncoded() method
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Test isDataAttribute() method
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    // Test shouldCollapseAttribute() method
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    // Test isBooleanAttribute() method
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    // Test equals() method
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute2.equals(attribute1));
        attribute1 = new Attribute("key", "value1");
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute2.equals(attribute1));
        attribute1 = new Attribute("key", "value");
        attribute2 = new Attribute("key1", "value");
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute2.equals(attribute1));
        attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute2.equals(attribute1));
    }

    // Test hashCode() method
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        attribute1 = new Attribute("key", "value1");
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
        attribute1 = new Attribute("key1", "value");
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
        attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    // Test clone() method
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertFalse(attribute == clonedAttribute);
    }
}