import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode leafNode;
 private Attributes attributes;

 @Before
 public void setUp() {
 leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 attributes = new Attributes();
 }

 @Test
 public void testHasAttributes() {
 assertFalse(leafNode.hasAttributes());
 leafNode.coreValue("testValue");
 assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 leafNode.coreValue("testValue");
 leafNode.attr("test", "testValue");
 assertEquals(attributes, leafNode.attributes());
 assertEquals("testValue", leafNode.attr("test"));
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAttributesWithNoValue() {
 leafNode.ensureAttributes();
 }

 @Test
 public void testEnsureAttributesWithValue() {
 leafNode.coreValue("testValue");
 leafNode.ensureAttributes();
 assertEquals(attributes, leafNode.attributes());
 }

 @Test
 public void testCoreValue() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSet() {
 leafNode.coreValue("testValue");
 leafNode.coreValue("newTestValue");
 assertEquals("newTestValue", leafNode.coreValue());
 }

 @Test
 public void testAttr() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.attr("test"));
 leafNode.attr("test", "newTestValue");
 assertEquals("newTestValue", leafNode.attr("test"));
 }

 @Test
 public void testAttrSet() {
 leafNode.coreValue("testValue");
 leafNode.attr("test", "newTestValue");
 assertEquals("newTestValue", leafNode.attr("test"));
 }

 @Test
 public void testHasAttr() {
 leafNode.coreValue("testValue");
 leafNode.attr("test", "testValue");
 assertTrue(leafNode.hasAttr("test"));
 }

 @Test
 public void testRemoveAttr() {
 leafNode.coreValue("testValue");
 leafNode.attr("test", "testValue");
 leafNode.removeAttr("test");
 assertFalse(leafNode.hasAttr("test"));
 }

 @Test
 public void testAbsUrl() {
 // Unsupported operation for LeafNodes
 try {
 leafNode.absUrl("test");
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testBaseUri() {
 leafNode.coreValue("testValue");
 assertEmpty(leafNode.baseUri());
 leafNode.parent().coreValue("testParent");
 assertEquals("testParent", leafNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 // LeafNodes have no children
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 leafNode.ensureChildNodes();
 }
}