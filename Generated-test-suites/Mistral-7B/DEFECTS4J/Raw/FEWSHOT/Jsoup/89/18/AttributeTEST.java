package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testNewAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testNewAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.getParent());
        assertTrue(parent.containsKey("key"));
        assertEquals("value", parent.get("key"));
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        Attributes parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertFalse(parent.containsKey("key"));
        assertNull(parent.get("key"));
        assertTrue(parent.containsKey("newKey"));
        assertEquals("value", parent.get("newKey"));
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());

        Attributes parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", parent.get("key"));
        assertNull(parent.get("newValue"));
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        Attributes parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        int hashcode1 = attribute1.hashCode();
        int hashcode2 = attribute2.hashCode();
        int hashcode3 = attribute3.hashCode();

        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.getParent());
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attribute = new Attribute("booleanAttribute", "");
        assertTrue(Attribute.isBooleanAttribute("booleanAttribute"));
        assertTrue(attribute.isDataAttribute());

        attribute = new Attribute("notBooleanAttribute", "");
        assertFalse(Attribute.isBooleanAttribute("notBooleanAttribute"));
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);

        Attribute attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        attribute = new Attribute("notBooleanAttribute", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }
}