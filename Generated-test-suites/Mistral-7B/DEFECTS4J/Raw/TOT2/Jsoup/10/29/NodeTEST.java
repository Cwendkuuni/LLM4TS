import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeConstructor() {
 Node node = new Node("", new Attributes());
 assertEquals("", node.baseUri());
 assertNotNull(node.attributes());
 assertNotNull(node.childNodes());
 assertFalse(node.hasChildren());
 }

 @Test
 public void testNodeAbstractMethod() {
 try {
 Node node = new Node() {
 @Override
 public String nodeName() {
 throw new UnsupportedOperationException();
 }
 };
 assertFalse(node instanceof Element);
 } catch (Exception e) {
 fail("Should not throw an exception when invoking an abstract method.");
 }
 }

 @Test
 public void testAttrMethod() {
 Element element = new Element("div") {
 @Override
 public String nodeName() {
 return "div";
 }
 };
 element.attr("class", "myclass");
 assertEquals("myclass", element.attr("class"));
 assertEquals("", element.attr("nonExistentAttr"));
 }

 @Test
 public void testHasAttrMethod() {
 Element element = new Element("div") {
 @Override
 public String nodeName() {
 return "div";
 }
 };
 element.attr("class", "myclass");
 assertTrue(element.hasAttr("class"));
 assertFalse(element.hasAttr("nonExistentAttr"));
 }

 @Test
 public void testRemoveAttrMethod() {
 Element element = new Element("div") {
 @Override
 public String nodeName() {
 return "div";
 }
 };
 element.attr("class", "myclass");
 element.removeAttr("class");
 assertFalse(element.hasAttr("class"));
 }

 @Test
 public void testAbsUrlMethod() {
 Element link = new Element("a") {
 @Override
 public String nodeName() {
 return "a";
 }
 };
 link.attr("href", "about.html");
 assertEquals("about.html", link.absUrl("href"));
 String baseUri = "http://example.com/index.html";
 link.setBaseUri(baseUri);
 assertEquals("http://example.com/about.html", link.absUrl("href"));
 }

 @Test
 public void testChildNodeMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChild(child);
 Node node = parent.childNode(0);
 assertEquals(child, node);
 }

 @Test
 public void testChildNodesMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child1 = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 Element child2 = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChild(child1);
 parent.addChild(child2);
 List<Node> nodes = parent.childNodes();
 assertEquals(2, nodes.size());
 assertEquals(child1, nodes.get(0));
 assertEquals(child2, nodes.get(1));
 }

 @Test
 public void testGetParentNodeMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 child.setParentNode(parent);
 assertEquals(parent, child.parentNode());
 }

 @Test(expected = NullPointerException.class)
 public void testGetParentNodeMethodNullParent() {
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 assertNull(child.parentNode());
 }

 @Test
 public void testOwnerDocumentMethod() {
 Element element = new Element("div") {
 @Override
 public String nodeName() {
 return "div";
 }
 };
 Document document = new Document() {
 @Override
 public String nodeName() {
 return "document";
 }
 };
 element.setOwnerDocument(document);
 assertEquals(document, element.ownerDocument());
 }

 @Test
 public void testRemoveMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChild(child);
 parent.remove();
 assertFalse(child.hasParentNode());
 }

 @Test
 public void testReplaceWithMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child1 = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 Element child2 = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChild(child1);
 parent.replaceWith(child2);
 assertEquals(child2, parent.childNode(0));
 assertEquals(child1, child1.parentNode()); // child1 still has a parent
 }

 @Test
 public void testSetParentNodeMethod() {
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 child.setParentNode(parent);
 assertEquals(parent, child.parentNode());
 }

 @Test(expected = NullPointerException.class)
 public void testSetParentNodeMethodNullParent() {
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 assertNull(child.parentNode());
 try {
 child.setParentNode(null);
 } finally {
 assertNull(child.parentNode());
 }
 }

 @Test
 public void testAddChildrenMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child1 = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 Element child2 = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNodes().get(0));
 assertEquals(child2, parent.childNodes().get(1));
 }

 @Test
 public void testAddChildrenMethodWithNullChildren() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 parent.addChildren();
 assertEquals(0, parent.childNodes().size());
 }

 @Test
 public void testAddChildrenMethodWithEmptyArray() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 parent.addChildren(new Node[0]);
 assertEquals(0, parent.childNodes().size());
 }

 @Test
 public void testReplaceChildMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element oldChild = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 Element newChild = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChild(oldChild);
 parent.replaceChild(newChild, oldChild);
 assertEquals(newChild, parent.childNode(0));
 assertEquals(oldChild, oldChild.parentNode()); // oldChild still has a parent
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceChildMethodNullChild() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element oldChild = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChild(oldChild);
 try {
 parent.replaceChild(null, oldChild);
 } finally {
 assertEquals(1, parent.childNodes().size());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceChildMethodWrongParentChild() {
 Element parent1 = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element parent2 = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent1.addChild(child);
 try {
 parent2.replaceChild(child, parent1);
 } finally {
 assertEquals(1, parent1.childNodes().size());
 assertEquals(0, parent2.childNodes().size());
 }
 }

 @Test
 public void testRemoveChildMethod() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element child = new Element("li") {
 @Override
 public String nodeName() {
 return "li";
 }
 };
 parent.addChild(child);
 parent.removeChild(child);
 assertFalse(child.hasParentNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveChildMethodNullChild() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 parent.removeChild(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveChildMethodOutOfBounds() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 parent.addChild(new TextNode("text"));
 parent.removeChild(0);
 }

 @Test
 public void testAddChildrenMethodWithArray() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Element[] children = {new Element("li"), new Element("li"), new Element("li")};
 parent.addChildren(children);
 assertEquals(3, parent.childNodes().size());
 }

 @Test
 public void testAddChildrenMethodWithList() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 List<Node> children = new ArrayList<Node>() {{
 add(new Element("li"));
 add(new Element("li"));
 add(new Element("li"));
 }};
 parent.addChildren(children);
 assertEquals(3, parent.childNodes().size());
 }

 @Test
 public void testAddChildrenMethodWithMixedTypes() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 Node[] children = {new Element("li"), new TextNode("text"), new Element("li")};
 parent.addChildren(children);
 assertEquals(3, parent.childNodes().size());
 }

 @Test
 public void testAddChildrenMethodWithStrings() {
 Element parent = new Element("ul") {
 @Override
 public String nodeName() {
 return "ul";
 }
 };
 String[] children = {"<li></li>", "<li></li>", "<li></li>"};
 parent.addChildren(children);
 assertEquals(3, parent.childNodes().size());
 }
}