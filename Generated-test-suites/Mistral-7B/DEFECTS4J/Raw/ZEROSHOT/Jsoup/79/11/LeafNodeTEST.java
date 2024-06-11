import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new LeafNode();
 assertTrue(leafNode.hasAttributes()); // empty leaf node has no attributes, but the default constructor sets an empty Attributes object as value

 leafNode.coreValue("testValue");
 assertFalse(leafNode.hasAttributes()); // setting a core value should remove any previous attributes
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new LeafNode();
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;

 assertEquals(attributes, leafNode.attributes());
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSet() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("testValue");
 leafNode.coreValue("newTestValue");
 assertEquals("newTestValue", leafNode.coreValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAttributeNonExistence() {
 LeafNode leafNode = new LeafNode();
 assertNull(leafNode.attr("testKey"));
 }

 @Test
 public void testAttributeExistence() {
 LeafNode leafNode = new LeafNode();
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;
 assertEquals("testValue", leafNode.attr("testKey"));
 }

 @Test
 public void testAttributeSet() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("testNodeName");
 leafNode.attr("testKey", "testValue");
 assertEquals("testValue", leafNode.attr("testKey"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new LeafNode();
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;
 assertTrue(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new LeafNode();
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;

 leafNode.removeAttr("testKey");
 assertFalse(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testAttrUrl() {
 LeafNode leafNode = new LeafNode();
 Attributes attributes = new Attributes();
 attributes.put("href", "testUrl");
 leafNode.value = attributes;
 assertEquals("testUrl", leafNode.absUrl("href"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("testNodeName");
 leafNode.parent().coreValue("testParentNodeName");
 assertEquals("testParentNodeName", leafNode.baseUri());
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