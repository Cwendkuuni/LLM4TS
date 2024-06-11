package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeConstructorWithKeyAndValue() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeConstructorWithKeyValueAndParent() {
        String key = "key";
        String value = "value";
        Attributes parent = new Attributes();

        Attribute attribute = new Attribute(key, value, parent);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertNotNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        String key = "oldKey";
        String value = "value";
        Attribute attribute = new Attribute(key, value, null);

        String newKey = "newKey";
        attribute.setKey(newKey);

        Assert.assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSetKeyNullKey() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, null);

        attribute.setKey(null);
    }

    @Test(expected = AssertionError.class)
    public void testSetKeyEmptyKey() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, null);

        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        String key = "key";
        String oldValue = "oldValue";
        String newValue = "newValue";
        Attribute attribute = new Attribute(key, oldValue, null);

        String value = attribute.setValue(newValue);

        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals(oldValue, value);
    }

    @Test(expected = AssertionError.class)
    public void testSetValueNullValue() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, null);

        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, null);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expectedHtml = String.format("%s=\"%s\"", key, value);
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue";

        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        Assert.assertEquals(unencodedKey, attribute.getKey());
        Assert.assertEquals(Entities.unescape(encodedValue, true), attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-key";
        Attribute attribute = new Attribute(key, null, null);

        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();

        String key1 = "key1";
        String value1 = "value1";

        Attribute attribute1 = new Attribute(key1, value1, null);
        Assert.assertFalse(attribute1.shouldCollapseAttribute(out));

        String key2 = "checked";
        Attribute attribute2 = new Attribute(key2, null, null);
        Assert.assertTrue(attribute2.shouldCollapseAttribute(out));

        String key3 = "key3";
        Attribute attribute3 = new Attribute(key3, value1, null);
        Assert.assertFalse(attribute3.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        String key1 = "booleanAttribute";
        Attribute attribute1 = new Attribute(key1, null, null);
        Assert.assertTrue(attribute1.isBooleanAttribute());

        String key2 = "notBooleanAttribute";
        Attribute attribute2 = new Attribute(key2, null, null);
        Assert.assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String value1 = "value1";
        Attribute attribute1 = new Attribute(key1, value1, null);

        String key2 = "key2";
        String value2 = "value2";
        Attribute attribute2 = new Attribute(key2, value2, null);

        Attribute attribute3 = new Attribute(key1, value1, null);

        Attribute attribute4 = new Attribute(key1, value1, null);
        Attribute attribute5 = new Attribute(key1, value1, null);
        Attribute attribute6 = new Attribute(key1, value1, null);

        Attribute attribute7 = new Attribute(key1, value2, null);

        Attribute attribute8 = new Attribute(key2, value1, null);

        Assert.assertEquals(attribute1, attribute3);
        Assert.assertEquals(attribute1, attribute4);
        Assert.assertEquals(attribute1, attribute5);
        Assert.assertEquals(attribute1, attribute6);

        Assert.assertNotEquals(attribute1, attribute2);
        Assert.assertNotEquals(attribute1, attribute7);
        Assert.assertNotEquals(attribute1, attribute8);
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String value1 = "value1";
        Attribute attribute1 = new Attribute(key1, value1, null);

        String key2 = "key2";
        String value2 = "value2";
        Attribute attribute2 = new Attribute(key2, value2, null);

        Attribute attribute3 = new Attribute(key1, value1, null);

        Assert.assertEquals(attribute1.hashCode(), attribute3.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, null);

        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(attribute, clonedAttribute);
        Assert.assertNotSame(attribute, clonedAttribute);
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}