import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.nodes.AttributesImpl.NamedAttribute;
import org.jsoup.nodes.Traversing;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testCreateNode() {
 Node node = new Element("div");
 assertTrue(node instanceof Element);
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertNull(node.childNodes());
 }

 @Test
 public void testCreateNodeWithAttributes() {
 String baseUri = "http://example.com";
 AttributesImpl attrs = new AttributesImpl();
 attrs.put("id", "myId");
 attrs.put("class", "myClass");

 Node node = new Element("div", baseUri, attrs);
 assertEquals("div", node.nodeName());
 assertNotSame(baseUri, node.baseUri()); // new baseUri is created
 assertEquals("myId", node.attr("id"));
 assertEquals("myClass", node.attr("class"));
 assertNotNull(node.attributes());
 }

 @Test
 public void testCreateNodeWithoutAttributes() {
 Node node = new Element("div", "http://example.com");
 assertEquals("div", node.nodeName());
 assertNotNull(node.baseUri());
 assertNull(node.attributes());
 }

 @Test
 public void testCreateEmptyNode() {
 Node node = new Node();
 assertSame(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 }

 @Test
 public void testGetAttribute() {
 Element element = new Element("div");
 element.attr("id", "myId");
 assertEquals("myId", element.attr("id"));
 assertEquals("", element.attr("nonExistentAttr"));
 }

 @Test
 public void testGetAttributeWithAbsoluteUrl() {
 Element element = new Element("a");
 element.attr("href", "http://example.com");
 assertEquals("http://example.com", element.absUrl("href"));
 }

 @Test
 public void testGetAttributeWithRelativeUrl() {
 Element element = new Element("a");
 element.attr("href", "relative.html");
 String baseUri = "http://example.com";
 assertEquals("relative.html", element.attr("abs:href"));
 assertEquals("http://example.com/relative.html", element.absUrl("abs:href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentForBaseUri() {
 new Element("div", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentForAttributes() {
 new Element("div", "", null);
 }

 @Test
 public void testAddChild() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.addChildren(child);
 assertEquals(1, parent.childNodes().size());
 assertEquals(child, parent.childNodes().get(0));
 }

 @Test
 public void testAddChildren() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 }

 @Test
 public void testReplaceChild() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1);
 parent.replaceChild(child1, child2);
 assertEquals(1, parent.childNodes().size());
 assertEquals(child2, parent.childNodes().get(0));
 }

 @Test
 public void testRemoveChild() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 parent.removeChild(child1);
 assertEquals(1, parent.childNodes().size());
 assertNull(parent.childNodes().get(0).parentNode());
 }

 @Test
 public void testSetParentNode() {
 Element parent = new Element("div");
 Element child = new Element("p");
 child.setParentNode(parent);
 assertEquals(parent, child.parentNode());
 }

 @Test
 public void testSetSiblingIndex() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, sibling2);
 sibling1.setSiblingIndex(0);
 sibling2.setSiblingIndex(1);
 assertEquals(0, sibling1.siblingIndex());
 assertEquals(1, sibling2.siblingIndex());
 }

 @Test
 public void testGetNextSibling() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, sibling2);
 assertEquals(sibling2, sibling1.nextSibling());
 assertEquals(null, sibling2.nextSibling());
 }

 @Test
 public void testGetPreviousSibling() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, sibling2);
 assertEquals(sibling1, sibling2.previousSibling());
 assertEquals(null, sibling1.previousSibling());
 }

 @Test
 public void testToString() {
 Element element = new Element("div");
 element.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings());
 element.outerHtmlTail(new StringBuilder(), 0, new Document.OutputSettings());
 String result = element.toString();
 assertTrue(result.contains("<div>"));
 assertTrue(result.contains("</div>"));
 }

 @Test
 public void testEquals() {
 Element element1 = new Element("div");
 Element element2 = new Element("div");
 assertFalse(element1.equals(element2));
 }

 @Test
 public void testHashCode() {
 Element element1 = new Element("div");
 Element element2 = new Element("div");
 int hashCode1 = element1.hashCode();
 int hashCode2 = element2.hashCode();
 assertNotSame(hashCode1, hashCode2);

 Element element3 = new Element("div");
 element3.setParentNode(element1);
 int hashCode3 = element3.hashCode();
 assertEquals(hashCode1, hashCode3);
 }
}