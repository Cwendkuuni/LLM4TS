import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;
import static org.jsoup.helper.W3C.css;

public class NodeTest {

 private Node root;

 @Before
 public void setUp() {
 root = new Document().parse("<html><body><div id='test'></div></body></html>");
 }

 @Test
 public void testNodeCreation() {
 Node node = new Node("http://example.com", new Attributes());
 assertEquals("http://example.com", node.baseUri());
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.parentNode());
 }

 @Test
 public void testAbstractNodeMethods() {
 Node div = new Element("div");
 div.attr("id", "testDiv");
 div.attr("class", "testClass");
 div.attr("style", "color: red; font-size: 12px;");

 Node p = new Element("p");
 p.nodeName().equals("p");
 node.appendChild(p);
 node.appendChild(div);

 assertEquals("div", div.nodeName());
 assertEquals("testDiv", div.attr("id"));
 assertEquals("testClass", div.attr("class"));
 assertEquals("color: red; font-size: 12px;", div.attr("style"));
 assertEquals(2, node.childNodes().size());

 assertEquals("p", p.nodeName());
 assertEquals("", p.attr("id"));
 assertEquals("", p.attr("class"));
 assertEquals("", p.attr("style"));

 assertEquals("testDiv", div.outerHtml().substring(0, div.outerHtml().indexOf('>')));
 assertEquals("testClass", div.outerHtml().substring(div.outerHtml().indexOf(" class=\"")+7, div.outerHtml().indexOf("\" style"))) ;
 assertEquals("color: red; font-size: 12px;", div.outerHtml().substring(div.outerHtml().indexOf(" style=\"")+7));
 }

 @Test
 public void testAttributeAbsUrl() throws Exception {
 Node a = new Element("a").attr("href", "test.html");
 String base = "http://test.com/test/";
 root.appendChild(a);
 root.setBaseUri(base);
 String absUrl = a.absUrl("href");
 assertEquals("http://test.com/test/test.html", absUrl);
 }

 @Test
 public void testAttributeAbsUrlInvalid() throws Exception {
 Node a = new Element("a").attr("abs:href", "test.html");
 String base = "http://test.com/test/";
 root.appendChild(a);
 root.setBaseUri(base);
 String absUrl = a.absUrl("abs:href");
 assertEquals("", absUrl);
 }

 @Test
 public void testAttributeAbsUrlNullBase() throws Exception {
 Node a = new Element("a").attr("href", "test.html");
 root.appendChild(a);
 String absUrl = a.absUrl("href");
 assertEquals("", absUrl);
 }

 @Test
 public void testAttributeAbsUrlInvalidBase() throws Exception {
 Node a = new Element("a").attr("href", "test.html");
 String base = "http://test.com/";
 root.appendChild(a);
 root.setBaseUri(base);
 String absUrl = a.absUrl("href");
 assertEquals("", absUrl);
 }

 @Test
 public void testSetBaseUri() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p = new Element("p").attr("id", "testP");
 root.appendChild(div);
 root.appendChild(p);
 div.setBaseUri("http://example.com/");
 assertEquals("http://example.com/", div.baseUri());
 assertEquals("", p.baseUri().toString());
 }

 @Test
 public void testSetBaseUriNull() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p = new Element("p").attr("id", "testP");
 root.appendChild(div);
 root.appendChild(p);
 div.setBaseUri(null);
 assertEquals("", div.baseUri());
 assertEquals("", p.baseUri().toString());
 }

 @Test
 public void testSetBaseUriInvalid() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p = new Element("p").attr("id", "testP");
 root.appendChild(div);
 root.appendChild(p);
 try {
 div.setBaseUri("test");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals("", div.baseUri());
 assertEquals("", p.baseUri().toString());
 }

 @Test
 public void testChildNodes() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p1 = new Element("p").attr("id", "testP1");
 Node p2 = new Element("p").attr("id", "testP2");
 Node p3 = new Element("p").attr("id", "testP3");
 root.appendChild(div);
 div.appendChild(p1);
 div.appendChild(p2);
 div.appendChild(p3);

 assertEquals(3, div.childNodes().size());
 assertEquals("p1", p1.nodeName());
 assertEquals("p2", p2.nodeName());
 assertEquals("p3", p3.nodeName());
 }

 @Test
 public void testChildNodesEmpty() {
 Node div = new Element("div").attr("id", "testDiv");
 root.appendChild(div);
 assertEquals(0, div.childNodes().size());
 }

 @Test
 public void testChildNodesNull() {
 Node div = new Element("div").attr("id", "testDiv");
 root.setBaseUri("http://test.com/test/");
 div.setBaseUri("http://example.com/");
 assertEquals(0, div.childNodes().size());
 }

 @Test
 public void testChildNodesInvalid() {
 Node div = new Element("div").attr("id", "testDiv");
 root.setBaseUri("http://test.com/test/");
 div.setBaseUri("test");
 assertEquals(0, div.childNodes().size());
 }

 @Test
 public void testGetParentNode() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p = new Element("p").attr("id", "testP");
 root.appendChild(div);
 div.appendChild(p);

 assertEquals(div, p.parentNode());
 assertEquals(null, root.parentNode());
 }

 @Test
 public void testGetParentNodeNull() {
 Node div = new Element("div").attr("id", "testDiv");
 assertEquals(null, div.parentNode());
 }

 @Test
 public void testGetParentNodeInvalid() {
 Node div = new Element("div").attr("id", "testDiv");
 root.appendChild(div);
 Node p = new Element("p").attr("id", "testP");
 try {
 div.setParentNode(p);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals(null, div.parentNode());
 }

 @Test
 public void testGetDocument() {
 assertEquals(root, root.ownerDocument());
 assertEquals(null, new Element("p").ownerDocument());
 }

 @Test
 public void testRemove() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p = new Element("p").attr("id", "testP");
 root.appendChild(div);
 div.appendChild(p);

 assertEquals(2, root.childNodes().size());
 div.remove();
 assertEquals(1, root.childNodes().size());
 assertEquals(null, p.parentNode());
 }

 @Test
 public void testReplaceChild() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p1 = new Element("p").attr("id", "testP1");
 Node p2 = new Element("p").attr("id", "testP2");
 root.appendChild(div);
 div.appendChild(p1);
 assertEquals(1, div.childNodes().size());

 Node newDiv = new Element("div").attr("id", "newDiv");
 div.replaceChild(p1, newDiv);
 assertEquals(1, div.childNodes().size());
 assertEquals("div", newDiv.nodeName());
 assertEquals("newDiv", newDiv.attr("id"));
 }

 @Test
 public void testReplaceChildNull() {
 Node div = new Element("div").attr("id", "testDiv");
 Node p1 = new Element("p").attr("id", "testP1");
 root.appendChild(div);
 div.appendChild(p1);

 Node newDiv = null;
 try {
 div.replaceChild(p1, newDiv);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 assertEquals(1, div.childNodes().size());
 }
}