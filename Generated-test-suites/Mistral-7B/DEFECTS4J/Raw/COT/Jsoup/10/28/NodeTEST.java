import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NodeTest {

 @Test
 public void testNodeConstructorWithBaseUriAndAttributes() {
 Node node = new Node("http://example.com", new Attributes());
 Validate.notNull(node.baseUri());
 Validate.notNull(node.attributes());
 }

 @Test
 public void testNodeConstructorWithBaseUriOnly() {
 Node node = new Node("http://example.com");
 Validate.notNull(node.baseUri());
 Validate.assertNull(node.attributes());
 }

 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Node();
 Validate.assertEquals(Collections.EMPTY_LIST, node.childNodes());
 Validate.assertNull(node.attributes());
 }

 @Test
 public void testNodeAbstractMethodgetNodeName() {
 try {
 Node node = new Node() {
 @Override
 public String nodeName() {
 throw new UnsupportedOperationException();
 }
 };
 fail("Abstract method should not be instantiated.");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testAttrMethodWithValidAttributeKey() {
 Node node = new Element("div", new Attributes().put("id", "test"));
 assertEquals("test", node.attr("id"));
 }

 @Test
 public void testAttrMethodWithInvalidAttributeKey() {
 Node node = new Element("div", new Attributes());
 assertNull(node.attr("nonExistentAttribute"));
 }

 @Test
 public void testAbsUrlMethodWithValidRelativeUrl() {
 Node node = new Element("a", new Attributes().put("href", "/test.html"));
 assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 @Test
 public void testAbsUrlMethodWithInvalidRelativeUrl() {
 Node node = new Element("a", new Attributes().put("href", "test.html"));
 assertNull(node.absUrl("href"));
 }

 @Test
 public void testAbsUrlMethodWithAbsoluteUrl() {
 Node node = new Element("a", new Attributes().put("href", "http://example.com/test.html"));
 assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 @Test(expected = MalformedURLException.class)
 public void testAbsUrlMethodWithInvalidUrl() {
 Node node = new Element("a", new Attributes().put("href", "invalid:url"));
 // expected MalformedURLException
 }

 @Test
 public void testChildNodeMethodWithValidIndex() {
 Node parentNode = new Element("div", new Attributes());
 Node childNode = new Element("p", new Attributes());
 parentNode.appendChild(childNode);
 Node node = parentNode.childNode(0);
 assertEquals(childNode, node);
 }

 @Test
 public void testChildNodesMethod() {
 Node parentNode = new Element("div", new Attributes());
 Node childNode1 = new Element("p", new Attributes());
 Node childNode2 = new Element("p", new Attributes());
 parentNode.appendChild(childNode1);
 parentNode.appendChild(childNode2);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(childNode1, childNodes.get(0));
 assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testRemoveChildMethodWithValidIndex() {
 Node parentNode = new Element("div", new Attributes());
 Node childNode1 = new Element("p", new Attributes());
 Node childNode2 = new Element("p", new Attributes());
 parentNode.appendChild(childNode1);
 parentNode.appendChild(childNode2);
 parentNode.removeChild(childNode1);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(1, childNodes.size());
 assertNull(childNodes.get(0).nodeName()); // check that removed node is no longer present
 }

 @Test
 public void testSetParentNodeMethod() {
 Node childNode = new Element("p", new Attributes());
 Node parentNode = new Element("div", new Attributes());
 childNode.setParentNode(parentNode);
 assertEquals(parentNode, childNode.parent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetParentNodeMethodWithNullParentNode() {
 Node childNode = new Element("p", new Attributes());
 childNode.setParentNode(null);
 // expected IllegalArgumentException
 }

 @Test(expected = IllegalStateException.class)
 public void testSetParentNodeMethodWithNonEmptyParentNode() {
 Node parentNode = new Element("div", new Attributes());
 Node childNode = new Element("p", new Attributes());
 parentNode.appendChild(childNode);
 childNode.setParentNode(new Element("div", new Attributes()));
 // expected IllegalStateException
 }

 @Test
 public void testHasAttrMethodWithValidAttributeKey() {
 Node node = new Element("div", new Attributes().put("id", "test"));
 assertTrue(node.hasAttr("id"));
 }

 @Test
 public void testHasAttrMethodWithInvalidAttributeKey() {
 Node node = new Element("div", new Attributes());
 assertFalse(node.hasAttr("nonExistentAttribute"));
 }

 @Test
 public void testSetAttrMethodWithValidAttributeKeyAndValue() {
 Node node = new Element("div", new Attributes());
 node.attr("id", "test");
 assertEquals("test", node.attr("id"));
 }

 @Test(expected = NullPointerException.class)
 public void testSetAttrMethodWithNullAttributes() {
 Node node = new Element("div", null);
 try {
 node.attr("id", "test");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAttrMethodWithInvalidAttributeKey() {
 Node node = new Element("div", new Attributes());
 try {
 node.attr("nonExistentAttribute", "value");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRemoveAttrMethodWithValidAttributeKey() {
 Node node = new Element("div", new Attributes().put("id", "test"));
 node.removeAttr("id");
 assertFalse(node.hasAttr("id"));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveAttrMethodWithNullAttributes() {
 Node node = new Element("div", null);
 node.removeAttr("id"); // expected NullPointerException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAttrMethodWithInvalidAttributeKey() {
 Node node = new Element("div", new Attributes());
 node.removeAttr("nonExistentAttribute"); // expected IllegalArgumentException
 }

 @Test
 public void testBaseUriMethodWithValidBaseUri() {
 Node node = new Element("div", new Attributes().put("baseUri", "http://example.com"));
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testBaseUriMethodWithInvalidBaseUri() {
 Node node = new Element("div", new Attributes().put("baseUri", "invalid:uri"));
 assertNull(node.baseUri());
 }

 @Test
 public void testSetBaseUriMethodWithValidBaseUri() {
 Node node = new Element("div", new Attributes());
 node.setBaseUri("http://example.com");
 assertEquals("http://example.com", node.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testSetBaseUriMethodWithNullBaseUri() {
 Node node = new Element("div", new Attributes());
 try {
 node.setBaseUri(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testOwnerDocumentMethodWithValidNode() {
 Node node = new Element("div", new Attributes());
 Document document = new Document();
 document.appendChild(node);
 assertEquals(document, node.ownerDocument());
 }

 @Test
 public void testOwnerDocumentMethodWithNullNode() {
 Document document = new Document();
 assertNull(document.ownerDocument());
 }

 @Test
 public void testRemoveMethodWithValidNode() {
 Node parentNode = new Element("div", new Attributes());
 Node childNode = new Element("p", new Attributes());
 parentNode.appendChild(childNode);
 parentNode.remove();
 assertNull(childNode.parentNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveMethodWithNonEmptyParentNode() {
 Node parentNode = new Element("div", new Attributes());
 Node childNode = new Element("p", new Attributes());
 parentNode.appendChild(childNode);
 childNode.remove();
 // expected IllegalStateException
 }

 @Test
 public void testReplaceWithMethodWithValidNodes() {
 Node parentNode = new Element("div", new Attributes());
 Node oldChildNode = new Element("p", new Attributes());
 Node newChildNode = new Element("p", new Attributes());
 parentNode.appendChild(oldChildNode);
 parentNode.replaceChild(oldChildNode, newChildNode);
 assertEquals(newChildNode, parentNode.childNode(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceWithMethodWithNullNewChildNode() {
 Node parentNode = new Element("div", new Attributes());
 Node oldChildNode = new Element("p", new Attributes());
 parentNode.appendChild(oldChildNode);
 try {
 parentNode.replaceChild(oldChildNode, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testReplaceWithMethodWithNonEmptyOldChildNode() {
 Node parentNode = new Element("div", new Attributes());
 Node oldChildNode1 = new Element("p", new Attributes());
 Node oldChildNode2 = new Element("p", new Attributes());
 parentNode.appendChild(oldChildNode1);
 parentNode.appendChild(oldChildNode2);
 Node newChildNode = new Element("p", new Attributes());
 try {
 parentNode.replaceChild(oldChildNode1, newChildNode);
 } catch (IllegalStateException e) {
 // expected
 }
 }
}