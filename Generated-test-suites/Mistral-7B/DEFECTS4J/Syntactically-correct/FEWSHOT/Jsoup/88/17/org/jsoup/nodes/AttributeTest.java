package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes.OutputSettings;
import org.jsoup.nodes.Attributes.NotFound;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.parent);
    }

    @Test
    public void testAttributeCreationWithEmptyKey() {
        try {
            new Attribute("", "value");
            fail("Expected an exception when creating an Attribute with an empty key");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Attribute("key", "");
            fail("Expected an exception when creating an Attribute with an empty value");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Attribute("", "");
            fail("Expected an exception when creating an Attribute with both an empty key and value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testAttributeKeySetterWithNullKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
        assertNull(attr.getKey());
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testAttributeValueSetterWithNullValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
        assertNull(attr.getValue());
    }

    @Test
    public void testGetHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));

        Attribute booleanAttr = new Attribute("checked", null);
        expectedHtml = "checked";
        sb = StringUtil.borrowBuilder();
        booleanAttr.html(sb, new Document("").outputSettings());
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testPutHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document doc = new Document("");
        attr.put(doc, null, "key", "value");
        Element element = doc.body();
        assertEquals("key=\"value\"", element.htmlTags().get(0).attributes().get(0).html());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document.OutputSettings settings = new Document.OutputSettings();
        assertTrue(attr.shouldCollapseAttribute(settings));

        Attribute booleanAttr = new Attribute("checked", null);
        settings.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(booleanAttr.shouldCollapseAttribute(settings));

        Attribute booleanAttrWithValue = new Attribute("checked", "checked");
        settings.syntax(Document.OutputSettings.Syntax.html);
        assertFalse(booleanAttrWithValue.shouldCollapseAttribute(settings));

        Attribute nonBooleanAttr = new Attribute("nonBoolean", "nonBoolean");
        settings.syntax(Document.OutputSettings.Syntax.html);
        assertFalse(nonBooleanAttr.shouldCollapseAttribute(settings));

        Attribute nonBooleanAttrWithValue = new Attribute("nonBoolean", "nonBooleanValue");
        settings.syntax(Document.OutputSettings.Syntax.html);
        assertFalse(nonBooleanAttrWithValue.shouldCollapseAttribute(settings));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("disabled"));
        assertTrue(Attribute.isBooleanAttribute("multiple"));
        assertTrue(Attribute.isBooleanAttribute("required"));
        assertFalse(Attribute.isBooleanAttribute("id"));
        assertFalse(Attribute.isBooleanAttribute("class"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        Attribute attr4 = new Attribute("key1", null);

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr3));
        assertFalse(attr1.equals(attr2));
        assertFalse(attr1.equals(attr4));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        Attribute attr4 = new Attribute("key1", null);

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        int hashCode4 = attr4.hashCode();

        assertEquals(hashCode1, hashCode3); // Same key and value
        assertNotSame(hashCode1, hashCode2); // Different keys or values
        assertNotSame(hashCode1, hashCode4); // Different keys and null values
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);

        attr.setValue("newValue");
        assertNotSame(attr, clonedAttr);
    }

    @Test
    public void testIndexOfKey() {
        Attributes attributes = new Attributes();
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        attributes.put("key1", attr1);
        attributes.put("key2", attr2);

        int index = attributes.indexOfKey("key1");
        assertEquals(0, index);

        index = attributes.indexOfKey("key3");
        assertEquals(-1, index);
    }

    @Test
    public void testCloneWithParent() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document doc = new Document("");
        attr.put(doc, null, "key", "value");
        Attribute clonedAttr = attr.clone();
        Element clonedElement = doc.createElement("div");
        clonedAttr.put(clonedElement, null, "key", "value");
        assertEquals(attr.html(), clonedAttr.html());
    }

    @Test
    public void testCloneWithParentAndValueChange() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document doc = new Document("");
        attr.put(doc, null, "key", "value");
        Attribute clonedAttr = attr.clone();
        Element clonedElement = doc.createElement("div");
        clonedAttr.setValue("newValue");
        clonedAttr.put(clonedElement, null, "key", "newValue");
        assertNotEquals(attr.html(), clonedAttr.html());
    }

    @Test
    public void testCloneWithParentAndKeyChange() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document doc = new Document("");
        attr.put(doc, null, "key", "value");
        Attribute clonedAttr = attr.clone();
        Element clonedElement = doc.createElement("div");
        clonedAttr.setKey("newKey");
        clonedAttr.put(clonedElement, null, "newKey", "value");
        assertNotEquals(attr.getKey(), clonedAttr.getKey());
        assertNotEquals(attr.html(), clonedAttr.html());
    }
}