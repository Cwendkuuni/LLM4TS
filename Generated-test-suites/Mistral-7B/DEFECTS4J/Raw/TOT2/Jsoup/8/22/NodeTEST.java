import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class NodeTest {

 // Testing constructor with valid baseUri and attributes
 @Test
 public void testValidNodeConstruction() {
 Node node = new Element("div", new Attributes());
 node.attributes().put("class", "testClass");
 Node validNode = new Node("", node.attributes());
 assertEquals("div", validNode.nodeName());
 assertNotNull(validNode.attributes());
 assertEquals("testClass", validNode.attributes().get("class"));
 }

 // Testing constructor with only baseUri
 @Test
 public void testValidNodeConstructionWithoutAttributes() {
 Node node = new Node("", null);
 assertNull(node.attributes());
 }

 // Testing constructor with null baseUri and attributes
 @Test(expected = NullPointerException.class)
 public void testInvalidNodeConstructionWithNullBaseUri() {
 new Node(null, new Attributes());
 }

 // Testing constructor with null attributes
 @Test(expected = NullPointerException.class)
 public void testInvalidNodeConstructionWithNullAttributes() {
 new Node("", null);
 }

 // Testing getNodeName method
 @Test
 public void testNodeNameMethod() {
 Element element = new Element("p");
 assertEquals("p", element.nodeName());
 }

 // Testing getAttribute method with valid attribute key
 @Test
 public void testGetAttributeMethodWithValidAttributeKey() {
 Element element = new Element("div");
 element.attr("class", "testClass");
 assertEquals("testClass", element.attr("class"));
 }

 // Testing getAttribute method with invalid attribute key
 @Test
 public void testGetAttributeMethodWithInvalidAttributeKey() {
 Element element = new Element("div");
 assertEquals("", element.attr("invalidKey"));
 }

 // Testing getAllAttributes method
 @Test
 public void testGetAllAttributesMethod() {
 Element element = new Element("div");
 element.attr("class", "testClass");
 element.attr("id", "testId");
 Attributes attributes = element.attributes();
 assertEquals("class", attributes.keySet().toString());
 assertEquals("testClass", attributes.get("class"));
 assertEquals("testId", attributes.get("id"));
 }

 // Testing setting attribute
 @Test
 public void testSettingAttributeMethod() {
 Element element = new Element("div");
 element.attr("class", "testClass");
 element.attr("id", "testId");
 assertEquals("testClass", element.attr("class"));
 assertEquals("testId", element.attr("id"));
 }

 // Testing removing attribute
 @Test
 public void testRemovingAttributeMethod() {
 Element element = new Element("div");
 element.attr("class", "testClass");
 element.removeAttr("class");
 assertNull(element.attributes());
 }

 // Testing getBaseUri method with valid baseUri
 @Test
 public void testGetBaseUriMethodWithValidBaseUri() {
 Element element = new Element("div");
 Node node = new Node("http://example.com", element.attributes());
 assertEquals("http://example.com", node.baseUri());
 }

 // Testing setBaseUri method with valid baseUri
 @Test
 public void testSetBaseUriMethodWithValidBaseUri() {
 Element element = new Element("div");
 Node node = new Node("", element.attributes());
 node.setBaseUri("http://example.com");
 assertEquals("http://example.com", node.baseUri());
 }

 // Testing getAbsUrl method with valid relative URL
 @Test
 public void testGetAbsUrlMethodWithValidRelativeUrl() {
 Element linkElement = new Element("a", new Attributes().put("href", "test.html"));
 Node node = new Node("", linkElement.attributes());
 assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 // Testing getAbsUrl method with null attribute key
 @Test
 public void testGetAbsUrlMethodWithNullAttributeKey() {
 Element element = new Element("div");
 Node node = new Node("", element.attributes());
 assertEquals("", node.absUrl(null));
 }

 // Testing getAbsUrl method with invalid URL
 @Test(expected = MalformedURLException.class)
 public void testGetAbsUrlMethodWithInvalidUrl() {
 Element linkElement = new Element("a", new Attributes().put("href", "test"));
 Node node = new Node("", linkElement.attributes());
 node.absUrl("href");
 }

 // Testing getChildNode method with valid index
 @Test
 public void testGetChildNodeMethodWithValidIndex() {
 Element parentElement = new Element("ul");
 Element childElement1 = new Element("li");
 Element childElement2 = new Element("li");
 parentElement.addChildren(childElement1, childElement2);
 Node parentNode = new Node("", parentElement.attributes());
 Node childNode1 = parentNode.childNode(0);
 assertEquals("li", childNode1.nodeName());
 }

 // Testing getChildNodes method with empty list
 @Test
 public void testGetChildNodesMethodWithEmptyList() {
 Element parentElement = new Element("ul");
 Node parentNode = new Node("", parentElement.attributes());
 List<Node> childNodes = parentNode.childNodes();
 assertTrue(childNodes.isEmpty());
 }

 // Testing getChildNodes method with non-empty list
 @Test
 public void testGetChildNodesMethodWithNonEmptyList() {
 Element parentElement = new Element("ul");
 Element childElement1 = new Element("li");
 Element childElement2 = new Element("li");
 parentElement.addChildren(childElement1, childElement2);
 Node parentNode = new Node("", parentElement.attributes());
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(2, childNodes.size());
 }

 // Testing getChildrenAsArray method with empty list
 @Test
 public void testGetChildrenAsArrayMethodWithEmptyList() {
 Element parentElement = new Element("ul");
 Node parentNode = new Node("", parentElement.attributes());
 Node[] children = parentNode.childNodesAsArray();
 assertArrayEquals(new Node[0], children);
 }

 // Testing getChildrenAsArray method with non-empty list
 @Test
 public void testGetChildrenAsArrayMethodWithNonEmptyList() {
 Element parentElement = new Element("ul");
 Element childElement1 = new Element("li");
 Element childElement2 = new Element("li");
 parentElement.addChildren(childElement1, childElement2);
 Node parentNode = new Node("", parentElement.attributes());
 Node[] children = parentNode.childNodesAsArray();
 assertArrayEquals(new Node[] { childElement1, childElement2 }, children);
 }

 // Testing getParent method with valid parent
 @Test
 public void testGetParentMethodWithValidParent() {
 Element parentElement = new Element("div");
 Element childElement = new Element("p");
 parentElement.addChild(childElement);
 Node parentNode = childElement.parent();
 assertEquals(parentElement, parentNode);
 }

 // Testing getParent method with null parent
 @Test
 public void testGetParentMethodWithNullParent() {
 Node node = new Node("", null);
 assertNull(node.parent());
 }

 // Testing getOwnerDocument method with valid owner document
 @Test
 public void testGetOwnerDocumentMethodWithValidOwnerDocument() {
 Document document = new Document();
 Element element = new Element("div", new Attributes());
 Node node = new Node("", element.attributes());
 assertEquals(document, node.ownerDocument());
 }

 // Testing getOwnerDocument method with null owner document
 @Test
 public void testGetOwnerDocumentMethodWithNullOwnerDocument() {
 Node node = new Node("", null);
 assertNull(node.ownerDocument());
 }

 // Testing remove method
 @Test
 public void testRemoveMethod() {
 Element parentElement = new Element("ul");
 Element childElement = new Element("li");
 parentElement.addChild(childElement);
 Node parentNode = new Node("", parentElement.attributes());
 List<Node> childNodes = parentNode.childNodes();
 parentNode.remove();
 assertEquals(0, childNodes.size());
 }

 // Testing replaceChild method
 @Test
 public void testReplaceChildMethod() {
 Element parentElement = new Element("ul");
 Element oldChildElement = new Element("li");
 Element newChildElement = new Element("li");
 parentElement.addChild(oldChildElement);
 Node parentNode = new Node("", parentElement.attributes());
 parentNode.replaceChild(oldChildElement, newChildElement);
 assertEquals(newChildElement, parentNode.childNode(0));
 }

 // Testing setParentNode method
 @Test
 public void testSetParentNodeMethod() {
 Element parentElement1 = new Element("ul");
 Element parentElement2 = new Element("ol");
 Node childNode = new Node("", parentElement1.attributes());
 childNode.setParentNode(parentElement2);
 assertEquals(parentElement2, childNode.parent());
 }

 // Testing replaceChild method with null child
 @Test(expected = NullPointerException.class)
 public void testReplaceChildMethodWithNullChild() {
 Element parentElement = new Element("ul");
 Node parentNode = new Node("", parentElement.attributes());
 parentNode.replaceChild(null, null);
 }

 // Testing addChildren method with empty array
 @Test
 public void testAddChildrenMethodWithEmptyArray() {
 Element parentElement = new Element("ul");
 Node parentNode = new Node("", parentElement.attributes());
 parentNode.addChildren();
 assertTrue(parentNode.childNodes().isEmpty());
 }

 // Testing addChildren method with non-empty array
 @Test
 public void testAddChildrenMethodWithNonEmptyArray() {
 Element parentElement = new Element("ul");
 Element childElement1 = new Element("li");
 Element childElement2 = new Element("li");
 Node parentNode = new Node("", parentElement.attributes());
 parentNode.addChildren(childElement1, childElement2);
 assertEquals(2, parentNode.childNodes().size());
 }
}