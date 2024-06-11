import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeInstance() {
 Node node = new Elements.Element("div");
 assertNotNull(node);
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertNull(node.childNodes());
 }

 @Test
 public void testNodeWithAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("class", "test-class");
 Node node = new Elements.Element("div", "", attributes);
 assertEquals("div", node.nodeName());
 assertEquals("test-class", node.attr("class"));
 assertNotNull(node.attributes());
 assertNull(node.baseUri());
 assertNull(node.childNodes());
 }

 @Test
 public void testNodeWithBaseUri() {
 String baseUri = "http://example.com";
 Node node = new Elements.Element("div", baseUri, new Attributes());
 assertEquals("div", node.nodeName());
 assertNull(node.attributes());
 assertEquals(baseUri, node.baseUri());
 assertNull(node.childNodes());
 }

 @Test
 public void testNodeWithParentChildRelationship() throws Exception {
 String baseUri = "http://example.com";
 Attributes parentAttributes = new Attributes();
 parentAttributes.put("id", "parent");
 Node parentNode = new Elements.Element("div", baseUri, parentAttributes);
 Attribute attr = new Attribute("class", "child");
 Node childNode = new Elements.Element("div", baseUri, new Attributes().put("class", "another-class"));
 childNode.setParentNode(parentNode);
 parentNode.addChild(childNode);
 assertEquals(parentNode, childNode.parentNode());
 assertEquals(childNode, parentNode.childNode(0));
 }

 @Test
 public void testAbsUrl() {
 String baseUri = "http://example.com";
 Node node = new Elements.Element("a", baseUri, new Attributes().put("href", "relative.html"));
 assertEquals("", node.absUrl(""));
 assertEquals("relative.html", node.absUrl("href"));
 assertEquals("http://example.com/relative.html", node.absUrl("href"));
 assertEquals("", node.absUrl("non-existent"));
 }

 @Test(expected = NullPointerException.class)
 public void testAbsUrlNullBaseUri() {
 Node node = new Elements.Element("a", null, new Attributes().put("href", ""));
 node.absUrl("href");
 }

 @Test(expected = NullPointerException.class)
 public void testAbsUrlNullAttributeKey() {
 Node node = new Elements.Element("a", "", new Attributes());
 node.absUrl(null);
 }

 @Test
 public void testHasAttr() {
 Node node = new Elements.Element("div", "", new Attributes().put("class", "test-class"));
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("non-existent"));
 }

 @Test
 public void testAttributeValue() {
 Node node = new Elements.Element("div", "", new Attributes().put("class", "test-class").put("id", "test-id"));
 assertEquals("test-class", node.attr("class"));
 assertEquals("test-id", node.attr("id"));
 assertEquals("", node.attr("non-existent"));
 }

 @Test
 public void testSetAttribute() {
 Node node = new Elements.Element("div", "", new Attributes());
 node.attr("class", "test-class");
 assertEquals("test-class", node.attr("class"));
 }

 @Test
 public void testRemoveAttribute() {
 Node node = new Elements.Element("div", "", new Attributes().put("class", "test-class"));
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testSetParentNode() {
 Node childNode = new Elements.Element("div");
 Node parentNode = new Elements.Element("div");
 childNode.setParentNode(parentNode);
 assertEquals(parentNode, childNode.parentNode());
 }

 @Test
 public void testRemoveChild() {
 Node parentNode = new Elements.Element("div");
 Node childNode1 = new Elements.Element("div");
 Node childNode2 = new Elements.Element("div");
 parentNode.addChild(childNode1);
 parentNode.addChild(childNode2);
 parentNode.removeChild(childNode1);
 assertFalse(parentNode.hasChildNode(childNode1));
 }

 @Test
 public void testReplaceChild() {
 Node parentNode = new Elements.Element("div");
 Node childNode1 = new Elements.Element("div");
 Node childNode2 = new Elements.Element("div");
 parentNode.addChild(childNode1);
 parentNode.replaceChild(childNode1, childNode2);
 assertEquals(childNode2, parentNode.childNode(0));
 }

 @Test
 public void testAddChildren() {
 Node parentNode = new Elements.Element("div");
 Node childNode1 = new Elements.Element("div");
 Node childNode2 = new Elements.Element("div");
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 assertEquals(childNode1, parentNode.childNode(0));
 assertEquals(childNode2, parentNode.childNode(1));
 }

 @Test
 public void testSiblingNodes() {
 Node parentNode = new Elements.Element("div");
 Node sibling1 = new Elements.Element("div");
 Node sibling2 = new Elements.Element("div");
 Node thisNode = new Elements.Element("div");
 parentNode.addChild(sibling1);
 parentNode.addChild(thisNode);
 parentNode.addChild(sibling2);
 List<Node> siblings = thisNode.siblingNodes();
 assertEquals(2, siblings.size());
 assertEquals(sibling1, siblings.get(0));
 assertEquals(sibling2, siblings.get(1));
 }

 @Test
 public void testNextSibling() {
 Node parentNode = new Elements.Element("div");
 Node sibling1 = new Elements.Element("div");
 Node thisNode = new Elements.Element("div");
 Node sibling2 = new Elements.Element("div");
 parentNode.addChild(sibling1);
 parentNode.addChild(thisNode);
 parentNode.addChild(sibling2);
 assertEquals(sibling2, thisNode.nextSibling());
 }

 @Test
 public void testPreviousSibling() {
 Node parentNode = new Elements.Element("div");
 Node sibling1 = new Elements.Element("div");
 Node thisNode = new Elements.Element("div");
 Node sibling2 = new Elements.Element("div");
 parentNode.addChild(sibling1);
 parentNode.addChild(thisNode);
 parentNode.addChild(sibling2);
 assertEquals(sibling1, thisNode.previousSibling());
 }

 @Test
 public void testGetDocument() {
 Node node = new Elements.Element("div");
 assertNull(node.ownerDocument());
 Document document = new Document();
 document.setBaseUri("http://example.com");
 document.appendChild(node);
 assertEquals(document, node.ownerDocument());
 }

 @Test
 public void testRemove() {
 Node parentNode = new Elements.Element("div");
 Node childNode = new Elements.Element("div");
 parentNode.addChild(childNode);
 parentNode.remove();
 assertFalse(parentNode.hasChildNode(childNode));
 }
}