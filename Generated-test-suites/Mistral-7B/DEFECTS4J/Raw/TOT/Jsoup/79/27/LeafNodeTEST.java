import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class LeafNodeTest {

 @Test
 public void testLeafNodeWithStringValue() {
 LeafNode leafNode = new TextNode("Hello World!");
 String coreValue = leafNode.coreValue();
 Assert.assertEquals("Hello World!", coreValue);
 }

 @Test
 public void testLeafNodeWithEmptyAttributes() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.coreValue("Attribute1=value1");
 Attributes attributes = leafNode.attributes();
 Assert.assertFalse(attributes.hasKey("Attribute1"));
 }

 @Test
 public void testLeafNodeWithValidAttributes() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.coreValue("Attribute1=value1 Attribute2=value2");
 Attributes attributes = leafNode.attributes();
 Assert.assertEquals("value1", attributes.get("Attribute1"));
 Assert.assertEquals("value2", attributes.get("Attribute2"));
 }

 @Test(expected = NullPointerException.class)
 public void testLeafNodeWithNullNodeName() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.ensureAttributes();
 leafNode.attr(null, "Value");
 }

 @Test
 public void testLeafNodeWithEmptyKey() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.ensureAttributes();
 leafNode.attr("", "Value");
 Assert.assertEquals("Value", leafNode.attr(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLeafNodeWithEmptyStringValue() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.ensureAttributes();
 leafNode.coreValue("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLeafNodeWithNullValue() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.ensureAttributes();
 leafNode.coreValue(null);
 }

 @Test
 public void testLeafNodeWithBaseUri() {
 LeafNode leafNode = new TextNode("Test");
 String baseUri = "http://example.com/";
 leafNode.ensureAttributes();
 leafNode.doSetBaseUri(baseUri);
 Assert.assertEquals("http://example.com/Test", leafNode.absUrl(""));
 }

 @Test
 public void testLeafNodeWithoutParent() {
 LeafNode leafNode = new TextNode("Test");
 Assert.assertEquals("", leafNode.baseUri());
 }

 @Test
 public void testLeafNodeWithParent() {
 LeafNode parentNode = new Elements.Element("Parent");
 LeafNode leafNode = new TextNode("Test");
 parentNode.appendChild(leafNode);
 Assert.assertEquals(parentNode.baseUri(), leafNode.baseUri());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLeafNodeWithChildNodes() {
 LeafNode leafNode = new TextNode("Test");
 leafNode.ensureChildNodes();
 }
}