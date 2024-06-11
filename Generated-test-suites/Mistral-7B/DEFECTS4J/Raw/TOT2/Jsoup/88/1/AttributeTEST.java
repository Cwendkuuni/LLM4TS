package org.junit.platform.commons.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Assert.assertEquals("testKey", attribute.getKey());
        Assert.assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("testValue", oldValue);
    }

    @Test(expected = SerializationException.class)
    public void testEdgeCasesOfHtmlWithNullOutputSettings() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.html(null, new Document(""));
    }

    @Test
    public void testEdgeCasesOfHtmlWithEmptyKeyAndValue() {
        Attribute attribute = new Attribute("", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            attribute.html(sb, new Document("").outputSettings());
        } catch (IOException exception) {
            throw new SerializationException(exception);
        }
        Assert.assertEquals("", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testEdgeCasesOfHtmlWithNullKeyAndNonEmptyValue() {
        Attribute attribute = new Attribute(null, "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            attribute.html(sb, new Document("").outputSettings());
        } catch (IOException exception) {
            throw new SerializationException(exception);
        }
        Assert.assertEquals("testValue", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testEdgeCasesOfHtmlWithNonEmptyKeyAndEmptyValue() {
        Attribute attribute = new Attribute("testKey", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            attribute.html(sb, new Document("").outputSettings());
        } catch (IOException exception) {
            throw new SerializationException(exception);
        }
        Assert.assertEquals("testKey", StringUtil.releaseBuilder(sb));
    }

    @Test(expected = SerializationException.class)
    public void testExceptionHandlingOfHtmlWithNullAccumulator() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.html(null, new Document("").outputSettings());
    }

    @Test(expected = SerializationException.class)
    public void testExceptionHandlingOfSetValueWithNullValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue(null);
    }

    // Testing other methods with edge cases
    // ...
}