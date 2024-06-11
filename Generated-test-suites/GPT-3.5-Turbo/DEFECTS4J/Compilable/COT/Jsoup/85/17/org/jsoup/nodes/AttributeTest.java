package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("name", "value");
        assertEquals("name", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setKey("newName");
        assertEquals("newName", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("name", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("name", "value");
        assertEquals("name=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("name", "value");
        assertEquals("name=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "encodedValue");
        assertEquals("name", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        assertTrue(attribute.isDataAttribute());
        assertFalse(Attribute.isDataAttribute("name"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("name", "");
        Attribute attribute2 = new Attribute("name", "name");
        Attribute attribute3 = new Attribute("name", "value");
        assertTrue(attribute1.shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(attribute2.shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(attribute3.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("allowfullscreen", null);
        Attribute attribute2 = new Attribute("name", "value");
        assertTrue(attribute1.isBooleanAttribute());
        assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Attribute attribute3 = new Attribute("name", "newValue");
        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Attribute attribute3 = new Attribute("name", "newValue");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1, attribute2);
        assertNotSame(attribute1, attribute2);
    }
}