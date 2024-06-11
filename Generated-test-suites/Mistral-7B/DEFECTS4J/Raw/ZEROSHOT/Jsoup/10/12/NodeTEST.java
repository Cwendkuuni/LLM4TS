import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertNull(node.childNodes());
 }

 @Test
 public void testNodeWithAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "example");
 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertEquals("example", node.attr("class"));
 assertEquals(1, node.childNodes().size());
 assertNull(node.parentNode());
 }

 @Test
 public void testNodeWithParent() {
 Document doc = new Document();
 Element parent = new Element("ul");
 doc.appendChild(parent);
 Element child = new Element("li");
 parent.appendChild(child);
 assertEquals("ul", parent.parentNode().nodeName());
 assertEquals("li", child.parentNode().nodeName());
 }

 @Test
 public void testNodeWithChildren() {
 Document doc = new Document();
 Element parent = new Element("ul");
 doc.appendChild(parent);
 Element child1 = new Element("li");
 Element child2 = new Element("li");
 parent.appendChild(child1);
 parent.appendChild(child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals("li", child1.nodeName());
 assertEquals("li", child2.nodeName());
 assertNull(child1.parentNode());
 assertNull(child2.parentNode());
 }

 @Test
 public void testNodeWithTextChild() {
 Document doc = new Document();
 Element parent = new Element("ul");
 doc.appendChild(parent);
 TextNode textNode = new TextNode("test");
 parent.appendChild(textNode);
 assertEquals(1, parent.childNodes().size());
 assertEquals("text", textNode.nodeName());
 assertNull(textNode.parentNode());
 }

 @Test
 public void testNodeWithBaseUri() {
 String baseUri = "http://example.com";
 Node node = new Element("a", baseUri, new Attributes());
 assertEquals(baseUri, node.baseUri());
 assertEquals("", node.absUrl("href"));
 node.attr("href", "test.html");
 assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 @Test
 public void testNodeWithDeepClone() {
 Document doc = new Document();
 Element parent = new Element("ul");
 doc.appendChild(parent);
 Element child1 = new Element("li");
 Element child2 = new Element("li");
 parent.appendChild(child1);
 parent.appendChild(child2);
 Node clonedNode = child1.cloneNode(true);
 assertEquals(2, clonedNode.childNodes().size());
 assertNotNull(clonedNode.childNodes().get(0));
 assertNotNull(clonedNode.childNodes().get(1));
 }
}