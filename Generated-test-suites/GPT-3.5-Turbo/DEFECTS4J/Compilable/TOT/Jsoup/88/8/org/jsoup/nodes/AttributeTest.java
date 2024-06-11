package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Test
    public void testConstructor() {
        attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.html());
    }

    @Test
    public void testToString() {
        attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        attribute = Attribute.createFromEncoded("key", "&amp;");
        assertEquals("key", attribute.getKey());
        assertEquals("&", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        attribute = new Attribute("key", "value");
        Attribute other = new Attribute("key", "value");
        assertTrue(attribute.equals(other));
    }

    @Test
    public void testHashCode() {
        attribute = new Attribute("key", "value");
        Attribute other = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        attribute = new Attribute("key", "value");
        Attribute cloned = attribute.clone();
        assertNotNull(cloned);
        assertNotSame(attribute, cloned);
        assertEquals(attribute.getKey(), cloned.getKey());
        assertEquals(attribute.getValue(), cloned.getValue());
    }

}