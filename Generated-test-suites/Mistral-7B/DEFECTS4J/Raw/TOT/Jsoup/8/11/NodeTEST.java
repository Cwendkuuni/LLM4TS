import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNewNodeWithBaseUriAndAttributes() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 assertEquals("example", node.nodeName());
 assertEquals("http://example.com", node.baseUri());
 assertEquals("class=example", node.attr("class"));
 }

 @Test
 public void testNewNodeWithoutBaseUri() {
 Node node = new Node();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.baseUri());
 }

 @Test
 public void testNewNodeWithOnlyBaseUri() {
 Node node = new Node("http://example.com");
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testGetAttribute() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example").put("id", "myId"));
 assertEquals("example", node.attr("class"));
 assertEquals("myId", node.attr("id"));
 assertEquals("", node.attr("nonexistent"));
 }

 @Test
 public void testSetAttribute() {
 Node node = new Node("http://example.com");
 node.attr("class", "example").attr("id", "myId");
 assertEquals("example", node.attr("class"));
 assertEquals("myId", node.attr("id"));
 }

 @Test
 public void testHasAttribute() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("nonexistent"));
 }

 @Test
 public void testRemoveAttribute() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example").put("id", "myId"));
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testAbsUrl() {
 Node node = new Node("http://example.com", new Attributes().put("abs:href", "relative.html"));
 assertEquals("http://example.com/relative.html", node.absUrl("href"));
 assertEquals("http://example.com/relative.html", node.absUrl("abs:href"));
 assertEquals("", node.absUrl("nonexistent"));
 }

 @Test
 public void testChildNodes() {
 Node parent = new Node("http://example.com");
 Node child1 = new Node("http://child1.com");
 Node child2 = new Node("http://child2.com");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testParentNode() {
 Node child = new Node("http://child.com");
 Node parent = new Node("http://parent.com");
 child.setParentNode(parent);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testOwnDocument() {
 Node node = new Node("http://example.com");
 assertNull(node.ownerDocument());
 Node doc = new Document();
 doc.appendChild(node);
 assertEquals(doc, node.ownerDocument());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveChildWithNullParent() {
 Node node = new Node();
 node.removeChild(null);
 }

 @Test
 public void testRemoveChild() {
 Node parent = new Node("http://parent.com");
 Node child = new Node("http://child.com");
 parent.addChildren(child);
 parent.removeChild(child);
 assertTrue(parent.childNodes().isEmpty());
 }
}