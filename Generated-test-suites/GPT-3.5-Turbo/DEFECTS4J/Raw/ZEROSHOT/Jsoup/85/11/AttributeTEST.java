import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    // Test getKey method
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    // Test setKey method
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Test getValue method
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    // Test setValue method
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    // Test html method
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    // Test toString method
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String toString = attribute.toString();
        assertEquals("key=\"value\"", toString);
    }

    // Test createFromEncoded method
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Test isDataAttribute method
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    // Test shouldCollapseAttribute method
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "key");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    // Test isBooleanAttribute method
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    // Test equals method
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(961292, attribute.hashCode());
    }

    // Test clone method
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
    }
}