package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Attribute class.
 */
public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("test", "testValue");
        Assert.assertEquals("test", attribute.getKey());
        Assert.assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("test", "testValue", attributes);
        Assert.assertEquals("test", attribute.getKey());
        Assert.assertEquals("testValue", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("test", "testValue");
        attribute.setKey("newTest");
        Assert.assertEquals("newTest", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("test", "testValue");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("test", "testValue");
        String newValue = "newTestValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals("testValue", oldValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("test", "testValue");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("test", "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("test=\"testValue\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("test", "testValue");
        Assert.assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-test", "testValue");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("test", "testValue");
        Assert.assertFalse(attribute.shouldCollapseAttribute(out));

        attribute = new Attribute("test", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));

        attribute = new Attribute("checked", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));

        out.syntax(Document.OutputSettings.Syntax.xml);
        attribute = new Attribute("checked", "");
        Assert.assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("test", "testValue");
        Assert.assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("checked", null);
        Assert.assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("allowfullscreen", null);
        Assert.assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testClone() {
        Attribute original = new Attribute("test", "testValue");
        Attribute cloned = original.clone();
        Assert.assertEquals(original.getKey(), cloned.getKey());
        Assert.assertEquals(original.getValue(), cloned.getValue());
        Assert.assertNotSame(original, cloned);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attribute attribute = new Attribute("test", "testValue");
        attribute.clone();
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test1", "value1");
        Attribute attribute3 = new Attribute("test2", "value1");
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testEqualsSameObject() {
        Attribute attribute = new Attribute("test", "testValue");
        Assert.assertTrue(attribute.equals(attribute));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test1", "value1");
        Attribute attribute3 = new Attribute("test2", "value1");
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);
        attributes.add(attribute3);
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = attributes.get(i);
            Assert.assertEquals(i, attribute.hashCode());
        }
    }
}