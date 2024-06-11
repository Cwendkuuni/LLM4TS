import org.junit.Test;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Node("baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.attributes());
 }

 @Test
 public void testAbstractNodeMethods() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertTrue(node.hasAttr("class"));
 assertEquals("", node.attr("class"));
 node.attr("class", "testClass");
 assertEquals("testClass", node.attr("class"));
 assertFalse(node.hasAttr("nonExistentAttr"));
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testAttributeUrls() {
 Node node = new Element("a");
 node.attr("href", "http://example.com");
 assertEquals("http://example.com", node.absUrl("href"));
 node.attr("abs:href", "https://example.org");
 assertEquals("https://example.org", node.absUrl("abs:href"));
 }

 @Test (expected = MalformedURLException.class)
 public void testMalformedUrlException() {
 Node node = new Element("a");
 node.absUrl("malformed://example.com");
 }

 @Test
 public void testChildNodes() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 Node firstChild = parent.childNode(0);
 assertEquals(0, firstChild.siblingIndex());
 assertEquals(child1, firstChild);
 Node lastChild = parent.childNode(1);
 assertEquals(1, lastChild.siblingIndex());
 assertEquals(child2, lastChild);
 }

 @Test
 public void testRemoveNode() {
 Node parent = new Element("div");
 Node child = new Element("p");
 parent.addChild(child);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 parent.removeChild(child);
 List<Node> remainingChildren = parent.childNodes();
 assertEquals(0, remainingChildren.size());
 }

 @Test
 public void testReplaceNode() {
 Node parent = new Element("div");
 Node oldChild = new Element("p");
 Node newChild = new Element("span");
 parent.addChild(oldChild);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 parent.replaceChild(oldChild, newChild);
 List<Node> newChildren = parent.childNodes();
 assertEquals(1, newChildren.size());
 assertEquals(newChild, newChildren.get(0));
 }

 @Test
 public void testDocumentManipulation() {
 Document doc = new Document();
 Element root = doc.createElement("html");
 doc.appendChild(root);
 Element body = doc.createElement("body");
 root.appendChild(body);
 Element div = doc.createElement("div");
 Element p = doc.createElement("p");
 p.setTextContent("Test content");
 div.appendChild(p);
 body.appendChild(div);
 List<Node> nodes = doc.childNodes();
 assertEquals(3, nodes.size());
 assertEquals("html", nodes.get(0).nodeName());
 assertEquals("body", nodes.get(1).nodeName());
 assertEquals("div", nodes.get(2).nodeName());
 }
}