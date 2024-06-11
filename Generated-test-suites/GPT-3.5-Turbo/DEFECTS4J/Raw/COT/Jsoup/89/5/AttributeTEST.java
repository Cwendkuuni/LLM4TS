
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.SerializationException;

import static org.junit.Assert.assertEquals;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
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
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&lt;value&gt;");
        assertEquals("key", attribute.getKey());
        assertEquals("<value>", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertEquals(true, attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        assertEquals(true, attribute.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(false, attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(true, attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(542323924, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}
