import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class NodeTest {

 @Test
 public void testNewNode() {
 Node node = new Node("baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 assertNull(node.attributes());
 }

 @Test
 public void testNewNodeWithAttributes() {
 Node node = new Node("baseUri", new Attributes().put("key", "value"));
 assertEquals("baseUri", node.baseUri());
 assertEquals("value", node.attr("key"));
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 assertSame(node.attributes(), node.attributes());
 }

 @Test
 public void testNewEmptyNode() {
 Node node = new Node();
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.attributes());
 }

 @Test
 public void testNodeName() {
 Node node = new Node("baseUri", new Attributes());
 assertNull(node.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testNullBaseUri() {
 new Node(null, new Attributes());
 }

 @Test(expected = AssertionError.class)
 public void testNullAttributes() {
 new Node("baseUri", null);
 }

 @Test
 public void testAttr() {
 Node node = new Node("baseUri", new Attributes().put("key", "value"));
 assertEquals("value", node.attr("key"));
 assertNull(node.attr("notKey"));
 }

 @Test
 public void testAttributes() {
 Node node = new Node("baseUri", new Attributes().put("key1", "value1").put("key2", "value2"));
 assertEquals("value1", node.attr("key1"));
 assertEquals("value2", node.attr("key2"));
 assertNull(node.attr("notKey"));
 assertSame(node.attributes(), node.attributes());
 }

 @Test
 public void testHasAttr() {
 Node node = new Node("baseUri", new Attributes().put("key", "value"));
 assertTrue(node.hasAttr("key"));
 assertFalse(node.hasAttr("notKey"));
 }

 @Test
 public void testRemoveAttr() {
 Node node = new Node("baseUri", new Attributes().put("key1", "value1").put("key2", "value2"));
 node.removeAttr("key1");
 assertNull(node.attr("key1"));
 assertSame(node.attributes(), node.attributes());
 }

 @Test
 public void testBaseUri() {
 Node node = new Node("baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 node.setBaseUri("newBaseUri");
 assertEquals("newBaseUri", node.baseUri());
 }

 @Test(expected = AssertionError.class)
 public void testMalformedBaseUri() {
 Node node = new Node("baseUri:invalid", new Attributes());
 }

 @Test
 public void testAbsUrl() {
 Node node = new Node("baseUri", new Attributes().put("href", "relativeUrl"));
 assertEquals("baseUri/relativeUrl", node.absUrl("href"));
 assertEquals("", node.absUrl("notHref"));
 }

 @Test
 public void testChildNode() {
 Node parent = new Node("parentBaseUri", new Attributes());
 Node child = new Node("childBaseUri", new Attributes());
 parent.addChild(child);
 assertEquals(child, parent.childNode(0));
 }

 @Test
 public void testChildNodes() {
 Node parent = new Node("parentBaseUri", new Attributes());
 Node child1 = new Node("child1BaseUri", new Attributes());
 Node child2 = new Node("child2BaseUri", new Attributes());
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testParent() {
 Node child = new Node("baseUri", new Attributes());
 Node parent = new Node("parentBaseUri", new Attributes());
 child.setParentNode(parent);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testOwnerDocument() {
 Node node = new Node("baseUri", new Attributes());
 assertNull(node.ownerDocument());

 Node parent = new Node("parentBaseUri", new Attributes());
 parent.addChild(node);
 assertEquals(parent, node.ownerDocument());
 }

 @Test
 public void testRemove() {
 Node parent = new Node("parentBaseUri", new Attributes());
 Node child1 = new Node("child1BaseUri", new Attributes());
 Node child2 = new Node("child2BaseUri", new Attributes());
 parent.addChildren(child1, child2);
 parent.removeChild(child1);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 assertEquals(child2, children.get(0));
 }

 @Test
 public void testReplaceWith() {
 Node parent = new Node("parentBaseUri", new Attributes());
 Node node1 = new Node("node1BaseUri", new Attributes());
 Node node2 = new Node("node2BaseUri", new Attributes());
 parent.addChild(node1);
 parent.replaceWith(node2, node1);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 assertEquals(node2, children.get(0));
 }

 @Test
 public void testSetParentNode() {
 Node child = new Node("baseUri", new Attributes());
 Node parent1 = new Node("parent1BaseUri", new Attributes());
 Node parent2 = new Node("parent2BaseUri", new Attributes());
 child.setParentNode(parent1);
 parent1.removeChild(child);
 child.setParentNode(parent2);
 assertEquals(parent2, child.parent());
 }
}