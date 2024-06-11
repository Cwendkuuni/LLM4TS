import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Validate.notNull(node.attributes());
 }

 @Test
 public void testNodeConstructorWithBaseUriAndAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node node = new Element("div", baseUri, attributes);
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Validate.notNull(node.attributes());
 Validate.assertEquals("div", node.nodeName());
 Validate.assertEquals("test-id", node.attr("id"));
 Validate.assertEquals(baseUri, node.baseUri());
 }

 @Test
 public void testNodeConstructorWithoutAttributes() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Validate.notNull(node.attributes());
 Validate.assertEquals("div", node.nodeName());
 Validate.assertEquals(baseUri, node.baseUri());
 Validate.assertEquals(Collections.emptyMap(), node.attributes());
 }

 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Element();
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Validate.assertNull(node.baseUri());
 Validate.assertNull(node.attributes());
 }

 @Test
 public void testNodeGettersAndSetters() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node node = new Element("div", baseUri, attributes);
 Validate.notNull(node.parentNode);
 Validate.assertEquals(baseUri, node.baseUri());
 Validate.assertEquals(attributes, node.attributes());

 Node parentNode = new Element("body");
 node.setParentNode(parentNode);
 Validate.assertEquals(parentNode, node.parentNode);

 Node childNode = new Element("p");
 node.addChild(childNode);
 Validate.assertEquals(1, node.childNodes().size());
 Validate.assertEquals(childNode, node.childNodes().get(0));
 Validate.assertEquals(childNode, node.childNode(0));
 Validate.assertEquals(childNode, node.childNodesAsArray()[0]);

 Validate.assertEquals(Collections.emptyList(), node.siblingNodes());
 Validate.assertNull(node.parent());

 node.remove();
 Validate.assertNull(parentNode.childNodes().get(0));
 }

 @Test
 public void testNodeToString() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node node = new Element("div", baseUri, attributes);
 Validate.assertEquals("<div id=\"test-id\"></div>", node.toString());
 }

 @Test
 public void testNodeClone() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node original = new Element("div", baseUri, attributes);
 Node cloned = original.clone();

 Validate.notNull(original);
 Validate.notNull(cloned);
 Validate.assertEquals(original, cloned.clone());
 Validate.assertNotSame(original, cloned);
 }

 @Test
 public void testNodeRemove() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node parent = new Element("body");
 Node node = new Element("div", baseUri, attributes);
 parent.addChild(node);

 Validate.assertEquals(1, parent.childNodes().size());
 node.remove();
 Validate.assertEquals(0, parent.childNodes().size());
 }

 @Test
 public void testNodeReplaceChild() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node parent = new Element("body");
 Node oldChild = new Element("p");
 parent.addChild(oldChild);
 Node newChild = new Element("div", baseUri, attributes);
 parent.replaceChild(oldChild, newChild);

 Validate.assertEquals(1, parent.childNodes().size());
 Validate.assertEquals(newChild, parent.childNodes().get(0));
 Validate.assertEquals(newChild, parent.childNode(0));
 Validate.assertEquals(newChild, parent.childNodesAsArray()[0]);
 }

 @Test
 public void testNodeSetParentNode() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node child = new Element("p");
 Node parent = new Element("body");
 child.setParentNode(parent);

 Validate.assertEquals(parent, child.parentNode);
 Validate.assertEquals(child, parent.childNodes().get(0));
 Validate.assertEquals(child, parent.childNode(0));
 Validate.assertEquals(child, parent.childNodesAsArray()[0]);
 }

 @Test
 public void testNodeHasAttr() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");
 attributes.put("class", "test-class");

 Node node = new Element("div", baseUri, attributes);
 Validate.assertTrue(node.hasAttr("id"));
 Validate.assertTrue(node.hasAttr("class"));
 Validate.assertFalse(node.hasAttr("nonexistent"));
 }

 @Test
 public void testNodeAttr() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node node = new Element("div", baseUri, attributes);
 Validate.assertEquals("test-id", node.attr("id"));
 Validate.assertEquals("", node.attr("nonexistent"));

 attributes.put("class", "test-class");
 Validate.assertEquals("test-class", node.attr("class"));

 Node nodeWithAbsoluteUrl = new Element("a", "http://example.com/test.html", attributes);
 Validate.assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 @Test
 public void testNodeAbsUrl() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "test.html");

 Node node = new Element("a", baseUri, attributes);
 Validate.assertEquals("test.html", node.attr("href"));
 Validate.assertEquals("http://example.com/test.html", node.absUrl("href"));

 String relativeUri = "subfolder/test2.html";
 Node nodeWithRelativeUrl = new Element("a", baseUri, attributes).attr("href", relativeUri);
 Validate.assertEquals(relativeUri, nodeWithRelativeUrl.attr("href"));
 Validate.assertEquals("http://example.com/subfolder/test2.html", nodeWithRelativeUrl.absUrl("href"));
 }

 @Test
 public void testNodeSetBaseUri() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "test.html");

 Node node = new Element("a", baseUri, attributes);
 Validate.assertEquals("test.html", node.attr("href"));
 Validate.assertEquals("http://example.com/test.html", node.absUrl("href"));

 Node nodeWithAbsoluteUrl = new Element("a", "http://example.com/test.html", attributes);
 Validate.assertEquals("test.html", nodeWithAbsoluteUrl.attr("href"));
 Validate.assertEquals("http://example.com/test.html", nodeWithAbsoluteUrl.absUrl("href"));
 }

 @Test
 public void testNodeSetBaseUriWithMalformedUrl() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "test.html");

 Node node = new Element("a", baseUri, attributes);
 Validate.assertEquals("test.html", node.attr("href"));
 Validate.assertEquals("", node.absUrl("href"));

 try {
 Node nodeWithMalformedBaseUri = new Element("a", "jávascrípt:test.html", attributes);
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test(expected = MalformedURLException.class)
 public void testNodeAbsUrlWithMalformedUrl() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "jávascrípt:test.html");

 Node node = new Element("a", baseUri, attributes);
 Validate.assertEquals("jávascrípt:test.html", node.attr("href"));
 }

 @Test
 public void testNodeRemoveAttr() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");
 attributes.put("class", "test-class");

 Node node = new Element("div", baseUri, attributes);
 Validate.assertTrue(node.hasAttr("id"));
 Validate.assertTrue(node.hasAttr("class"));

 node.removeAttr("class");
 Validate.assertEquals("test-id", node.attr("id"));
 Validate.assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testNodeAttributesClone() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");
 attributes.put("class", "test-class");

 Node node = new Element("div", baseUri, attributes);
 Node clonedNode = node.clone();

 Validate.assertEquals(attributes, node.attributes());
 Validate.assertEquals(attributes, clonedNode.attributes());
 }

 @Test
 public void testNodeInnerHtml() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Node parent = new Element("body");
 Node node1 = new Element("p", baseUri, attributes).text("test");
 parent.addChild(node1);

 Node node2 = new Element("p", baseUri, attributes).text("another test");
 parent.addChild(node2);

 Validate.assertEquals("<p>test</p>", node1.outerHtml());
 Validate.assertEquals("<p>another test</p>", node2.outerHtml());
 Validate.assertEquals("<p>test</p><p>another test</p>", parent.outerHtml());
 }

 @Test
 public void testNodeOuterHtmlHeadTail() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test-id");

 Element div = new Element("div", baseUri, attributes);
 div.outerHtmlHead(new StringBuilder(), 1, new Document("").outputSettings());
 div.outerHtmlTail(new StringBuilder(), 1, new Document("").outputSettings());

 StringBuilder expected = new StringBuilder();
 expected.append("<div id=\"test-id\">");
 expected.append("\n");
 expected.append(StringUtil.padding(1 * 4)); // 4 spaces * 1 level
 expected.append("</div>");

 Validate.assertEquals(expected.toString(), div.toString());
 }
}