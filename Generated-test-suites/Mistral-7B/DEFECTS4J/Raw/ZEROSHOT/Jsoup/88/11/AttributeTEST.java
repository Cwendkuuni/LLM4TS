package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes.NotFound;

import java.io.IOException;
import java.util.Arrays;

public class AttributeTest {

    @org.junit.Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertEquals(0, attrWithParent.parent.size());
    }

    @org.junit.Test
    public void testAttributeSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        assertEquals("newKey", attrWithParent.getKey());
        assertEquals(0, attrWithParent.parent.size());
    }

    @org.junit.Test
    public void testAttributeSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals("value", oldValue);

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        String oldValueParent = attrWithParent.parent.get("key");
        attrWithParent.setValue("newValue");
        assertEquals("newValue", attrWithParent.getValue());
        assertEquals("value", oldValueParent);
    }

    @org.junit.Test
    public void testHtmlMethod() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, null);
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        sb.setLength(0);
        attrWithParent.html(sb, null);
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @org.junit.Test
    public void testToStringMethod() {
        Attribute attr = new Attribute("key", "value");
        String expectedToString = "key=\"value\"";
        assertEquals(expectedToString, attr.toString());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals(expectedToString, attrWithParent.toString());
    }

    @org.junit.Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("unencodedKey", "encodedValue=\"value\"");
        assertEquals("unencodedKey", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @org.junit.Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data-key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @org.junit.Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertFalse(Attribute.shouldCollapseAttribute("booleanAttribute", null, out));
        assertFalse(Attribute.shouldCollapseAttribute("booleanAttribute", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("booleanAttribute", "true", out));
        assertFalse(Attribute.shouldCollapseAttribute("booleanAttribute", "false", out));
    }

    @org.junit.Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());
    }

    @org.junit.Test
    public void testEqualsMethod() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @org.junit.Test
    public void testHashCodeMethod() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @org.junit.Test
    public void testCloneMethod() {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();

        assertEquals(attr.getKey(), clone.getKey());
        assertEquals(attr.getValue(), clone.getValue());
    }
}