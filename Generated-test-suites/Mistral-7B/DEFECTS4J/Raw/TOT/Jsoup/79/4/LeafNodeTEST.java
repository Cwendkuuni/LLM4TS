import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode node = new TextNode("Hello World");
 Assert.assertFalse(node.hasAttributes());

 Element element = new Element("div");
 element.attr("id", "test");
 element.coreValue("Test Value");
 Assert.assertTrue(element.hasAttributes());
 }

 @Test
 public void testGetCoreValue() {
 TextNode node = new TextNode("Hello World");
 Assert.assertEquals("Hello World", node.coreValue());

 Element element = new Element("div");
 element.attr("id", "test");
 element.coreValue("Test Value");
 Assert.assertEquals("Test Value", element.coreValue());
 }

 @Test
 public void testSetCoreValue() {
 TextNode node = new TextNode("Hello World");
 node.coreValue("New Value");
 Assert.assertEquals("New Value", node.coreValue());

 Element element = new Element("div");
 element.attr("id", "test");
 element.coreValue("Test Value");
 element.coreValue("New Value");
 Assert.assertEquals("New Value", element.coreValue());
 }

 @Test
 public void testGetAttribute() {
 TextNode node = new TextNode("Hello World");
 Assert.assertEquals(EmptyString, node.attr("test"));

 Element element = new Element("div");
 element.attr("id", "test");
 element.attr("class", "testClass");
 Assert.assertEquals("test", element.attr("id"));
 Assert.assertEquals("testClass", element.attr("class"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetAttributeNullKey() {
 TextNode node = new TextNode("Hello World");
 node.attr(null, "Test Value");
 Assert.fail("Should not be able to set attribute with null key.");
 }

 @Test
 public void testSetAttribute() {
 TextNode node = new TextNode("Hello World");
 node.attr("test", "Test Value");
 Assert.assertEquals("Test Value", node.attr("test"));

 Element element = new Element("div");
 element.attr("id", "test");
 Assert.assertNull(element.attr("nonExistentAttribute"));
 element.attr("nonExistentAttribute", "Test Value");
 Assert.assertEquals("Test Value", element.attr("nonExistentAttribute"));
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testSetAttributeInvalidKey() {
 TextNode node = new TextNode("Hello World");
 node.attr("", "Test Value");
 Assert.fail("Should not be able to set attribute with empty key.");

 Element element = new Element("div");
 element.attr("id", "test");
 element.attr("", "Test Value");
 Assert.fail("Should not be able to set attribute with empty key.");
 }

 @Test
 public void testRemoveAttribute() {
 TextNode node = new TextNode("Hello World");
 node.attr("test", "Test Value");
 node.removeAttr("test");
 Assert.assertEquals(EmptyString, node.attr("test"));

 Element element = new Element("div");
 element.attr("id", "test");
 element.attr("class", "testClass");
 element.removeAttr("class");
 Assert.assertNull(element.attr("class"));
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testRemoveAttributeInvalidKey() {
 TextNode node = new TextNode("Hello World");
 node.removeAttr("test");
 node.removeAttr("");
 Assert.fail("Should not be able to remove attribute with empty key.");
 }

 @Test
 public void testAttrExistence() {
 TextNode node = new TextNode("Hello World");
 Assert.assertFalse(node.hasAttr("test"));

 Element element = new Element("div");
 element.attr("id", "test");
 Assert.assertTrue(element.hasAttr("id"));
 }
}