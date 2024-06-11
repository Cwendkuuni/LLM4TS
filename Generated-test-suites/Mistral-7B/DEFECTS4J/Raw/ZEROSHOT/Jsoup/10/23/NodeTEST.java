import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Node;
import static org.jsoup.helper.W3C.*;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 Assert.assertEquals("div", node.nodeName());
 Assert.assertNull(node.parentNode());
 Assert.assertNull(node.baseUri());
 Assert.assertNull(node.childNodes());
 Assert.assertNull(node.attributes());
 }

 @Test
 public void testNodeWithAttributes() {
 Document doc = new Document();
 Element node = new Element("a", doc);
 node.attr("href", "http://example.com");
 node.attr("class", "exampleClass");

 Assert.assertEquals("a", node.nodeName());
 Assert.assertEquals("http://example.com", node.absUrl("href"));
 Assert.assertEquals("exampleClass", node.attr("class"));
 Assert.assertEquals(2, node.attributes().size());
 }

 @Test
 public void testNodeWithBaseUri() {
 Document doc = new Document();
 doc.baseUri("http://example.com");
 Element node = new Element("a", doc);
 node.attr("href", "relativePath");

 Assert.assertEquals("a", node.nodeName());
 Assert.assertEquals("http://example.com/relativePath", node.absUrl("href"));
 }

 @Test
 public void testNodeWithChildren() {
 Document doc = new Document();
 Element parent = new Element("div", doc);
 Element child1 = new Element("p", doc);
 Element child2 = new Element("p", doc);

 parent.addChildren(child1, child2);

 Assert.assertEquals(2, parent.childNodes().size());
 Assert.assertEquals("div", parent.nodeName());
 Assert.assertEquals(child1, parent.childNode(0));
 Assert.assertEquals(child2, parent.childNode(1));
 }

 @Test
 public void testNodeWithParent() {
 Document doc = new Document();
 Element parent = new Element("div", doc);
 Element child = new Element("p", doc);

 parent.addChildren(child);
 child.setParentNode(parent);

 Assert.assertEquals(child, parent.childNode(0));
 Assert.assertEquals(parent, child.parentNode());
 }

 @Test
 public void testNodeWithSibling() {
 Document doc = new Document();
 Element parent = new Element("div", doc);
 Element sibling1 = new Element("p", doc);
 Element sibling2 = new Element("p", doc);
 Element child = new Element("p", doc);

 parent.addChildren(sibling1, child, sibling2);

 Assert.assertEquals(3, parent.childNodes().size());
 Assert.assertEquals(sibling1, parent.childNode(0));
 Assert.assertEquals(child, parent.childNode(1));
 Assert.assertEquals(sibling2, parent.childNode(2));
 }

 @Test
 public void testNodeWithOuterHtml() {
 Document doc = new Document();
 Element div = new Element("div", doc);
 Element p = new Element("p", doc);
 div.addChildren(p);
 p.outerHtml("Hello World");

 Assert.assertEquals("<div><p>Hello World</p></div>", div.outerHtml());
 }

 @Test
 public void testNodeClone() {
 Document doc = new Document();
 Element parent = new Element("div", doc);
 Element child = new Element("p", doc);

 parent.addChildren(child);
 Node clonedChild = child.clone();

 Assert.assertEquals(child, parent.childNode(0));
 Assert.assertEquals(clonedChild, parent.childNode(1));
 Assert.assertEquals(child.nodeName(), clonedChild.nodeName());
 Assert.assertEquals(child.baseUri(), clonedChild.baseUri());
 Assert.assertEquals(child.childNodes(), clonedChild.childNodes());
 Assert.assertEquals(child.attributes(), clonedChild.attributes());
 }
}