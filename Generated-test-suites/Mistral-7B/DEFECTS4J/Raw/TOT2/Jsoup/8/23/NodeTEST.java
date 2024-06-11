import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.nodes.AttributesImpl.Attribute;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Assert.assertEquals("div", node.nodeName());
 }

 @Test
 public void testNodeWithAttributes() {
 Node node = new Element("a").attr("href", "test.html").attr("class", "test");
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Assert.assertEquals("a", node.nodeName());
 Assert.assertEquals("test.html", node.attr("href"));
 Assert.assertEquals("test", node.attr("class"));
 }

 @Test
 public void testNodeWithEmptyAttributes() {
 Node node = new Element("input");
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Assert.assertEquals("input", node.nodeName());
 Assert.assertTrue(node.attributes().isEmpty());
 }

 @Test
 public void testNodeWithBaseUri() {
 Node node = new Element("img").attr("src", "test.jpg").attr("baseUri", "test.com");
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 Assert.assertEquals("img", node.nodeName());
 Assert.assertEquals("test.com/test.jpg", node.absUrl("src"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Node node = new Element("img").attr("baseUri", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributes() {
 Node node = new Element("div") {
 @Override
 public String nodeName() {
 return "div";
 }
 };
 node.attributes = null;
 Node parentNode = new Element("body");
 node.parentNode = parentNode;
 parentNode.childNodes.add(node);

 Node childNode = new Element("p");
 parentNode.childNodes.add(childNode);
 childNode.parentNode = parentNode;
 childNode.setParentNode(parentNode);
 childNode.replaceWith(node);
 }

 @Test
 public void testNodeWithChildren() {
 Node parentNode = new Element("ul");
 Node childNode1 = new Element("li").text("Child 1");
 Node childNode2 = new Element("li").text("Child 2");
 parentNode.childNodes.add(childNode1);
 parentNode.childNodes.add(childNode2);
 parentNode.addChildren(childNode1, childNode2);
 parentNode.removeChild(childNode1);
 parentNode.childNodesAsArray();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNegativeChildIndex() {
 Node parentNode = new Element("div");
 parentNode.addChildren(new Element("p"), new Element("p"));
 parentNode.removeChild(parentNode.childNodesAsArray()[2]);
 parentNode.removeChild(-1);
 }

 @Test
 public void testNodeWithParent() {
 Node childNode = new Element("div").attr("id", "child");
 Node parentNode = new Element("body");
 parentNode.addChildren(childNode);
 childNode.setParentNode(parentNode);
 childNode.remove();
 childNode.parent = null;
 parentNode.removeChild(parentNode.childNodesAsArray()[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullChildNode() {
 Node parentNode = new Element("div");
 parentNode.addChildren(new Element("p"), null);
 }

 @Test
 public void testNodeWithEmptyChildList() {
 Node parentNode = new Element("div");
 Node childNode = new Element("p");
 parentNode.addChildren();
 parentNode.removeChild(childNode);
 parentNode.childNodesAsArray();
 }

 @Test
 public void testNodeWithInvalidAttribute() {
 Node node = new Element("input");
 node.attr("@test", "test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAttributeName() {
 Node node = new Element("input");
 node.attr("", "test");
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributeValue() {
 Node node = new Element("input").attr("class", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyAttributeValue() {
 Node node = new Element("input").attr("class", "");
 }
}