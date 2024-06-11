import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.jsoup.helper.Validate.*;

public class LeafNodeTest {

 private LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };

 @Test
 public void testHasAttributes() {
 LeafNode node1 = new LeafNode();
 Assert.assertFalse(node1.hasAttributes());

 LeafNode node2 = new LeafNode();
 node2.coreValue("testValue");
 Assert.assertFalse(node2.hasAttributes());

 LeafNode node3 = new LeafNode();
 node3.value = new Attributes();
 Assert.assertTrue(node3.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.value = new Attributes();
 node.value.put("testKey", "testValue");

 Attributes attributes = node.attributes();
 Assert.assertEquals("testValue", attributes.get("testKey"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureAttributesWhenNoAttributes() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.ensureAttributes();
 }

 @Test
 public void testCoreValue() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 Assert.assertEquals("testValue", node.coreValue());
 }

 @Test
 public void testCoreValueWithAttributes() {
 LeafNode node = new LeafNode();
 node.value = new Attributes();
 node.coreValue("testValue");
 Assert.assertEquals("testValue", node.coreValue());
 Assert.assertFalse(node.hasAttributes());
 }

 @Test
 public void testCoreValueSetting() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.coreValue("newTestValue");
 Assert.assertEquals("newTestValue", node.coreValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttrWithNullKey() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr(null, "testValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrWithEmptyKey() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr("", "testValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrWithEmptyValue() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testKey", "");
 }

 @Test
 public void testAttrSetting() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testKey", "testValue");
 Assert.assertEquals("testValue", node.attr("testKey"));
 }

 @Test
 public void testAttrWithExistingKey() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testKey", "testValue");
 node.attr("testKey", "newTestValue");
 Assert.assertEquals("newTestValue", node.attr("testKey"));
 }

 @Test
 public void testHasAttr() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testKey", "testValue");
 Assert.assertTrue(node.hasAttr("testKey"));
 Assert.assertFalse(node.hasAttr("nonExistentKey"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testKey", "testValue");
 node.removeAttr("testKey");
 Assert.assertFalse(node.hasAttr("testKey"));
 }

 @Test
 public void testAttrWithSameKeyMultipleTimes() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testKey", "testValue1");
 node.attr("testKey", "testValue2");
 Assert.assertEquals("testValue2", node.attr("testKey"));
 }

 @Test
 public void testBaseUri() {
 LeafNode node = new LeafNode();
 node.coreValue("testValue");
 Assert.assertEquals("", node.baseUri());

 LeafNode parentNode = new LeafNode();
 parentNode.coreValue("http://test.com");
 node.parent(parentNode);
 Assert.assertEquals("http://test.com", node.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode node = new LeafNode();
 Assert.assertEquals(0, node.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode node = new LeafNode();
 node.ensureChildNodes();
 }
}