import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNotNull(node.attributes());
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testBaseUriSetterAndGetter() {
 String baseUri = "http://example.com";
 Node node = new Element("div");
 node.setBaseUri(baseUri);
 assertEquals(baseUri, node.baseUri());
 }

 @Test
 public void testAttrMethod() {
 Node node = new Element("a").attr("href", "http://example.com");
 assertEquals("http://example.com", node.attr("href"));
 assertEquals("", node.attr("nonExistentAttr"));
 }

 @Test
 public void testAttributesSetterAndGetter() {
 Node node = new Element("div");
 Attributes attrs = new Attributes();
 attrs.put("class", "example");
 node.setAttributes(attrs);
 assertEquals("example", node.attr("class"));
 assertEquals(attrs, node.attributes());
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributeKey() {
 Node node = new Element("div");
 node.attr(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributeValue() {
 Node node = new Element("div");
 node.attr("attribute", null);
 }

 @Test
 public void testHasAttrMethod() {
 Node node = new Element("div").attr("class", "example");
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("nonExistentAttr"));
 }

 @Test
 public void testRemoveAttrMethod() {
 Node node = new Element("div").attr("class", "example");
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testAbsUrlMethod() {
 Node node = new Element("a").attr("href", "page.html");
 assertEquals("page.html", node.absUrl("href"));
 assertEquals("", node.absUrl("nonExistentAttr"));
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedUrlBaseUri() {
 Node node = new Element("div").setBaseUri("javascript:alert('Hello')");
 node.absUrl("href");
 }

 @Test
 public void testChildNodeMethod() {
 Node parent = new Element("ul");
 Node child = new Element("li").setText("Child");
 parent.addChildren(child);
 Node retrievedChild = parent.childNode(0);
 assertEquals(child, retrievedChild);
 }

 @Test
 public void testChildNodesMethod() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").setText("Child 1");
 Node child2 = new Element("li").setText("Child 2");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testParentMethod() {
 Element parent = new Element("ul");
 Element child = new Element("li").setText("Child");
 parent.addChildren(child);
 assertEquals(parent, child.parent());
 assertNull(child.parent());
 }

 @Test
 public void testOwnerDocumentMethod() {
 Node node = new Element("div").ownerDocument();
 assertNotNull(node.ownerDocument());
 }

 @Test
 public void testRemoveMethod() {
 Node parent = new Element("ul");
 Node child = new Element("li").setText("Child");
 parent.addChildren(child);
 parent.remove();
 assertNull(child.parentNode());
 }

 @Test
 public void testReplaceWithMethod() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").setText("Child 1");
 Node child2 = new Element("li").setText("Child 2");
 parent.addChildren(child1);
 parent.replaceWith(child2);
 assertEquals(child2, parent.childNode(0));
 assertEquals(child1, child1.parentNode());
 }

 @Test
 public void testSetParentNodeAndReplaceChildMethods() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").setText("Child 1");
 Node child2 = new Element("li").setText("Child 2");
 child1.setParentNode(parent);
 parent.addChildren(child1);
 parent.replaceChild(child1, child2);
 assertEquals(child2, parent.childNode(0));
 assertEquals(child1, child1.parentNode());
 }

 @Test
 public void testAddChildrenMethod() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").setText("Child 1");
 Node child2 = new Element("li").setText("Child 2");
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }

 @Test
 public void testAddChildrenWithIndexMethod() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").setText("Child 1");
 Node child2 = new Element("li").setText("Child 2");
 parent.addChildren(1, child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }
}