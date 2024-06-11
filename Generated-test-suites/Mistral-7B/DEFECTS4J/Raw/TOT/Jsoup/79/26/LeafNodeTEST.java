import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new TextNode("Test");
 Assert.assertFalse(leafNode.hasAttributes());

 LeafNode leafNodeWithAttributes = new LeafNode("Test") {
 @Override
 public Attributes attributes() {
 Attributes attributes = new Attributes();
 attributes.put("testAttribute", "testValue");
 return attributes;
 }
 };
 Assert.assertTrue(leafNodeWithAttributes.hasAttributes());
 }

 @Test
 public void testGetAndSetCoreValue() {
 LeafNode leafNode = new TextNode("Test");
 Assert.assertEquals("Test", leafNode.coreValue());

 leafNode.coreValue("New Test");
 Assert.assertEquals("New Test", leafNode.coreValue());
 }

 @Test
 public void testGetAttribute() {
 LeafNode leafNode = new TextNode("Test") {
 @Override
 public Attributes attributes() {
 Attributes attributes = new Attributes();
 attributes.put("testAttribute", "testValue");
 return attributes;
 }
 };

 Assert.assertEquals("testValue", leafNode.attr("testAttribute"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetAttributeNullKey() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.attr(null, "testValue");
 leafNode.attr("testAttribute", "testValue");

 Assert.assertNull(leafNode.attr(null));
 }

 @Test(expected = NullPointerException.class)
 public void testSetAttributeNullKey() {
 LeafNode leafNode = new TextNode("Test");

 leafNode.attr(null, "testValue");
 Assert.fail("Expected NullPointerException");
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new TextNode("Test") {
 @Override
 public Attributes attributes() {
 Attributes attributes = new Attributes();
 attributes.put("testAttribute", "testValue");
 return attributes;
 }
 };

 Assert.assertTrue(leafNode.hasAttr("testAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new TextNode("Test") {
 @Override
 public Attributes attributes() {
 Attributes attributes = new Attributes();
 attributes.put("testAttribute", "testValue");
 return attributes;
 }
 };

 Assert.assertFalse(leafNode.hasAttr("testAttribute"));
 leafNode.attr("testAttribute", "newTestValue");
 Assert.assertTrue(leafNode.hasAttr("testAttribute"));
 leafNode.removeAttr("testAttribute");
 Assert.assertFalse(leafNode.hasAttr("testAttribute"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new TextNode("Test") {
 @Override
 public Attributes attributes() {
 Attributes attributes = new Attributes();
 attributes.put("abs:url", "http://test.com");
 return attributes;
 }
 };

 Assert.assertEquals("http://test.com", leafNode.absUrl("abs:url"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 LeafNode leafNode = new TextNode("Test");
 Assert.assertEquals(0, leafNode.childNodeSize());
 }
}