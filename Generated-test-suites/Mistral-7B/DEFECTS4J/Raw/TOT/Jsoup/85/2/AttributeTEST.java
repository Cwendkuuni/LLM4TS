package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Attribute class.
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        // Create Attribute with valid key and value
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        // Create Attribute with valid key, value and parent
        Attribute attributeParent = new Attributes();
        Attribute attributeWithParent = new Attribute("key", "value", attributeParent);
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertEquals(attributeWithParent, attributeParent.get("key"));

        // Test key and value setting
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());

        // Test key and value getting after setting
        String oldKey = attribute.getKey();
        String oldValue = attribute.getValue();

        attribute.setKey("newKey2");
        assertEquals("newKey2", attribute.getKey());
        assertEquals("newValue", oldKey);

        attribute.setValue("newValue2");
        assertEquals("newValue2", attribute.getValue());
        assertEquals("newValue", oldValue);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));

        Attribute booleanAttribute = new Attribute("checked", null);
        booleanAttribute.html(sb, new Document("").outputSettings());
        assertEquals("checked", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        String encodedKey = "key=\"data-my-data\"";
        Attribute attribute = Attribute.createFromEncoded(encodedKey.substring(1, encodedKey.length() - 1), encodedKey);
        assertEquals("data-my-data", attribute.getKey());
        assertEquals(null, attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
    }
}