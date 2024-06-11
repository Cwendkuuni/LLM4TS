package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.PutAttribute;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.jsoup.serializer.OutputSettings.Syntax;

import java.util.Arrays;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;
 private Element element;
 private Document document;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 attributes = new Attributes();
 attributes.put("key", attribute);
 element = new Element("html");
 document = new Document();
 document.body().appendElement(element);
 }

 @Test
 public void testAttributeConstructionWithKeyAndValue() {
 Attribute expectedAttribute = new Attribute("key", "value");
 assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testAttributeConstructionWithKeyValueAndParent() {
 Attribute expectedAttribute = new Attribute("key", "value", attributes);
 assertEquals(expectedAttribute, attribute);
 }

 @Test
 public void testSettingKey() {
 attribute.setKey("newKey");
 Attribute newAttribute = new Attribute("newKey", "value", attributes);
 assertEquals(newAttribute, attribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullKey() {
 attribute.setKey(null);
 fail("Setting a null key should throw an IllegalArgumentException");
 }

 @Test
 public void testSettingEmptyKey() {
 attribute.setKey("");
 Attribute emptyAttribute = new Attribute("", "value", attributes);
 assertEquals(emptyAttribute, attribute);
 }

 @Test
 public void testSettingKeyWithExistingKeyInParent() {
 Attribute existingAttribute = attributes.get("key");
 attribute.setKey("newKey");
 Attribute newAttribute = new Attribute("newKey", "value", attributes);
 assertEquals(newAttribute, attribute);
 assertEquals(existingAttribute.getKey(), "key");
 assertEquals(newAttribute.getKey(), "newKey");
 }

 @Test
 public void testGettingKey() {
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testGettingValue() {
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSettingValue() {
 String oldValue = attribute.getValue();
 attribute.setValue("newValue");
 Attribute newAttribute = new Attribute("key", "newValue", attributes);
 assertEquals(newAttribute, attribute);
 assertEquals(oldValue, "value");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullValue() {
 attribute.setValue(null);
 fail("Setting a null value should throw an IllegalArgumentException");
 }

 @Test
 public void testGettingHtml() throws IOException {
 StringBuilder expectedSb = StringUtil.borrowBuilder();
 Attribute.html("key", "value", expectedSb, document.outputSettings());
 assertEquals("key=\"value\"", expectedSb.toString());
 }

 @Test
 public void testGettingHtmlWithEmptyValueOrSameAsKey() throws IOException {
 StringBuilder expectedSb = StringUtil.borrowBuilder();
 Attribute.html("key", "", expectedSb, document.outputSettings());
 assertEquals("key", expectedSb.toString());
 }

 @Test
 public void testGettingHtmlWithBooleanAttribute() throws IOException {
 StringBuilder expectedSb = StringUtil.borrowBuilder();
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 Attribute.html("checked", null, expectedSb, document.outputSettings());
 assertEquals("checked", expectedSb.toString());
 }

 @Test
 public void testGettingHtmlWithBooleanAttributeAndValue() throws IOException {
 StringBuilder expectedSb = StringUtil.borrowBuilder();
 Attribute booleanAttribute = new Attribute("checked", "true", attributes);
 Attribute.html("checked", "true", expectedSb, document.outputSettings());
 assertEquals("checked=\"true\"", expectedSb.toString());
 }

 @Test
 public void testToString() {
 String expectedString = "key=\"value\"";
 assertEquals(expectedString, attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedValue = "key=\"value\"";
 Attribute expectedAttribute = new Attribute("key", "value");
 Attribute newAttribute = Attribute.createFromEncoded("key", encodedValue);
 assertEquals(expectedAttribute, newAttribute);
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithBooleanAttributeKey() {
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithDataAttributeKey() {
 Attribute dataAttribute = new Attribute("data-key", "value", attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlThrowsSerializationExceptionWhenIOExceptionOccurs() throws IOException, SerializationException {
 StringBuilder expectedSb = StringUtil.borrowBuilder();
 throw new IOException("IOException");
 Attribute.html("key", "value", expectedSb, document.outputSettings());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlThrowsSerializationExceptionWhenSerializationExceptionOccurs() throws SerializationException {
 StringBuilder expectedSb = StringUtil.borrowBuilder();
 throw new SerializationException("SerializationException");
 Attribute.html("key", "value", expectedSb, document.outputSettings());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 assertTrue(attribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyValue() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 assertTrue(attribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithValueSameAsKey() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 assertTrue(attribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithValueDifferentFromKey() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute newAttribute = new Attribute("newKey", "value", attributes);
 assertFalse(newAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttributeAndValueDifferentFromKey() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute booleanAttribute = new Attribute("checked", "true", attributes);
 assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndBooleanAttribute() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute dataBooleanAttribute = new Attribute("data-checked", null, attributes);
 assertTrue(dataBooleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndBooleanAttributeAndValue() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute dataBooleanAttribute = new Attribute("data-checked", "true", attributes);
 assertTrue(dataBooleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndValueDifferentFromKey() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute newAttribute = new Attribute("newKey", "value", attributes);
 assertFalse(newAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndValueDifferentFromKeyAndOutputSettingsXml() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.xml;
 Attribute newAttribute = new Attribute("newKey", "value", attributes);
 assertFalse(newAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndValueDifferentFromKeyAndOutputSettingsHtmlWithTrueBooleanAttribute() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute booleanAttribute = new Attribute("data-checked", "true", attributes);
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndValueDifferentFromKeyAndOutputSettingsHtmlWithFalseBooleanAttribute() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute booleanAttribute = new Attribute("data-checked", "false", attributes);
 assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndValueDifferentFromKeyAndOutputSettingsHtmlWithBooleanValue() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute booleanAttribute = new Attribute("data-checked", "true", attributes);
 assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndValueDifferentFromKeyAndOutputSettingsHtmlWithBooleanValueAndTrueValue() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute booleanAttribute = new Attribute("data-checked", "true", attributes);
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }

 @Test
 public void testShouldCollapseAttributeWithPrefixAndValueDifferentFromKeyAndOutputSettingsHtmlWithBooleanValueAndFalseValue() {
 Syntax outputSettingsSyntax = Document.OutputSettings.Syntax.html;
 Attribute booleanAttribute = new Attribute("data-checked", "false", attributes);
 assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettingsSyntax));
 }
}