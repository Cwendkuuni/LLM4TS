package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
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
        Attribute attribute = new Attribute("testKey", "testValue");
        String oldValue = attribute.setValue("newValue");
        assertEquals("testValue", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String expectedHtml = "testKey=\"testValue\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("testKey", "test%20value");
        assertEquals("testKey", attribute.getKey());
        assertEquals("test value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-test", "testValue");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("testKey", "testKey", null);
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("testKey", "testValue");
        Attribute attribute2 = new Attribute("testKey", "testValue");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("testKey", "testValue");
        int expectedHashCode = 31 * "testKey".hashCode() + "testValue".hashCode();
        assertEquals(expectedHashCode, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}