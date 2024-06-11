import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class NodeTest {

 private Node rootNode;

 @Before
 public void setUp() {
 rootNode = new Document().createElement("html");
 rootNode.attr("baseURI", "http://example.com");
 }

 @Test
 public void testNodeCreation() {
 Node node = new Node("http://example.com", new Attributes());
 assertEquals("http://example.com", node.baseUri());
 assertNull(node.parentNode());
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 }

 @Test
 public void testAbstractNodeMethods() {
 Element element = rootNode.createElement("div");
 element.attr("id", "test");
 element.attr("class", "test");

 assertEquals("div", element.nodeName());
 assertEquals("test", element.attr("id"));
 assertEquals("test", element.attr("class"));
 assertTrue(element.hasAttr("id"));
 assertTrue(element.hasAttr("class"));
 assertFalse(element.hasAttr("nonexistent"));

 Attribute attr = element.attr("id");
 assertEquals("test", attr.getValue());

 assertEquals(element.attributes(), element.attributes());
 assertEquals(1, element.attributes().size());

 element.attr("id", "newtest");
 assertEquals("newtest", element.attr("id"));

 element.removeAttr("id");
 assertFalse(element.hasAttr("id"));
 }

 @Test
 public void testAbsUrl() {
 Element link = rootNode.createElement("a");
 link.attr("href", "/test.html");
 assertEquals("", link.absUrl("href"));
 assertEquals("http://example.com/test.html", link.absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsUrlNegativeAttributeKey() {
 Element link = rootNode.createElement("a");
 link.attr("href", "/test.html");
 link.absUrl("-1");
 }

 @Test
 public void testChildNode() {
 Element child = rootNode.createElement("p");
 rootNode.appendChild(child);
 Node childNode = rootNode.childNode(0);
 assertEquals(child, childNode);
 }

 @Test
 public void testChildNodes() {
 Element child1 = rootNode.createElement("p");
 Element child2 = rootNode.createElement("p");
 rootNode.appendChild(child1);
 rootNode.appendChild(child2);
 List<Node> childNodes = rootNode.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(child1, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 @Test
 public void testRemoveNode() {
 Element child = rootNode.createElement("p");
 rootNode.appendChild(child);
 rootNode.removeChild(child);
 assertFalse(rootNode.hasChildNodes());
 }

 @Test
 public void testSetBaseUri() {
 Element element = rootNode.createElement("div");
 element.setBaseUri("http://test.com");
 assertEquals("http://test.com", element.baseUri());
 }
}