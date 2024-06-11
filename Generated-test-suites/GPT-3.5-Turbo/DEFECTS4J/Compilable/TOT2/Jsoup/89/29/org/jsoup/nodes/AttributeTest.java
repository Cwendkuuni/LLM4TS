package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "testValue");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "oldValue");
        String oldValue = attribute.setValue("newValue");
        assertEquals("oldValue", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey=\"testValue\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("href", "index.html");
        assertEquals("href", attribute.getKey());
        assertEquals("index.html", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-test", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("test", "test");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("test", "value");
        Attribute attribute2 = new Attribute("test", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertNotNull(attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}