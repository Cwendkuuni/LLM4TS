import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes.Attribute;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new TextNode("Test Text");
 assertFalse(leafNode.hasAttributes());

 Element element = new Element("TestElement");
 element.attr("test", "TestValue");
 assertFalse(element.hasAttributes());

 Attributes attributes = new Attributes();
 attributes.put("test", "TestValue");
 LeafNode attributesLeafNode = new TextNode("", attributes);
 assertTrue(attributesLeafNode.hasAttributes());
 }

 @Test
 public void testGetAttributes() {
 LeafNode leafNode = new TextNode("Test Text");
 assertEquals(0, leafNode.attributes().size());

 Element element = new Element("TestElement");
 element.attr("test", "TestValue");
 Attributes attributes = element.attributes();
 assertEquals("TestValue", attributes.get("test"));

 Attributes attributesLeafNode = new Attributes();
 attributesLeafNode.put("test", "TestValue");
 LeafNode attributesLeafNodeWithValue = new TextNode("", attributesLeafNode);
 assertEquals("TestValue", attributesLeafNodeWithValue.attr("test"));
 }

 @Test
 public void testSetCoreValue() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.coreValue("Test Value");
 assertEquals("Test Value", leafNode.text());

 Element element = new Element("TestElement");
 element.text("Test Text");
 element.coreValue("Test Value");
 assertEquals("Test Value", element.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCoreValueWithNull() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.coreValue(null);
 }

 @Test
 public void testGetCoreValue() {
 LeafNode leafNode = new TextNode("Test Text");
 assertEquals("Test Text", leafNode.coreValue());

 Element element = new Element("TestElement");
 element.text("Test Text");
 assertEquals("Test Text", element.coreValue());
 }

 @Test
 public void testSetAndGetAttribute() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr("test", "TestValue");
 assertEquals("TestValue", leafNode.attr("test"));

 Element element = new Element("TestElement");
 element.attr("test", "TestValue");
 assertEquals("TestValue", element.attr("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAttributeWithNullKey() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr(null, "TestValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAttributeWithNullValue() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr("test", null);
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new TextNode("Test Text");
 assertFalse(leafNode.hasAttr("test"));

 Element element = new Element("TestElement");
 element.attr("test", "TestValue");
 assertTrue(element.hasAttr("test"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new TextNode("Test Text", new Attributes().put("test", "TestValue"));
 assertEquals("TestText", leafNode.attr("test"));
 leafNode.removeAttr("test");
 assertNull(leafNode.attr("test"));

 Element element = new Element("TestElement");
 element.attr("test", "TestValue");
 assertEquals("TestValue", element.attr("test"));
 element.removeAttr("test");
 assertNull(element.attr("test"));
 }

 @Test
 public void testGetAbsUrl() {
 LeafNode leafNode = new TextNode("Test Text");
 assertEquals("", leafNode.absUrl("test"));

 Element element = new Element("TestElement");
 element.attr("test", "http://test.com/TestValue");
 assertEquals("http://test.com/TestValue", element.absUrl("test"));
 }

 @Test
 public void testBaseUri() {
 Element element = new Element("TestElement");
 element.parent().attr("baseuri", "http://test.com/");
 assertEquals("http://test.com/", element.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode leafNode = new TextNode("Test Text");
 assertEquals(0, leafNode.childNodeSize());
 }
}