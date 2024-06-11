import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 private Node createTestNode(String baseUri) {
 Attributes attributes = new Attributes();
 attributes.put("id", "testNode");
 return new Element("div", baseUri, attributes);
 }

 @Test
 public void testNodeCreation() {
 Node node = new Node("", new Attributes());
 assertEquals("", node.nodeName());
 assertNull(node.parentNode);
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.attributes());
 assertEquals("", node.baseUri());
 assertNull(node.parent());

 Node elementNode = new Element("div", "", new Attributes());
 assertEquals("div", elementNode.nodeName());
 assertNotNull(elementNode.parentNode);
 assertEquals(Collections.EMPTY_LIST, elementNode.childNodes());
 assertNotNull(elementNode.attributes());
 assertEquals("", elementNode.baseUri());
 assertNull(elementNode.parent());
 }

 @Test
 public void testBasicAttributeGetters() {
 Node node = createTestNode("baseUri");
 assertEquals("baseUri", node.baseUri());
 assertNotNull(node.attributes());
 assertEquals("", node.attr(""));
 assertEquals("testNode", node.attr("id"));
 assertEquals("testNode", node.hasAttr("id") ? node.attr("id") : "");
 assertEquals("", node.attr("nonExistent"));
 }

 @Test
 public void testAttributeSetters() {
 Node node = createTestNode("baseUri");
 node.attr("id", "newId");
 assertEquals("newId", node.attr("id"));
 assertTrue(node.hasAttr("id"));
 node.removeAttr("id");
 assertFalse(node.hasAttr("id"));
 }

 @Test
 public void testAbsUrl() {
 Node node = createTestNode("baseUri/subfolder");
 assertEquals("baseUri/subfolder", node.baseUri());
 assertEquals("", node.absUrl(""));
 assertEquals("subfolder/relativeUrl", node.absUrl("relativeUrl"));
 assertEquals("http://example.com/subfolder/relativeUrl", node.absUrl("http://example.com/subfolder/relativeUrl"));
 }

 @Test(expected = MalformedURLException.class)
 public void testAbsUrlMalformedUrl() {
 Node node = createTestNode("baseUri/subfolder");
 assertEquals("", node.absUrl("abs:malf:ormedUrl"));
 }

 @Test
 public void testChildNodes() {
 Node parent = createTestNode("baseUri");
 Node child1 = new TextNode("child1");
 Node child2 = new TextNode("child2");
 parent.addChildren(child1, child2);
 List<Node> childNodes = parent.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(child1, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 @Test
 public void testChildNodesAsArray() {
 Node parent = createTestNode("baseUri");
 Node child1 = new TextNode("child1");
 Node child2 = new TextNode("child2");
 parent.addChildren(child1, child2);
 Node[] childNodes = parent.childNodesAsArray();
 assertEquals(2, childNodes.length);
 assertEquals(child1, childNodes[0]);
 assertEquals(child2, childNodes[1]);
 }

 @Test
 public void testAddChildren() {
 Node parent = createTestNode("baseUri");
 Node child1 = new Element("p", "", new Attributes().put("class", "first"));
 Node child2 = new Element("p", "", new Attributes().put("class", "second"));
 parent.addChildren(child1, child2);
 List<Node> childNodes = parent.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(child1, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 @Test
 public void testRemoveChild() {
 Node parent = createTestNode("baseUri");
 Node child = new TextNode("child");
 parent.addChildren(child);
 List<Node> childNodes = parent.childNodes();
 assertEquals(1, childNodes.size());
 parent.removeChild(child);
 assertEquals(0, childNodes.size());
 }

 @Test
 public void testReplaceChild() {
 Node parent = createTestNode("baseUri");
 Node oldChild = new TextNode("oldChild");
 Node newChild = new TextNode("newChild");
 parent.addChildren(oldChild);
 parent.replaceChild(oldChild, newChild);
 List<Node> childNodes = parent.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals(newChild, childNodes.get(0));
 }

 @Test
 public void testSetParentNode() {
 Node child = new TextNode("child");
 Node parent = new Element("parent", "", new Attributes());
 child.setParentNode(parent);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testReplaceChildWithSelf() {
 Node parent = new Element("parent", "", new Attributes());
 Node child = new TextNode("child");
 parent.addChildren(child);
 child.setParentNode(child);
 List<Node> childNodes = parent.childNodes();
 assertEquals(0, childNodes.size());
 assertNull(child.parent());
 }

 @Test
 public void testRemoveAndReplaceWithNull() {
 Node parent = new Element("parent", "", new Attributes());
 Node child = new TextNode("child");
 parent.addChildren(child);
 parent.removeChild(child);
 parent.replaceChild(child, null);
 List<Node> childNodes = parent.childNodes();
 assertEquals(0, childNodes.size());
 assertNull(parent.childNodes().get(0));
 }

 @Test
 public void testReplaceChildWithSelf2() {
 Node parent = new Element("parent", "", new Attributes());
 Node child = new TextNode("child");
 parent.addChildren(child);
 parent.replaceChild(child, child);
 List<Node> childNodes = parent.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals(child, childNodes.get(0));
 }

 @Test
 public void testSetSiblingIndex() {
 Node parent = new Element("parent", "", new Attributes());
 Node sibling1 = new TextNode("sibling1");
 Node sibling2 = new TextNode("sibling2");
 Node child = new TextNode("child");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(1, child.siblingIndex());
 }

 @Test
 public void testSiblingNodes() {
 Node parent = new Element("parent", "", new Attributes());
 Node sibling1 = new TextNode("sibling1");
 Node sibling2 = new TextNode("sibling2");
 Node child = new TextNode("child");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(2, parent.siblingNodes().size());
 assertEquals(sibling2, parent.siblingNodes().get(1));
 }

 @Test
 public void testNextSibling() {
 Node parent = new Element("parent", "", new Attributes());
 Node sibling1 = new TextNode("sibling1");
 Node child = new TextNode("child");
 Node sibling2 = new TextNode("sibling2");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(sibling2, child.nextSibling());
 }

 @Test
 public void testPreviousSibling() {
 Node parent = new Element("parent", "", new Attributes());
 Node sibling1 = new TextNode("sibling1");
 Node child = new TextNode("child");
 Node sibling2 = new TextNode("sibling2");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(sibling1, child.previousSibling());
 }

 @Test
 public void testOuterHtml() {
 Node parent = new Element("parent", "", new Attributes());
 Node child1 = new Element("child1", "", new Attributes().put("class", "child1"));
 Node child2 = new Element("child2", "", new Attributes().put("class", "child2"));
 parent.addChildren(child1, child2);
 Document doc = new Document();
 StringBuilder accum = new StringBuilder(32*1024);
 parent.outerHtml(accum);
 String expected = "<html><head><body><div class=\"parent\">" +
 "<div class=\"child1\"></div>" +
 "<div class=\"child2\"></div>" +
 "</div></body></html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlWithEmptyTextNodes() {
 Node parent = new Element("parent", "", new Attributes());
 Node child1 = new TextNode(" ");
 Node child2 = new TextNode(" ");
 parent.addChildren(child1, child2);
 Document doc = new Document();
 StringBuilder accum = new StringBuilder(32*1024);
 parent.outerHtml(accum);
 String expected = "<html><head><body><div class=\"parent\">" +
 "<text></text>" +
 "<text></text>" +
 "</div></body></html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlWithEmptyElements() {
 Node parent = new Element("parent", "", new Attributes());
 Node child1 = new Element("child1", "", new Attributes());
 Node child2 = new Element("child2", "", new Attributes());
 parent.addChildren(child1, child2);
 Document doc = new Document();
 StringBuilder accum = new StringBuilder(32*1024);
 parent.outerHtml(accum);
 String expected = "<html><head><body><div class=\"parent\">" +
 "<div class=\"child1\"></div>" +
 "<div class=\"child2\"></div>" +
 "</div></body></html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlWithTextAndElements() {
 Node parent = new Element("parent", "", new Attributes());
 Node textChild = new TextNode("textChild");
 Node elementChild = new Element("elementChild", "", new Attributes().put("class", "child"));
 parent.addChildren(textChild, elementChild);
 Document doc = new Document();
 StringBuilder accum = new StringBuilder(32*1024);
 parent.outerHtml(accum);
 String expected = "<html><head><body><div class=\"parent\">" +
 "<div class=\"child\">textChild</div>" +
 "<div class=\"child\">" +
 "<div class=\"elementChild\"></div>" +
 "</div>" +
 "</div></body></html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testRemove() {
 Node parent = new Element("parent", "", new Attributes());
 Node child = new TextNode("child");
 parent.addChildren(child);
 List<Node> childNodes = parent.childNodes();
 assertEquals(1, childNodes.size());
 parent.remove();
 assertEquals(0, childNodes.size());
 assertNull(child.parent());
 }

 @Test
 public void testReplaceWith() {
 Node parent = new Element("parent", "", new Attributes());
 Node child1 = new TextNode("child1");
 Node child2 = new TextNode("child2");
 parent.addChildren(child1);
 parent.replaceWith(child2);
 List<Node> childNodes = parent.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals(child2, childNodes.get(0));
 }
}