package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    private Attribute attr;

    @Before
    public void setUp() {
        this.attr = new Attribute("testKey", "testValue");
    }

    @Test
    public void testConstructorWithKeyAndValue() {
        Assert.assertEquals("testKey", attr.getKey());
        Assert.assertEquals("testValue", attr.getValue());
    }

    @Test
    public void testConstructorWithKeyAndValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attrWithParent = new Attribute("testKey", "testValue", parent);
        parent.put(attrWithParent.getKey(), attrWithParent.getValue());

        Assert.assertEquals("testKey", attrWithParent.getKey());
        Assert.assertEquals("testValue", attrWithParent.getValue());
        Assert.assertEquals(1, parent.size());
    }

    @Test
    public void testSetKey() {
        attr.setKey("newTestKey");
        Assert.assertEquals("newTestKey", attr.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSetKeyNull() {
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        String oldValue = attr.getValue();
        attr.setValue("newTestValue");
        Assert.assertEquals("newTestValue", attr.getValue());
        Assert.assertNotEquals(oldValue, attr.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testSetValueNull() {
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String htmlOutput = StringUtil.releaseBuilder(sb);
        Assert.assertEquals("testKey=\"testValue\"", htmlOutput);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlIOException() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        throw new IOException();
    }

    @Test
    public void testCreateFromEncoded() {
        String encodedKey = "testKey=testValue";
        Attribute createdAttr = Attribute.createFromEncoded(encodedKey.split("=")[0], encodedKey.substring(encodedKey.indexOf("=") + 1));
        Assert.assertEquals("testKey", createdAttr.getKey());
        Assert.assertEquals("testValue", createdAttr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Assert.assertFalse(attr.isDataAttribute());
        Attribute dataAttr = new Attribute("data-test", null);
        Assert.assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertTrue(attr.shouldCollapseAttribute(outputSettings));
        Attribute booleanAttr = new Attribute("checked", null);
        Assert.assertTrue(booleanAttr.shouldCollapseAttribute(outputSettings));
        Attribute nonBooleanAttr = new Attribute("test", "value");
        Assert.assertFalse(nonBooleanAttr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertFalse(attr.isBooleanAttribute());
        Attribute booleanAttr = new Attribute("checked", null);
        Assert.assertTrue(booleanAttr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithBooleanValue() {
        Attribute booleanAttr = new Attribute("checked", "true");
        Assert.assertTrue(booleanAttr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute otherAttr = new Attribute("testKey", "testValue");
        Attribute identicalAttr = attr;
        Attribute differentAttr1 = new Attribute("testKey2", "testValue");
        Attribute differentAttr2 = new Attribute("testKey", "testValue2");
        Attribute differentAttr3 = new Attribute("testKey", null);

        Assert.assertTrue(attr.equals(identicalAttr));
        Assert.assertFalse(attr.equals(otherAttr));
        Assert.assertFalse(attr.equals(differentAttr1));
        Assert.assertFalse(attr.equals(differentAttr2));
        Assert.assertFalse(attr.equals(differentAttr3));
    }

    @Test
    public void testHashCode() {
        Attribute otherAttr = new Attribute("testKey", "testValue");
        Attribute identicalAttr = attr;
        Attribute differentAttr1 = new Attribute("testKey2", "testValue");
        Attribute differentAttr2 = new Attribute("testKey", "testValue2");
        Attribute differentAttr3 = new Attribute("testKey", null);

        Assert.assertEquals(attr.hashCode(), identicalAttr.hashCode());
        Assert.assertNotEquals(attr.hashCode(), otherAttr.hashCode());
        Assert.assertNotEquals(attr.hashCode(), differentAttr1.hashCode());
        Assert.assertNotEquals(attr.hashCode(), differentAttr2.hashCode());
        Assert.assertNotEquals(attr.hashCode(), differentAttr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttr = attr.clone();
        Assert.assertEquals(attr, clonedAttr);
        Assert.assertEquals(attr.getKey(), clonedAttr.getKey());
        Assert.assertEquals(attr.getValue(), clonedAttr.getValue());
    }

    @After
    public void tearDown() {
        StringUtil.cleanUpBuilder();
    }
}