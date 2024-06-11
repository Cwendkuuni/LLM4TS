import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.jsoup.helper.Validate.notNull;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new MyLeafNode();
 assertTrue(leafNode.hasAttributes()); // when value is an Attributes object
 leafNode.coreValue("testValue");
 assertFalse(leafNode.hasAttributes()); // when value is a string
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.attr("testKey", "testValue");
 Attributes attributes = leafNode.attributes();
 assertEquals("testValue", attributes.get("testKey"));
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetting() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.coreValue("testValue");
 leafNode.coreValue("newTestValue");
 assertEquals("newTestValue", leafNode.coreValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullKey() {
 LeafNode leafNode = new MyLeafNode();
 notNull(leafNode);
 leafNode.attr(null, "testValue");
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullValue() {
 LeafNode leafNode = new MyLeafNode();
 notNull(leafNode);
 leafNode.attr("testKey", null);
 }

 @Test
 public void testAttrSetting() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.attr("testKey", "testValue");
 assertEquals("testValue", leafNode.attr("testKey"));
 }

 @Test
 public void testAttrRemoval() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.attr("testKey", "testValue");
 leafNode.removeAttr("testKey");
 assertNull(leafNode.attr("testKey"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.attr("testKey", "testValue");
 assertTrue(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testHasAttrFalse() {
 LeafNode leafNode = new MyLeafNode();
 assertFalse(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.attr("testKey", "testValue");
 Node removedNode = leafNode.removeAttr("testKey");
 assertNull(removedNode);
 assertFalse(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.attr("abs:href", "http://test.com");
 assertEquals("http://test.com", leafNode.absUrl("abs:href"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode = new MyLeafNode();
 leafNode.parent().attr("base", "http://test.com");
 assertEquals("http://test.com", leafNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode leafNode = new MyLeafNode();
 assertEquals(0, leafNode.childNodeSize());
 }
}

class MyLeafNode extends LeafNode {
 // empty class for testing
}