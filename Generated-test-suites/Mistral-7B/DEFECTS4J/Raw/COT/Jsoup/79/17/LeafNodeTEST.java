import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class LeafNodeTest {

 private LeafNode node;

 @Before
 public void setup() {
 node = new TextNode("Test Value");
 }

 @Test
 public void testHasAttributes() {
 assertFalse(node.hasAttributes());
 }

 @Test
 public void testAttributes() {
 node.ensureAttributes();
 Attributes attributes = node.attributes();
 assertNotNull(attributes);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureAttributesWhenHasAttributes() {
 node.value = new Attributes();
 node.ensureAttributes();
 }

 @Test
 public void testCoreValue() {
 assertEquals("Test Value", node.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 node.coreValue("New Value");
 assertEquals("New Value", node.coreValue());
 }

 @Test
 public void testAttr() {
 node.ensureAttributes();
 node.attr("test", "Test Value");
 assertEquals("Test Value", node.attr("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrWhenNullKey() {
 node.ensureAttributes();
 node.attr(null, "Test Value");
 }

 @Test
 public void testAttrSetterWithSameKeyAsNodeName() {
 node.coreValue("Test Value");
 assertEquals("Test Value", node.attr(node.nodeName()));
 }

 @Test
 public void testAttrSetterWithDifferentKey() {
 node.coreValue("Test Value");
 node.attr("test", "New Value");
 assertEquals("New Value", node.attr("test"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testHasAttr() {
 node.ensureAttributes();
 Assert.assertFalse(node.hasAttr("test"));
 }

 @Test
 public void testRemoveAttr() {
 node.ensureAttributes();
 node.attr("test", "Test Value");
 node.removeAttr("test");
 Assert.assertFalse(node.hasAttr("test"));
 }

 @Test
 public void testAttrWithNullValue() {
 node.ensureAttributes();
 node.attr("test", null);
 Assert.assertNull(node.attr("test"));
 }

 @Test
 public void testAbsUrl() {
 String baseUri = "http://test.com";
 node.baseUri(baseUri);
 node.ensureAttributes();
 node.attr("href", "test.html");
 String expected = "http://test.com/test.html";
 assertEquals(expected, node.absUrl("href"));
 }

 @Test
 public void testBaseUri() {
 String parentName = "ParentNode";
 Node parent = new TextNode(parentName);
 parent.coreValue("Parent Value");
 node.parent(parent);
 String expected = "Parent Value";
 assertEquals(expected, node.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 int expected = 0;
 assertEquals(expected, node.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 }
}