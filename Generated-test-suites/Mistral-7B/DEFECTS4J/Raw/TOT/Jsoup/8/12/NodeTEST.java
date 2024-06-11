import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.ClassesAttribute;
import org.jsoup.nodes.attributes.HrefAttribute;
import org.jsoup.nodes.attributes.IdAttribute;
import org.jsoup.nodes.attributes.StyleAttribute;
import static org.junit.Assert.*;

public class NodeTest {

 private Node rootNode;

 @Before
 public void setUp() {
 Document doc = new Document();
 rootNode = doc.body();

 Element h1 = doc.createElement("h1");
 h1.attr("id", "test-id");
 h1.attr("class", "test-class");
 h1.attr("style", "color: red; font-size: 24px;");

 Element p = doc.createElement("p");
 p.text("This is a test.");
 p.attr("id", "test-p");

 Element a = doc.createElement("a");
 a.attr("href", "/test.html");
 a.text("Test link");

 h1.appendChild(a);
 h1.appendChild(p);
 rootNode.appendChild(h1);
 }

 @Test
 public void testNodeName() {
 assertEquals("h1", rootNode.childNodes().get(0).nodeName());
 }

 @Test
 public void testAttr() {
 assertEquals("test-id", rootNode.childNodes().get(0).attr("id"));
 assertEquals("/test.html", rootNode.childNodes().get(0).childNodes().get(0).absUrl("href"));
 }

 @Test
 public void testAttributes() {
 Attributes attributes = rootNode.childNodes().get(0).attributes();

 assertTrue(attributes.hasKey("style"));
 assertTrue(attributes.hasKey("class"));
 assertTrue(attributes.hasKey("id"));

 assertEquals("test-class", attributes.get("class"));
 assertEquals("color: red; font-size: 24px;", attributes.get("style"));
 assertEquals("test-id", attributes.get("id"));
 }

 @Test
 public void testHasAttr() {
 assertTrue(rootNode.childNodes().get(0).hasAttr("id"));
 assertTrue(rootNode.childNodes().get(0).childNodes().get(0).hasAttr("href"));
 assertFalse(rootNode.childNodes().get(0).hasAttr("non-existent-attribute"));
 }

 @Test
 public void testRemoveAttr() {
 Element h1 = (Element) rootNode.childNodes().get(0);
 h1.removeAttr("id");
 assertFalse(h1.hasAttr("id"));
 }

 @Test
 public void testSetAttr() {
 Element h1 = (Element) rootNode.childNodes().get(0);
 h1.attr("title", "Test title");
 assertEquals("Test title", h1.attr("title"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Node node = new Element("div");
 node.setBaseUri(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributes() {
 Node node = new Element("div");
 node.setBaseUri("");
 node.setAttributes(null);
 }

 @Test
 public void testAbsUrlWithEmptyBaseUri() {
 Element a = new Element("a");
 a.attr("href", "some/path");
 String absoluteUrl = a.absUrl("href");
 assertEquals("some/path", absoluteUrl);
 }

 @Test
 public void testAbsUrlWithAbsoluteBaseUri() {
 Element a = new Element("a");
 a.attr("href", "http://example.com/path");
 Document doc = new Document();
 Node node = doc.createElement("div");
 node.setBaseUri("http://example.com");
 String absoluteUrl = node.absUrl(a.nodeName() + ":href");
 assertEquals("http://example.com/path", absoluteUrl);
 }

 @Test
 public void testAbsUrlWithRelativeBaseUri() {
 Element a = new Element("a");
 a.attr("href", "path");
 Document doc = new Document();
 Node node = doc.createElement("div");
 node.setBaseUri("http://example.com/base-path");
 String absoluteUrl = node.absUrl(a.nodeName() + ":href");
 assertEquals("http://example.com/base-path/path", absoluteUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testAbsUrlWithInvalidBaseUri() {
 Element a = new Element("a");
 a.attr("href", "http://example.com/path");
 Node node = new Element("div");
 node.setBaseUri("invalid:base-uri");
 String absoluteUrl = node.absUrl(a.nodeName() + ":href");
 }

 @Test
 public void testChildNodes() {
 assertEquals(2, rootNode.childNodes().size());
 }

 @Test
 public void testChildNodeByIndex() {
 assertEquals(h1, rootNode.childNodes().get(0));
 assertEquals(p, rootNode.childNodes().get(1));
 }

 @Test
 public void testRemoveChild() {
 rootNode.removeChild(rootNode.childNodes().get(0));
 assertEquals(1, rootNode.childNodes().size());
 }

 @Test
 public void testReplaceChild() {
 Element newP = doc.createElement("p");
 newP.text("New test.");
 rootNode.replaceChild(rootNode.childNodes().get(0), newP);
 assertEquals(1, rootNode.childNodes().size());
 assertEquals("New test.", rootNode.childNodes().get(0).text());
 }

 @Test
 public void testAddChildren() {
 Element newH1 = doc.createElement("h1");
 newH1.text("New heading.");
 rootNode.addChildren(newH1);
 assertEquals(3, rootNode.childNodes().size());
 assertEquals("New heading.", rootNode.childNodes().get(2).text());
 }

 @Test
 public void testAddChildrenWithIndex() {
 Element newH1 = doc.createElement("h1");
 newH1.text("New heading.");
 rootNode.addChildren(1, newH1);
 assertEquals(3, rootNode.childNodes().size());
 assertEquals("New heading.", rootNode.childNodes().get(1).text());
 }
}