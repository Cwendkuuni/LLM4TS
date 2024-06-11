import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeConstructor() {
 Node node = new Node("baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 assertNotNull(node.attributes());
 assertTrue(node.childNodes().isEmpty());
 }

 @Test
 public void testNodeConstructorWithAttributes() {
 Node node = new Node("baseUri", new Attributes().put("attrKey", "attrValue"));
 assertEquals("baseUri", node.baseUri());
 assertEquals("attrValue", node.attr("attrKey"));
 }

 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Node();
 assertNull(node.baseUri());
 assertNull(node.attributes());
 assertTrue(node.childNodes().isEmpty());
 }

 @Test
 public void testGetNodeName() {
 fail("Abstract Node class, cannot instantiate or test node name directly.");
 }

 @Test
 public void testGetAttribute() {
 Node node = new Element("div", new Attributes().put("attrKey", "attrValue"));
 assertEquals("attrValue", node.attr("attrKey"));
 assertTrue(node.hasAttr("attrKey"));
 assertEquals("", node.attr("nonExistentAttr"));
 }

 @Test
 public void testGetAllAttributes() {
 Node node = new Element("div", new Attributes().put("attr1", "value1").put("attr2", "value2"));
 Attributes attributes = node.attributes();
 assertEquals("attr1", attributes.getKey(0));
 assertEquals("value1", attributes.getValue(0));
 assertEquals("attr2", attributes.getKey(1));
 assertEquals("value2", attributes.getValue(1));
 }

 @Test
 public void testSetAttribute() {
 Node node = new Element("div");
 node.attr("attrKey", "attrValue");
 assertEquals("attrValue", node.attr("attrKey"));
 }

 @Test
 public void testHasAttribute() {
 Node node = new Element("div", new Attributes().put("attrKey", "attrValue"));
 assertTrue(node.hasAttr("attrKey"));
 assertFalse(node.hasAttr("nonExistentAttr"));
 }

 @Test
 public void testRemoveAttribute() {
 Node node = new Element("div", new Attributes().put("attrKey", "attrValue"));
 node.removeAttr("attrKey");
 assertFalse(node.hasAttr("attrKey"));
 }

 @Test
 public void testGetBaseUri() {
 Node node = new Element("div", new Attributes(), "baseUri");
 assertEquals("baseUri", node.baseUri());
 }

 @Test
 public void testSetBaseUri() {
 Node node = new Element("div");
 node.setBaseUri("newBaseUri");
 assertEquals("newBaseUri", node.baseUri());
 }

 @Test
 public void testGetChildNodes() {
 Node parent = new Element("div");
 Node child1 = new Element("p", new Attributes(), "child1");
 Node child2 = new Element("p", new Attributes(), "child2");
 parent.addChildren(child1, child2);
 List<Node> childNodes = parent.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(child1, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 @Test
 public void testGetChildNode() {
 Node parent = new Element("div");
 Node child = new Element("p", new Attributes(), "child");
 parent.addChildren(child);
 assertEquals(child, parent.childNode(0));
 }

 @Test
 public void testGetChildNodesEmptyList() {
 Node node = new Element("div");
 List<Node> childNodes = node.childNodes();
 assertTrue(childNodes.isEmpty());
 }

 @Test
 public void testGetParentNode() {
 Node child = new Element("p", new Attributes(), "child");
 Node parent = new Element("div", new Attributes(), "parent");
 parent.addChildren(child);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testGetOwnerDocument() {
 Node child = new Element("p", new Attributes(), "child");
 Document document = new Document();
 document.addChild(child);
 assertEquals(document, child.ownerDocument());
 }

 @Test
 public void testRemove() {
 Node parent = new Element("div");
 Node child = new Element("p", new Attributes(), "child");
 parent.addChildren(child);
 parent.remove();
 assertFalse(child.hasParent());
 }

 @Test
 public void testReplaceWith() {
 Node parent = new Element("div");
 Node oldChild = new Element("p", new Attributes(), "oldChild");
 Node newChild = new Element("p", new Attributes(), "newChild");
 parent.addChildren(oldChild);
 parent.replaceWith(newChild);
 assertEquals(newChild, oldChild.parent());
 assertEquals(newChild, parent.childNode(0));
 }

 @Test
 public void testClone() {
 Node parent = new Element("div");
 Node originalChild = new Element("p", new Attributes(), "originalChild");
 Node clonedChild = originalChild.clone();
 assertEquals(originalChild, clonedChild.clone().parent());
 assertNotEquals(originalChild, clonedChild.parent());
 }
}