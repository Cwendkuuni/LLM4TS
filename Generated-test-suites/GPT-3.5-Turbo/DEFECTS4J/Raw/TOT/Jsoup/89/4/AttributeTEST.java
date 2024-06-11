
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class AttributeTest {
    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key1", "value1");
        assertNotNull(attribute);
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyAndValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key1", "value1", parent);
        assertNotNull(attribute);
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
        assertSame(parent, attribute.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetKeyWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key1", "value1", parent);
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("newKey", parent.get(0));
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        String oldValue = attribute.setValue("newValue");
        assertEquals("value1", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValueWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key1", "value1", parent);
        String oldValue = attribute.setValue("newValue");
        assertEquals("value1", oldValue);
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.get(0));
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key1", "value1");
        String expected = "key1=\"value1\"";
        assertEquals(expected, attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        Attribute attribute = new Attribute("key1", "value1");
        Document.OutputSetting outputSettings = new Document.OutputSettings();
        attribute.html(null, outputSettings);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key1", "value1");
        String expected = "key1=\"value1\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key1", "value1");
        assertNotNull(attribute);
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key1"));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute("checked", "", null));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "checked", null));
        assertFalse(Attribute.shouldCollapseAttribute("checked", "value1", null));
        assertFalse(Attribute.shouldCollapseAttribute("key1", "", null));
        assertFalse(Attribute.shouldCollapseAttribute("key1", "value1", null));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("value1"));
   }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value1");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
    }
}
