import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 private Node rootNode;

 @Before
 public void setUp() {
 rootNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "html";
 }
 };
 }

 @Test
 public void testCreateNode() {
 Node newNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "body";
 }
 };

 assertEquals("body", newNode.nodeName());
 assertEquals("", newNode.baseUri());
 assertTrue(newNode.childNodes().isEmpty());
 assertTrue(newNode.hasAttr(""));
 assertTrue(newNode.attributes().isEmpty());
 }

 @Test
 public void testCreateNodeWithAttributes() {
 Node newNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "a";
 }
 };
 newNode.attr("href", "http://example.com");
 newNode.attr("title", "Example Title");

 assertEquals("a", newNode.nodeName());
 assertEquals("", newNode.baseUri());
 assertTrue(newNode.childNodes().isEmpty());
 assertEquals("href=\"http://example.com\" title=\"Example Title\"", newNode.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateNodeNullBaseUri() {
 new Node(null, new Attributes());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateNodeNullAttributes() {
 new Node("", null);
 }

 @Test
 public void testSetBaseUri() {
 Node newNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "img";
 }
 };
 newNode.setBaseUri("http://example.com");
 assertEquals("http://example.com", newNode.baseUri());
 }

 @Test
 public void testGetBaseUri() {
 Node newNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "img";
 }
 };
 newNode.setBaseUri("http://example.com");
 assertEquals("http://example.com", newNode.baseUri());
 }

 @Test
 public void testGetAbsUrl() {
 Node newNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "a";
 }
 };
 newNode.attr("href", "relative.html");
 assertEquals("", newNode.absUrl("href"));
 newNode.setBaseUri("http://example.com");
 assertEquals("http://example.com/relative.html", newNode.absUrl("href"));
 }

 @Test
 public void testGetAbsUrlEmptyBaseUri() {
 Node newNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "a";
 }
 };
 newNode.attr("href", "relative.html");
 assertEquals("", newNode.absUrl("href"));
 }

 @Test
 public void testGetAbsUrlInvalidBaseUri() {
 Node newNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "a";
 }
 };
 newNode.attr("href", "relative.html");
 newNode.setBaseUri("http://example.com");
 try {
 newNode.absUrl("bogus");
 fail("Expected MalformedURLException");
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testGetChildNodeByIndex() {
 Node parentNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "div";
 }
 };
 Node child1 = new TextNode("Child 1");
 Node child2 = new TextNode("Child 2");
 parentNode.addChildren(child1, child2);
 assertEquals(child1, parentNode.childNode(0));
 assertEquals(child2, parentNode.childNode(1));
 }

 @Test
 public void testGetChildren() {
 Node parentNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "div";
 }
 };
 Node child1 = new TextNode("Child 1");
 Node child2 = new TextNode("Child 2");
 parentNode.addChildren(child1, child2);
 assertEquals(2, parentNode.childNodes().size());
 }

 @Test
 public void testSetParentNode() {
 Node child = new TextNode("Child");
 Node parent = new TextNode("Parent");
 child.setParentNode(parent);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testRemoveChild() {
 Node parent = new TextNode("Parent");
 Node child = new TextNode("Child");
 parent.addChild(child);
 parent.removeChild(child);
 assertTrue(parent.childNodes().isEmpty());
 }

 @Test
 public void testReplaceChild() {
 Node parent = new TextNode("Parent");
 Node oldChild = new TextNode("Old Child");
 Node newChild = new TextNode("New Child");
 parent.addChild(oldChild);
 parent.replaceChild(newChild, oldChild);
 assertEquals(newChild, parent.childNode(0));
 }

 @Test
 public void testAddChildren() {
 Node parentNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Node child1 = new TextNode("Child 1");
 Node child2 = new TextNode("Child 2");
 parentNode.addChildren(child1, child2);
 assertEquals(2, parentNode.childNodes().size());
 }

 @Test
 public void testAddChildrenEmptyArray() {
 Node parentNode = new Node("", new Attributes()) {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 parentNode.addChildren();
 assertTrue(parentNode.childNodes().isEmpty());
 }
}