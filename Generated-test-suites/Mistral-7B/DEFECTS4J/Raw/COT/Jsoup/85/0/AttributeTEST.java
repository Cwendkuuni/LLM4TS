package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreationWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assertions.assertEquals("key", attribute.getKey());
        Assertions.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assertions.assertEquals("key", attribute.getKey());
        Assertions.assertEquals("value", attribute.getValue());
        Assertions.assertNotNull(attribute.parent);
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assertions.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.getValue();
        attribute.setValue("newValue");
        Assertions.assertEquals("newValue", attribute.getValue());
        Assertions.assertNotEquals(oldValue, attribute.getValue());
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String htmlOutput = StringUtil.releaseBuilder(sb);
        Assertions.assertEquals("key=\"value\"", htmlOutput);
    }

    @Test
    public void testToStringMethod() {
        Attribute attribute = new Attribute("key", "value");
        String toStringOutput = attribute.toString();
        Assertions.assertEquals("key=\"value\"", toStringOutput);
    }

    @Test
    public void testCreateFromEncodedMethod() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        Assertions.assertEquals(unencodedKey, attribute.getKey());
        Assertions.assertEquals(encodedValue, attribute.getValue());
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("data-test", "test");
        Assertions.assertTrue(attribute.isDataAttribute());

        Attribute nonDataAttribute = new Attribute("key", "value");
        Assertions.assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "");
        Assertions.assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        attribute = new Attribute("key", "value");
        Assertions.assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        attribute = new Attribute("booleanAttribute", "");
        Assertions.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttributeDeprecatedMethod() {
        Attribute attribute = new Attribute("booleanAttribute", "");
        Assertions.assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("key", "value");
        Assertions.assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Attribute attribute = new Attribute("booleanAttribute", "");
        Assertions.assertTrue(Attribute.isBooleanAttribute(attribute.getKey()));

        attribute = new Attribute("key", "value");
        Assertions.assertFalse(Attribute.isBooleanAttribute(attribute.getKey()));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        Assertions.assertTrue(attribute1.equals(attribute2));
        Assertions.assertFalse(attribute1.equals(attribute3));
        Assertions.assertFalse(attribute3.equals(attribute1));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        Assertions.assertEquals(attribute1.hashCode(), attribute2.hashCode());
        Assertions.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
        Assertions.assertNotEquals(attribute3.hashCode(), attribute1.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        Assertions.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assertions.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

    @Test
    public void testExceptionWhenNullKey() {
        Assertions.assertThrows(NullPointerException.class, () -> new Attribute(null, "value"));
    }

    @Test
    public void testExceptionWhenEmptyKey() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Attribute("", "value"));
    }

    @Test
    public void testExceptionWhenNullParent() {
        Assertions.assertThrows(NullPointerException.class, () -> new Attribute("key", "value", null));
    }

    @Test
    public void testExceptionWhenNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> new Attribute("key", null));
    }

    @Test
    public void testExceptionWhenNullDocumentOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        Assertions.assertThrows(NullPointerException.class, () -> attribute.html(null, new Document.OutputSettings()));
    }
}