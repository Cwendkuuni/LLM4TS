package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeTest {

 private Attribute attr;
 private Attributes attrs;
 private Document doc;

 @Before
 public void setUp() {
 attr = new Attribute("key", "value");
 attrs = new Attributes();
 doc = new Document();
 }

 @Test
 public void testAttributeConstructionWithKeyAndValue() {
 Attribute expected = new Attribute("key", "value");
 assertEquals(expected, attr);
 }

 @Test
 public void testAttributeConstructionWithKeyValueAndParent() {
 Attribute expected = new Attribute("key", "value", attrs);
 assertEquals(expected, attr);
 }

 @Test
 public void testSettingKey() {
 String newKey = "newKey";
 attr.setKey(newKey);
 assertEquals(newKey, attr.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 attr.setKey(null);
 }

 @Test
 public void testSettingEmptyKey() {
 attr.setKey("");
 assertEquals("", attr.getKey());
 }

 @Test
 public void testSettingKeyAndUpdatingParent() {
 String newKey = "newKey";
 attr.setKey(newKey);
 int index = attrs.indexOfKey(newKey);
 assertEquals(newKey, attrs.keyAt(index));
 }

 @Test
 public void testGetValue() {
 assertEquals("value", attr.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 attr.setValue(null);
 }

 @Test(expected = SerializationException.class)
 public void testSettingHtmlStringAsValue() {
 attr.setValue("<html></html>");
 }

 @Test
 public void testSettingValueAndUpdatingParent() {
 String newValue = "newValue";
 attr.setValue(newValue);
 assertEquals(newValue, attrs.get("key"));
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder expectedSb = new StringBuilder();
 Attribute.html("key", "value", expectedSb, doc.outputSettings());
 String expected = StringUtil.releaseBuilder(expectedSb);
 assertEquals(expected, attr.html());
 }

 @Test
 public void testToString() {
 String expected = attr.html();
 assertEquals(expected, attr.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute expected = new Attribute("key", "value");
 Attribute actual = Attribute.createFromEncoded("key", "value");
 assertEquals(expected, actual);
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attr.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithNonDataAttributeName() {
 Attribute nonDataAttr = new Attribute("nonDataKey", "nonDataValue");
 assertFalse(nonDataAttr.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeTrue() throws IOException {
 Document.OutputSettings settings = new Document.OutputSettings();
 assertTrue(attr.shouldCollapseAttribute(settings));
 }

 @Test
 public void testShouldCollapseAttributeFalse() throws IOException {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.syntax(Document.OutputSettings.Syntax.xml);
 assertFalse(attr.shouldCollapseAttribute(settings));
 }

 @Test
 public void testIsBooleanAttributeTrue() {
 assertTrue(attr.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeFalse() {
 Attribute nonBooleanAttr = new Attribute("nonBooleanKey", "nonBooleanValue");
 assertFalse(nonBooleanAttr.isBooleanAttribute());
 }

 @Test
 public void testEqualsTrue() {
 Attribute equalAttr = new Attribute("key", "value");
 assertTrue(attr.equals(equalAttr));
 }

 @Test
 public void testEqualsFalse() {
 Attribute otherAttr = new Attribute("otherKey", "otherValue");
 assertFalse(attr.equals(otherAttr));
 }

 @Test
 public void testClone() {
 Attribute clonedAttr = attr.clone();
 assertEquals(attr.getKey(), clonedAttr.getKey());
 assertEquals(attr.getValue(), clonedAttr.getValue());
 }
}