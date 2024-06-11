import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode createLeafNode(String nodeName, String value) {
 return new LeafNode() {
 @Override
 public String nodeName() {
 return nodeName;
 }
 @Override
 public Object value() {
 return value;
 }
 };
 }

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = createLeafNode("div", "testValue");
 assertFalse(leafNode.hasAttributes());

 LeafNode leafNodeWithAttributes = createLeafNode("div", new Attributes());
 assertTrue(leafNodeWithAttributes.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = createLeafNode("div", "testValue");
 assertNull(leafNode.attributes());

 LeafNode leafNodeWithAttributes = createLeafNode("div", new Attributes().put("testAttr", "testValue"));
 Attributes attributes = leafNodeWithAttributes.attributes();
 assertEquals("testAttr", attributes.get("testAttr"));
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = createLeafNode("div", "testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 LeafNode leafNode = createLeafNode("div", null);
 leafNode.coreValue("newTestValue");
 assertEquals("newTestValue", leafNode.coreValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAttributesSetterWithoutNodeName() {
 LeafNode leafNode = createLeafNode(null, null);
 leafNode.attr("testKey", "testValue");
 }

 @Test
 public void testAttrGetter() {
 LeafNode leafNode = createLeafNode("div", "testValue");
 assertEquals("testValue", leafNode.attr("nodeName"));
 }

 @Test
 public void testAttrSetter() {
 LeafNode leafNode = createLeafNode("div", null);
 leafNode.attr("nodeName", "newTestValue");
 assertEquals("newTestValue", leafNode.attr("nodeName"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = createLeafNode("div", null);
 assertFalse(leafNode.hasAttr("nodeName"));

 LeafNode leafNodeWithAttr = createLeafNode("div", new Attributes().put("testAttr", "testValue"));
 assertTrue(leafNodeWithAttr.hasAttr("testAttr"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNodeWithAttr = createLeafNode("div", new Attributes().put("testAttr", "testValue"));
 leafNodeWithAttr.removeAttr("testAttr");
 assertFalse(leafNodeWithAttr.hasAttr("testAttr"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = createLeafNode("div", null);
 assertEquals("", leafNode.absUrl("testKey"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode = createLeafNode("div", null);
 Node parentNode = new ElementNode("parent");
 parentNode.coreValue("http://test.com");
 leafNode.parent(parentNode);
 assertEquals("http://test.com", leafNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode leafNode = new LeafNode();
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new LeafNode();
 leafNode.ensureChildNodes();
 }
}