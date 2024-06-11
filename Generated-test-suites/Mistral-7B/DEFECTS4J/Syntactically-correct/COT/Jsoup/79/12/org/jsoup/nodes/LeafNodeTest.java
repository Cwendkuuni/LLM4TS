import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

public class LeafNodeTest {

 private LeafNode node;

 @Test
 public void testHasAttributes() {
 node = new LeafNode();
 assertFalse(node.hasAttributes());

 node.coreValue("test");
 assertTrue(node.hasAttributes());
 }

 @Test
 public void testAttributes() {
 node = new LeafNode();
 node.coreValue("test", new Attributes());
 Attributes attributes = node.attributes();
 assertNotNull(attributes);
 assertEquals("test", attributes.get("nodeName"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureAttributesWhenNoAttributes() {
 node = new LeafNode();
 node.ensureAttributes();
 }

 @Test
 public void testEnsureAttributesWhenHasAttributes() {
 node = new LeafNode();
 node.coreValue("test", new Attributes().put("testKey", "testValue"));
 node.ensureAttributes();
 Attributes attributes = (Attributes) node.value;
 assertNotNull(attributes);
 assertEquals("testValue", attributes.get("testKey"));
 }

 @Test
 public void testCoreValue() {
 node = new LeafNode();
 node.coreValue("test");
 assertEquals("test", node.coreValue());
 }

 @Test
 public void testCoreValueSet() {
 node = new LeafNode();
 node.coreValue("test");
 node.coreValue("newTest");
 assertEquals("newTest", node.coreValue());
 }

 @Test
 public void testAttrNullKey() {
 node = new LeafNode();
 node.coreValue("test");
 assertNull(node.attr(null));
 }

 @Test
 public void testAttrEmptyKey() {
 node = new LeafNode();
 node.coreValue("test");
 assertNull(node.attr(""));
 }

 @Test
 public void testAttrKeyEqualsNodeName() {
 node = new LeafNode();
 node.coreValue("test", new Attributes().put("test", "testValue"));
 assertEquals("testValue", node.attr("test"));
 }

 @Test
 public void testAttrKeyNotEqualsNodeName() {
 node = new LeafNode();
 node.coreValue("test");
 assertNull(node.attr("otherKey"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullValue() {
 node = new LeafNode();
 node.coreValue("test");
 node.attr("test", null);
 }

 @Test
 public void testAttrSetAndGet() {
 node = new LeafNode();
 node.coreValue("test");
 node.attr("test", "testValue");
 assertEquals("testValue", node.attr("test"));
 }

 @Test
 public void testHasAttr() {
 node = new LeafNode();
 node.coreValue("test", new Attributes().put("test", "testValue"));
 assertTrue(node.hasAttr("test"));
 }

 @Test
 public void testRemoveAttr() {
 node = new LeafNode();
 node.coreValue("test", new Attributes().put("test", "testValue"));
 node.removeAttr("test");
 assertFalse(node.hasAttr("test"));
 }

 @Test
 public void testAbsUrl() {
 node = new LeafNode();
 node.coreValue("test", new Attributes().put("abs:url", "http://test.com"));
 assertEquals("http://test.com", node.absUrl("abs:url"));
 }

 @Test
 public void testBaseUri() {
 node = new LeafNode();
 node.coreValue("test", new Attributes().put("base", "<html>"));
 assertEquals("<html>", node.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 node = new LeafNode();
 assertEquals(0, node.childNodeSize());
 }
}