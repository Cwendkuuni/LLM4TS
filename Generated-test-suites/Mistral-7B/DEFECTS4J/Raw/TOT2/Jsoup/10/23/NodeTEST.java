import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

public class NodeTest {

 private Node node;
 private Document doc;

 @Before
 public void setUp() {
 node = new Element("div");
 node.attributes().put("id", "test");
 doc = new Document();
 doc.appendChild(node);
 }

 @Test
 public void testNodeName() {
 assertEquals("div", node.nodeName());
 }

 @Test
 public void testAttr() {
 node.attr("class", "test-class");
 assertEquals("test-class", node.attr("class"));
 assertEquals("", node.attr("non-existent-attr"));
 }

 @Test
 public void testAttributes() {
 Attributes attributes = node.attributes();
 attributes.put("id", "test");
 assertEquals("test", attributes.get("id"));
 }

 @Test
 public void testHasAttr() {
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("non-existent-attr"));
 }

 @Test
 public void testRemoveAttr() {
 node.attr("class", "test-class");
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testBaseUri() {
 node.setBaseUri("http://example.com");
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testAbsUrl() {
 node.attributes().put("href", "/test.html");
 assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 @Test
 public void testChildNode() {
 Node child = new Element("p");
 node.appendChild(child);
 assertEquals(child, node.childNode(0));
 }

 @Test
 public void testChildren() {
 node.appendChild(new TextNode("Test"));
 node.appendChild(new Element("p"));
 List<Node> children = node.childNodes();
 assertEquals(2, children.size());
 }

 @Test
 public void testParentNode() {
 Element parent = new Element("body");
 parent.appendChild(node);
 assertEquals(parent, node.parent());
 }

 @Test
 public void testOwnerDocument() {
 assertEquals(doc, node.ownerDocument());
 }

 @Test
 public void testRemove() {
 doc.remove(node);
 assertFalse(node.parentNode() instanceof Node);
 }

 @Test
 public void testReplaceWith() {
 Node newNode = new Element("span");
 node.replaceWith(newNode);
 assertEquals(newNode, node.parentNode().childNode(node.siblingIndex()));
 }

 @Test
 public void testSetParentNode() {
 Node newParent = new Element("ul");
 node.setParentNode(newParent);
 assertEquals(newParent, node.parentNode());
 }

 @Test
 public void testReplaceChild() {
 Node oldNode = new Element("li");
 Node newNode = new Element("li");
 node.replaceChild(oldNode, newNode);
 assertEquals(newNode, node.childNode(oldNode.siblingIndex()));
 }

 @Test
 public void testAddChildren() {
 Node child1 = new Element("li");
 Node child2 = new Element("li");
 node.addChildren(child1, child2);
 assertEquals(3, node.childNodes().size());
 }

 @Test
 public void testAddChildrenArray() {
 Node child1 = new Element("li");
 Node child2 = new Element("li");
 node.addChildren(new Node[]{child1, child2});
 assertEquals(3, node.childNodes().size());
 }

 @Test
 public void testSiblingNodes() {
 Node sibling = new Element("p");
 node.appendChild(sibling);
 List<Node> siblings = node.siblingNodes();
 assertEquals(1, siblings.size());
 assertEquals(sibling, siblings.get(0));
 }

 @Test
 public void testNextSibling() {
 Node sibling = new Element("p");
 node.appendChild(new TextNode("Test"));
 node.appendChild(sibling);
 assertEquals(sibling, node.nextSibling());
 }

 @Test
 public void testPreviousSibling() {
 Node sibling = new Element("p");
 TextNode textNode = new TextNode("Test");
 node.appendChild(textNode);
 node.appendChild(sibling);
 assertEquals(textNode, node.previousSibling());
 }

 @Test
 public void testSiblingIndex() {
 Node sibling = new Element("p");
 TextNode textNode = new TextNode("Test");
 node.appendChild(textNode);
 node.appendChild(sibling);
 assertEquals(1, sibling.siblingIndex());
 }

 @Test(expected = NullPointerException.class)
 public void testNullParent() {
 Node node2 = new Element("div");
 node2.setParentNode(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNegativeIndexChild() {
 node.addChildren(new Element("p"));
 node.addChildren(new Element("p"));
 assertEquals(-1, node.childNode(-1).siblingIndex());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNullIndexChild() {
 node.addChildren(new Element("p"));
 node.addChildren(new Element("p"));
 assertEquals(Node.class, node.childNode(null).getClass());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsSibling() {
 node.addChildren(new Element("p"));
 node.addChildren(new Element("p"));
 assertEquals(Node.class, node.siblingNode(2).getClass());
 }
}