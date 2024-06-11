import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesFactory;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

 private Node root;

 @Before
 public void setUp() {
 root = new Document();
 }

 @Test
 public void testEmptyNodeCreation() {
 Node node = new Node();
 assertNull(node.parentNode);
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertNull(node.nodeName());
 }

 @Test
 public void testNodeCreationWithBaseUriAndAttributes() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("class", "example").build();
 Node node = new Node(baseUri, attributes);
 assertEquals(baseUri, node.baseUri());
 assertEquals(attributes, node.attributes());
 assertNull(node.parentNode);
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 }

 @Test
 public void testNodeWithChildren() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("class", "example").build();
 Node parent = new Node(baseUri, attributes);
 Node child1 = new Node(baseUri, attributes);
 Node child2 = new Node(baseUri, attributes);
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testNodeWithAttribute() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("href", "/test").build();
 Node link = new Node(baseUri, attributes);
 assertEquals("", link.attr("class"));
 assertEquals("/test", link.absUrl("href"));
 }

 @Test
 public void testNodeWithAbsoluteAttribute() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("src", "http://example.com/image.jpg").build();
 Node image = new Node(baseUri, attributes);
 assertEquals("http://example.com/image.jpg", image.absUrl("src"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Node node = new Node(null, new Attributes());
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributes() {
 Node node = new Node("", null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = MalformedURLException.class)
 public void testInvalidBaseUri() {
 String invalidBaseUri = "http://example.com:1234/";
 Node node = new Node(invalidBaseUri, new Attributes());
 fail("Expected MalformedURLException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributeKey() {
 Node link = new Node("", new Attributes());
 try {
 link.attr(null, "value");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetDocument() {
 Document doc = new Document();
 Node node = new Element("div", doc);
 assertEquals(doc, node.ownerDocument());
 }

 @Test
 public void testRemovalFromParent() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("class", "example").build();
 Node parent = new Node(baseUri, attributes);
 Node child = new Node(baseUri, attributes);
 parent.addChildren(child);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 parent.removeChild(child);
 List<Node> remainingChildren = parent.childNodes();
 assertEquals(0, remainingChildren.size());
 }

 @Test
 public void testReplaceChild() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("class", "example").build();
 Node parent = new Node(baseUri, attributes);
 Node child1 = new Node(baseUri, attributes);
 Node child2 = new Node(baseUri, attributes);
 parent.addChildren(child1);
 parent.replaceChild(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 assertEquals(child2, children.get(0));
 }

 @Test
 public void testAddChildren() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("class", "example").build();
 Node parent = new Node(baseUri, attributes);
 Node child1 = new Node(baseUri, attributes);
 Node child2 = new Node(baseUri, attributes);
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testChildNodesAsArray() {
 String baseUri = "http://example.com/";
 Attributes attributes = AttributesFactory.create().add("class", "example").build();
 Node parent = new Node(baseUri, attributes);
 Node child1 = new Node(baseUri, attributes);
 Node child2 = new Node(baseUri, attributes);
 parent.addChildren(child1, child2);
 Node[] childrenAsArray = parent.childNodesAsArray();
 assertEquals(2, childrenAsArray.length);
 assertEquals(child1, childrenAsArray[0]);
 assertEquals(child2, childrenAsArray[1]);
 }
}