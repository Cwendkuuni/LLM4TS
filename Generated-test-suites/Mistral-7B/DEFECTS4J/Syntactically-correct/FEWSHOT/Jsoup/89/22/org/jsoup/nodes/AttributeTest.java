package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "value=\"encoded value\"");
        assertEquals("key", attr.getKey());
        assertEquals("encoded value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());

        Attribute nonDataAttribute = new Attribute("key", "value");
        assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = Document.OutputSettings.syntaxHtml();
        Attribute booleanAttributeWithEmptyValue = new Attribute("checked", "");
        assertTrue(booleanAttributeWithEmptyValue.shouldCollapseAttribute(outputSettings));

        Attribute booleanAttributeWithSameValue = new Attribute("checked", "checked");
        assertTrue(booleanAttributeWithSameValue.shouldCollapseAttribute(outputSettings));

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));

        Attribute nonBooleanAttributeWithEmptyValue = new Attribute("key", "");
        assertFalse(nonBooleanAttributeWithEmptyValue.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEqualsAndHashcode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertTrue(attr1.equals(attr3));
        assertTrue(attr1.hashCode() == attr3.hashCode());

        assertFalse(attr1.equals(attr2));
        assertFalse(attr1.hashCode() == attr2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }

    @Test
    public void testKeyValidation() {
        String invalidKey = null;
        try {
            new Attribute(invalidKey, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        String emptyKey = "";
        try {
            new Attribute(emptyKey, "value");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        String whitespaceKey = " \t\r\n key";
        try {
            new Attribute(whitespaceKey, "value");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidCharacterKey = "key#";
        try {
            new Attribute(invalidCharacterKey, "value");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attr = new Attribute("key", "value", parentAttributes);

        assertEquals(parentAttributes, attr.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        String newKey = "newKey";

        attr.setKey(newKey);
        assertEquals(newKey, attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String newValue = "newValue";

        attr.setValue(newValue);
        assertEquals(newValue, attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document document = new Document("");

        attr.html(sb, document.outputSettings());

        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithEmptyValue() throws IOException {
        Attribute attr = new Attribute("key", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document document = new Document("");

        attr.html(sb, document.outputSettings());

        String expectedHtml = "key";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithBooleanAttribute() throws IOException {
        Attribute attr = new Attribute("checked", null);
        StringBuilder sb = StringUtil.borrowBuilder();
        Document document = new Document("");

        attr.html(sb, document.outputSettings());

        String expectedHtml = "checked";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithDataAttribute() throws IOException {
        Attribute attr = new Attribute("data-key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document document = new Document("");

        attr.html(sb, document.outputSettings());

        String expectedHtml = "data-key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithBooleanAttributeAndEmptyValue() throws IOException {
        Attribute attr = new Attribute("checked", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document document = new Document("");

        attr.html(sb, document.outputSettings());

        String expectedHtml = "checked";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithBooleanAttributeAndSameValue() throws IOException {
        Attribute attr = new Attribute("checked", "checked");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document document = new Document("");

        attr.html(sb, document.outputSettings());

        String expectedHtml = "";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        String expectedHtml = "key=\"value\"";

        assertEquals(expectedHtml, attr.toString());
    }
}