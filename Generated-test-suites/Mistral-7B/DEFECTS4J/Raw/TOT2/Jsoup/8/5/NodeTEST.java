import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Node("baseUri", new Attributes());
 Assert.assertEquals("baseUri", node.baseUri());
 Assert.assertNotNull(node.attributes());
 Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
 }

 @Test
 public void testNodeCreationWithAttributes() {
 String attributeKey = "testAttr";
 String attributeValue = "testValue";
 Node node = new Node("baseUri", new Attributes().put(attributeKey, attributeValue));
 Assert.assertEquals(attributeValue, node.attr(attributeKey));
 }

 @Test
 public void testNodeCreationWithoutBaseUri() {
 Node node = new Node();
 Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
 Assert.assertNull(node.attributes());
 }

 @Test
 public void testNodeName() {
 Node node = new Element("div");
 Assert.assertEquals("div", node.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testNodeNameMismatch() {
 Node node = new Node();
 try {
 Assert.assertEquals("p", node.nodeName());
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testAttr() {
 Node node = new Element("a").attr("href", "http://test.com");
 Assert.assertEquals("http://test.com", node.absUrl("href"));
 Assert.assertEquals("", node.attr("nonexistentattr"));
 }

 @Test
 public void testAttrWithAbsPrefix() {
 Node node = new Element("a").attr("abs:href", "http://test.com");
 Assert.assertEquals("http://test.com", node.absUrl("href"));
 }

 @Test(expected = AssertionError.class)
 public void testAttrNull() {
 Node node = new Node();
 try {
 Assert.assertEquals("test", node.attr("test"));
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testAbsUrl() {
 Node node = new Element("a").attr("href", "test.html");
 Assert.assertEquals("", node.absUrl(""));
 Assert.assertEquals("test.html", node.absUrl("href"));
 Assert.assertEquals("http://test.com/test.html", new Node("http://test.com").absUrl("test.html"));
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedAbsUrl() {
 Node node = new Node();
 node.absUrl("invalidUrl");
 }

 @Test
 public void testChildNodes() {
 Node parentNode = new Element("div");
 Node childNode1 = new Element("p").attr("test", "test1");
 Node childNode2 = new Element("p").attr("test", "test2");
 parentNode.addChildren(childNode1, childNode2);
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(2, childNodes.size());
 Assert.assertEquals(childNode1, childNodes.get(0));
 Assert.assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testChildNodesWithArray() {
 Node parentNode = new Element("div");
 Node childNode1 = new Element("p").attr("test", "test1");
 Node childNode2 = new Element("p").attr("test", "test2");
 parentNode.addChildren(new Node[]{childNode1, childNode2});
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(2, childNodes.size());
 Assert.assertEquals(childNode1, childNodes.get(0));
 Assert.assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testRemoveChild() {
 Node parentNode = new Element("div");
 Node childNode1 = new Element("p").attr("test", "test1");
 Node childNode2 = new Element("p").attr("test", "test2");
 parentNode.addChildren(childNode1, childNode2);
 parentNode.removeChild(childNode1);
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(1, childNodes.size());
 Assert.assertEquals(childNode2, childNodes.get(0));
 }

 @Test
 public void testSetParentNode() {
 Node childNode = new Element("p");
 Node parentNode = new Element("div");
 childNode.setParentNode(parentNode);
 Assert.assertEquals(parentNode, childNode.parent());
 }

 @Test
 public void testReplaceChild() {
 Node oldChild = new Element("p").attr("test", "test1");
 Node newChild = new Element("p").attr("test", "test2");
 Node parentNode = new Element("div");
 parentNode.addChildren(oldChild);
 parentNode.replaceChild(oldChild, newChild);
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(1, childNodes.size());
 Assert.assertEquals(newChild, childNodes.get(0));
 }

 @Test
 public void testAddChildren() {
 Node parentNode = new Element("div");
 Node childNode1 = new Element("p").attr("test", "test1");
 Node childNode2 = new Element("p").attr("test", "test2");
 parentNode.addChildren(childNode1, childNode2);
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(2, childNodes.size());
 Assert.assertEquals(childNode1, childNodes.get(0));
 Assert.assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testAddChildrenWithIndex() {
 Node parentNode = new Element("div");
 Node childNode1 = new Element("p").attr("test", "test1");
 Node childNode2 = new Element("p").attr("test", "test2");
 parentNode.addChildren(1, new Node[]{childNode1, childNode2});
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(2, childNodes.size());
 Assert.assertEquals(childNode1, childNodes.get(0));
 Assert.assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testGetParent() {
 Node childNode = new Element("p").attr("test", "test");
 Node parentNode = new Element("div").attr("test", "test");
 parentNode.addChild(childNode);
 Assert.assertEquals(parentNode, childNode.parent());
 }

 @Test
 public void testGetParentNull() {
 Node childNode = new Element("p").attr("test", "test");
 Assert.assertNull(childNode.parent());
 }

 @Test
 public void testOwnerDocument() {
 Node childNode = new Element("p").attr("test", "test");
 Node parentNode = new Element("div").attr("test", "test");
 parentNode.addChild(childNode);
 Assert.assertEquals(parentNode.ownerDocument(), childNode.ownerDocument());
 }

 @Test
 public void testOwnerDocumentNull() {
 Node childNode = new Element("p").attr("test", "test");
 Assert.assertNull(childNode.ownerDocument());
 }

 @Test
 public void testRemove() {
 Node parentNode = new Element("div").attr("test", "test");
 Node childNode = new Element("p").attr("test", "test");
 parentNode.addChild(childNode);
 parentNode.remove();
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(0, childNodes.size());
 }
}