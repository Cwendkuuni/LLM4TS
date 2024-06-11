package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.nodes.Attributes.Named;
import org.jsoup.nodes.Attributes.NotFound;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attr = new Attribute("key", "value");
        String expectedKey = "key";
        String expectedValue = "value";
        assertEquals(expectedKey, attr.getKey());
        assertEquals(expectedValue, attr.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("key", "value", attributes);
        String expectedKey = "key";
        String expectedValue = "value";
        assertEquals(expectedKey, attr.getKey());
        assertEquals(expectedValue, attr.getValue());
        assertEquals(attributes, attr.parent);
    }

    @Test
    public void testGetKeyMethod() {
        Attribute attr = new Attribute("key", "value");
        String expectedKey = "key";
        assertEquals(expectedKey, attr.getKey());
    }

    @Test
    public void testSetKeyMethod() {
        Attribute attr = new Attribute("key1", "value1");
        attr.setKey("key2");
        String expectedKey = "key2";
        assertEquals(expectedKey, attr.getKey());
    }

    @Test
    public void testGetValueMethod() {
        Attribute attr = new Attribute("key", "value");
        String expectedValue = "value";
        assertEquals(expectedValue, attr.getValue());
    }

    @Test
    public void testSetValueMethod() {
        Attribute attr = new Attribute("key", "value1");
        attr.setValue("value2");
        String expectedValue = "value2";
        assertEquals(expectedValue, attr.getValue());
    }

    @Test
    public void testHtmlMethodWithAppendable() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, null);
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlMethodWithDocumentOutputSettings() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document document = new Document("");
        String expectedHtml = "key=\"value\"";
        attr.html(document.outputSettings());
        Elements elements = new Elements(document.select("*"));
        Node node = elements.first();
        String actualHtml = node.toString();
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testToStringMethod() {
        Attribute attr = new Attribute("key", "value");
        String expectedString = "key=\"value\"";
        assertEquals(expectedString, attr.toString());
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        String expectedKey = "key";
        String expectedValue = "value";
        assertEquals(expectedKey, attr.getKey());
        assertEquals(expectedValue, attr.getValue());
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attr = new Attribute("key1", "value1");
        boolean expectedResult = false;
        assertTrue(expectedResult != attr.isDataAttribute());

        attr = new Attribute("data-key1", "value1");
        expectedResult = true;
        assertTrue(expectedResult == attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() {
        Attribute attr = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = false;
        assertFalse(expectedResult == attr.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        expectedResult = attr.key.equalsIgnoreCase("value") || attr.val == null;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));

        attr.key = "data-key";
        expectedResult = true;
        assertFalse(expectedResult == attr.shouldCollapseAttribute(outputSettings));

        attr.val = "value";
        expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));

        attr.key = "checked";
        attr.val = "";
        expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));

        attr.key = "checked";
        attr.val = null;
        expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document document = new Document("");
        attr.html(document.outputSettings(), new IOException("IOException message"));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        int expectedResult1 = attr1.hashCode();
        int expectedResult2 = attr2.hashCode();
        int expectedResult3 = attr3.hashCode();
        assertNotEquals(expectedResult1, expectedResult2);
        assertEquals(expectedResult1, expectedResult3);
    }

    @Test
    public void testCloneMethod() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Attribute attr = new Attribute("key", "value");
        boolean expectedResult = false;
        assertFalse(expectedResult == attr.isBooleanAttribute());

        attr.setKey("checked");
        expectedResult = true;
        assertTrue(expectedResult == attr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeStaticMethod() {
        boolean expectedResult = false;
        assertFalse(expectedResult == Attribute.isBooleanAttribute("key"));

        String booleanAttribute = "checked";
        expectedResult = true;
        assertTrue(expectedResult == Attribute.isBooleanAttribute(booleanAttribute));
    }

    @Test
    public void testShouldCollapseAttributeWithNullVal() {
        Attribute attr = new Attribute("key", null);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithEmptyVal() {
        Attribute attr = new Attribute("key", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithEmptyKey() {
        Attribute attr = new Attribute("", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithNullKey() {
        Attribute attr = new Attribute(null, "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanKeyAndEmptyVal() {
        Attribute attr = new Attribute("checked", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanKeyAndNullVal() {
        Attribute attr = new Attribute("checked", null);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanKeyAndValEqualsKey() {
        Attribute attr = new Attribute("checked", "checked");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanKeyAndValNotEqualsKey() {
        Attribute attr = new Attribute("checked", "unchecked");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = false;
        assertFalse(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanKeyAndValIsEmpty() {
        Attribute attr = new Attribute("checked", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanKeyAndValIsNotNull() {
        Attribute attr = new Attribute("checked", "checked");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanKeyAndValIsNull() {
        Attribute attr = new Attribute("checked", null);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedResult = true;
        assertTrue(expectedResult == attr.shouldCollapseAttribute(outputSettings));
    }
}