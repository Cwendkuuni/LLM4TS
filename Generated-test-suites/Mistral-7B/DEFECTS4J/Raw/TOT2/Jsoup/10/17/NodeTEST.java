import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNotNull(node.attributes());
 assertEmpty(node.childNodes());
 }

 @Test
 public void testNodeWithAttributes() {
 Node node = new Element("a").attr("href", "http://example.com");
 assertEquals("a", node.nodeName());
 assertEquals("http://example.com", node.absUrl("href"));
 }

 @Test
 public void testNodeWithBaseUri() {
 Document doc = new Document();
 Node node = new Element("a").setBaseUri("http://example.com").attr("href", "test");
 assertEquals("http://example.com/test", node.absUrl("href"));
 }

 @Test
 public void testNodeWithChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p").setText("Child 1");
 Node child2 = new Element("p").setText("Child 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }

 @Test
 public void testNodeRemoval() {
 Node parent = new Element("div");
 Node child = new Element("p").setText("Child");
 parent.appendChild(child);
 parent.removeChild(child);
 assertFalse(parent.hasChildNodes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNodeNullAttributes() {
 Node node = new Element("a");
 node.attr(null, "value");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNodeInvalidAttributeKey() {
 Node node = new Element("a");
 node.attr("abs:href", "value"); // invalid key starts with 'abs:'
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNodeChildAccessOutOfBounds() {
 Node parent = new Element("div");
 Node child = new Element("p").setText("Child");
 parent.appendChild(child);
 assertEquals(child, parent.childNode(0));
 assertEquals(-1, parent.childNodes().indexOf(child)); // indexOf returns -1 for non-existent elements
 try {
 parent.childNode(1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testNodeClone() {
 Node original = new Element("div");
 Node cloned = original.clone();
 assertNotSame(original, cloned);
 assertEquals("div", cloned.nodeName());
 assertEquals(original.attributes(), cloned.attributes());
 assertEquals(original.childNodes(), cloned.childNodes());
 }

 @Test
 public void testNodeAttributesClone() {
 Node node = new Element("a").attr("href", "http://example.com").attr("title", "Test");
 Attributes attrs = node.attributes();
 Node clonedNode = new Element("a").attr(attrs);
 assertEquals(attrs, clonedNode.attributes());
 }

 @Test
 public void testNodeDeepClone() {
 Document originalDoc = new Document();
 Node originalRoot = new Element("html").attr("xmlns:html", "http://www.w3.org/1999/xhtml");
 originalDoc.setRootElem(originalRoot);
 Node clonedRoot = originalRoot.clone();
 Document clonedDoc = new Document();
 clonedDoc.setRootElem(clonedRoot);
 assertEquals(originalDoc, clonedDoc);
 }
}