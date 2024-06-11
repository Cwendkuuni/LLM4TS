import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

public class LeafNodeTest {

 private LeafNode node;

 @Test
 public void testHasAttributes() {
 node = new LeafNode();
 assertFalse(node.hasAttributes());

 node.coreValue("testAttribute");
 assertTrue(node.hasAttributes());
 }

 @Test
 public void testAttributes() {
 node = new LeafNode();
 node.coreValue("testAttribute");
 Attributes attributes = node.attributes();
 assertNotNull(attributes);
 assertEquals("testAttribute", attributes.get("testAttribute"));
 }

 @Test
 public void testCoreValue() {
 node = new LeafNode();
 node.coreValue("testValue");
 assertEquals("testValue", node.coreValue());
 }

 @Test
 public void testCoreValueSetting() {
 node = new LeafNode();
 node.coreValue("testValue");
 node.coreValue("newTestValue");
 assertEquals("newTestValue", node.coreValue());
 }

 @Test
 public void testAttr() {
 node = new LeafNode();
 node.coreValue("testAttribute");
 assertEquals("testAttribute", node.attr("testAttribute"));
 assertNull(node.attr("nonExistentAttribute"));
 }

 @Test
 public void testAttrSetting() {
 node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testAttribute", "testValue");
 assertEquals("testValue", node.attr("testAttribute"));
 }

 @Test
 public void testHasAttr() {
 node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testAttribute", "testValue");
 assertTrue(node.hasAttr("testAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 node = new LeafNode();
 node.coreValue("testValue");
 node.attr("testAttribute", "testValue");
 node.removeAttr("testAttribute");
 assertFalse(node.hasAttr("testAttribute"));
 }

 @Test
 public void testAttrGettingFromParent() {
 LeafNode parent = new LeafNode();
 parent.coreValue("parentTestAttribute");
 LeafNode child = new LeafNode();
 child.parent(parent);
 assertEquals("parentTestAttribute", child.attr("testAttribute"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 node.childNodeSize();
 fail("Leaf nodes should not have child nodes.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 fail("Leaf nodes should not have child nodes.");
 }

 @Test
 public void testEnsureAttributes() {
 node = new LeafNode();
 node.coreValue("testValue");
 node.ensureAttributes();
 Attributes attributes = node.attributes();
 assertEquals("testValue", attributes.get("testAttribute"));
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAttributesWhenHasAttributes() {
 node = new LeafNode();
 node.coreValue("testValue");
 node.ensureAttributes();
 node.ensureAttributes();
 fail("EnsureAttributes should throw an IllegalStateException when called multiple times.");
 }

 @Test
 public void testEnsureAttributesWithMultipleAttributes() {
 LeafNode node = new LeafNode();
 Map<String, String> attributesMap = new HashMap<>();
 attributesMap.put("attr1", "value1");
 attributesMap.put("attr2", "value2");
 node.value = attributesMap;
 node.ensureAttributes();
 Attributes actual = node.attributes();
 Map<String, String> expected = new HashMap<>(attributesMap);
 expected.remove("value");
 assertEquals(expected, actual);
 }
}