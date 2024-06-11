package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertSame(parentAttributes, attribute.parent);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value");
        attribute.setKey("key2");
        assertEquals("key2", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value1");
        attribute.setValue("value2");
        assertEquals("value2", attribute.getValue());
        assertEquals("value1", attribute.parent.get("key"));
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        expected.append("key=\"value\"");
        String result = attribute.html();
        assertEquals(expected.toString(), result);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue=\"encoded value\"");
        assertEquals("unencodedKey", attribute.getKey());
        assertEquals("encoded value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
        Attribute attribute1 = new Attribute("key", "value");
        assertFalse(attribute1.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotSame(attribute.parent, clonedAttribute.parent);
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.isBooleanAttribute());
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }
}