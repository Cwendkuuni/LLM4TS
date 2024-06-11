import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeTraversor;
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
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testNodeCreationWithAttributes() {
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "someClass");
 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertEquals("someClass", node.attr("class"));
 assertNotNull(node.attributes());
 }

 @Test
 public void testNodeCreationWithoutAttributesOrBaseUri() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 }

 @Test
 public void testSettingAndGettingAttribute() {
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "someClass");
 Node node = new Element("div", baseUri, attributes);
 node.attr("class", "anotherClass");
 assertEquals("anotherClass", node.attr("class"));
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullAttributeKey() {
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 Node node = new Element("div", baseUri, attributes);
 node.attr(null, "someValue");
 }

 @Test
 public void testSettingAndGettingAttributeWithAbsoluteUrl() {
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "/path/to/page.html");
 Node node = new Element("a", baseUri, attributes);
 assertEquals("/path/to/page.html", node.absUrl("href"));
 }

 @Test
 public void testSettingAndGettingAttributeWithRelativeUrl() {
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "path/to/page.html");
 Node node = new Element("a", baseUri, attributes);
 assertEquals("path/to/page.html", node.attr("href"));
 assertEquals("https://example.com/path/to/page.html", node.absUrl("href"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGettingChildAtInvalidIndex() {
 Node parentNode = new Element("div");
 Node node = new Element("p");
 parentNode.addChildren(node);
 parentNode.removeChild(node); // remove it, so it's not in the list anymore
 Node childAtInvalidIndex = parentNode.childNode(1);
 }

 @Test
 public void testGettingChildren() {
 Node parentNode = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parentNode.addChildren(child1, child2);
 List<Node> children = parentNode.childNodes();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 }

 @Test
 public void testSettingAndGettingParent() {
 Node child = new Element("p");
 Node parent = new Element("div");
 child.setParentNode(parent);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testSettingAndGettingParentNull() {
 Node child = new Element("p");
 child.setParentNode(null);
 assertNull(child.parent());
 }

 @Test
 public void testSettingAndGettingBaseUri() {
 String baseUri = "https://example.com";
 Node node = new Element("div", baseUri, new Attributes());
 assertEquals(baseUri, node.baseUri());
 node.setBaseUri("https://another-example.com");
 assertEquals("https://another-example.com", node.baseUri());
 }

 @Test(expected = MalformedURLException.class)
 public void testSettingInvalidBaseUri() {
 String baseUri = "https://example.com/not-a-url";
 Node node = new Element("div", baseUri, new Attributes());
 }

 @Test
 public void testChildNodesAsArray() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 Node[] childrenArray = parent.childNodesAsArray();
 assertEquals(2, childrenArray.length);
 assertEquals(child1, childrenArray[0]);
 assertEquals(child2, childrenArray[1]);
 }

 @Test
 public void testRemovingChild() {
 Node parent = new Element("div");
 Node child = new Element("p");
 parent.addChildren(child);
 parent.removeChild(child);
 assertTrue(parent.childNodes().isEmpty());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemovingChildAtInvalidIndex() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 parent.removeChild(2);
 }

 @Test
 public void testAddingChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 }

 @Test
 public void testAddingChildrenMultiple() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 Node child3 = new Element("p");
 parent.addChildren(child1, child2, child3);
 List<Node> children = parent.childNodes();
 assertEquals(3, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 assertTrue(children.contains(child3));
 }

 @Test
 public void testAddingChildrenNull() {
 Node parent = new Element("div");
 parent.addChildren(null);
 List<Node> children = parent.childNodes();
 assertEquals(0, children.size());
 }

 @Test
 public void testAddingChildrenEmptyArray() {
 Node parent = new Element("div");
 parent.addChildren(new Node[0]);
 List<Node> children = parent.childNodes();
 assertEquals(0, children.size());
 }

 @Test
 public void testTraversingChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 NodeTraversor traversor = new NodeTraversor();
 traversor.addVisitor(new NodeVisitor() {
 @Override
 public void head(Node node, int depth) {
 System.out.println("Head of node: " + node.nodeName());
 }

 @Override
 public void tail(Node node, int depth) {
 System.out.println("Tail of node: " + node.nodeName());
 }
 });
 traversor.traverse(parent);
 }
}