import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNotNull(node.attributes());
 assertNotNull(node.baseUri());
 assertNull(node.childNodes());
 }

 @Test
 public void testNodeAttributes() {
 Node node = new Element("a").attr("href", "http://www.google.com");
 assertEquals("a", node.nodeName());
 assertEquals("http://www.google.com", node.absUrl("href"));
 assertNotNull(node.attributes());
 assertEquals("href", node.attributes().keySet().iterator().next());
 assertEquals("http://www.google.com", node.attributes().get("href"));
 }

 @Test
 public void testNodeChildNodes() {
 Node parent = new Element("div");
 Node child1 = new Element("p").text("Child 1");
 Node child2 = new Element("p").text("Child 2");
 Node child3 = new TextNode("Text Node");

 parent.addChildren(child1, child2, child3);
 List<Node> children = parent.childNodes();
 assertEquals(3, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 assertEquals(child3, children.get(2));
 }

 @Test
 public void testNodeParentNode() {
 Node child = new Element("p").text("Child");
 Node parent = new Element("div").addChild(child);
 assertEquals(parent, child.parentNode());
 assertNull(child.parent());
 }

 @Test
 public void testNodeClone() {
 Node original = new Element("div").text("Original");
 Node cloned = original.clone();
 assertEquals(original.nodeName(), cloned.nodeName());
 assertEquals(original.text(), cloned.text());
 assertNotSame(original, cloned);
 }

 @Test
 public void testNodeToString() {
 Node node = new Element("p").text("Test");
 assertEquals("<p>Test</p>", node.toString());
 }

 @Test
 public void testNodeEqualsAndHashcode() {
 Node node1 = new Element("p").text("Test 1");
 Node node2 = new Element("p").text("Test 1");
 Node node3 = new Element("p").text("Test 2");

 assertTrue(node1.equals(node1));
 assertTrue(node1.equals(node2));
 assertFalse(node1.equals(node3));

 int hashCode1 = node1.hashCode();
 int hashCode2 = node2.hashCode();
 int hashCode3 = node3.hashCode();

 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 }

 @Test
 public void testNodeOuterHtml() {
 Node node = new Element("div").attr("class", "test").addChild(new TextNode("Test"));
 StringBuilder stringBuilder = new StringBuilder();
 node.outerHtml(stringBuilder);

 String expected = "<div class=\"test\">Test</div>";
 assertEquals(expected, stringBuilder.toString());
 }
}