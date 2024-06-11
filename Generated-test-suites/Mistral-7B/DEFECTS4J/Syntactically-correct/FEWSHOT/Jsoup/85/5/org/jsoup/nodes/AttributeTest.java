package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", new Attributes(new Attributes.Attribute[]{}));

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());

        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertSame(new Attributes(new Attributes.Attribute[]{}), attributeWithParent.getParent());
    }

    @Test
    public void testKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");

        assertEquals("newKey", attribute.getKey());
        assertNotEquals("key", attribute.getKey());
    }

    @Test
    public void testValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");

        assertEquals("newValue", attribute.getValue());
        assertNotEquals("value", attribute.getValue());
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb);

        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "key", "value"));
        assertTrue(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "key", ""));
        assertTrue(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "key", "key"));
        assertFalse(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "key", "newValue"));
        assertFalse(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "notAboolean", "value"));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute(""));
        assertFalse(Attribute.isBooleanAttribute("randomKey"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute1.hashCode() == attribute2.hashCode());

        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.hashCode() == attribute3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals("key", clonedAttribute.getKey());
        assertEquals("value", clonedAttribute.getValue());
        assertNull(clonedAttribute.getParent());
    }
}