import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
        assertEquals("newKey=\"value\"", attribute.html());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        assertEquals("key", attribute.getKey());
        assertEquals("newValue", attribute.getValue());
        assertNull(attribute.parent);
        assertEquals("key=\"newValue\"", attribute.html());
        assertEquals("value", oldValue);
    }

    @Test
    public void testHtmlShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testHtmlShouldNotCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("newKey", "value");

        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("newKey", "value");

        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();

        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
        assertNull(attribute2.parent);
    }
}