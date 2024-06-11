import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testCreation() {
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attributes, attribute.getParent());
 assertNull(attribute.getParent().get(attribute.getKey()));
 }

 @Test
 public void testSettingKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 assertSame(attributes, attribute.getParent());
 assertEquals("value", attributes.get(newKey).getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullKey() {
 attribute.setKey(null);
 }

 @Test
 public void testSettingValue() {
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals("value", attribute.getValueBefore());
 assertEquals(newValue, attribute.getValue());
 assertSame(attributes, attribute.getParent());
 assertEquals(newValue, attributes.get(attribute.getKey()).getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws Exception {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 String dataKey = "data-test";
 Attribute dataAttribute = new Attribute(dataKey, null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 String booleanKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanKey, null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute equalAttribute = new Attribute("key", "value", attributes);
 assertTrue(attribute.equals(equalAttribute));
 }

 @Test
 public void testNotEqualsDifferentKey() {
 Attribute differentKeyAttribute = new Attribute("differentKey", "value", attributes);
 assertFalse(attribute.equals(differentKeyAttribute));
 }

 @Test
 public void testNotEqualsDifferentValue() {
 Attribute differentValueAttribute = new Attribute("key", "differentValue", attributes);
 assertFalse(attribute.equals(differentValueAttribute));
 }

 @Test
 public void testNotEqualsDifferentParent() {
 Attributes differentParentAttributes = new Attributes();
 Attribute differentParentAttribute = new Attribute("key", "value", differentParentAttributes);
 assertFalse(attribute.equals(differentParentAttribute));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertEquals(attribute.getParent(), clonedAttribute.getParent());
 }
}