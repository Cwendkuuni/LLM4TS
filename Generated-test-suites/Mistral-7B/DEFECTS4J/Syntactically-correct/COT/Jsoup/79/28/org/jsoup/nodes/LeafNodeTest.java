import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.junit.Assert;
import org.junit.Test;

public class LeafNodeTest {

 private LeafNode node;

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new LeafNode();
Assert.assertFalse(leafNode.hasAttributes());
 LeafNode leafNodeWithAttributes = new LeafNode();
 leafNodeWithAttributes.coreValue("key", "value");
 Assert.assertTrue(leafNodeWithAttributes.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new LeafNode();
 Assert.assertNull(leafNode.attributes());
 LeafNode leafNodeWithAttributes = new LeafNode();
 leafNodeWithAttributes.coreValue("key", "value");
 Attributes attributes = leafNodeWithAttributes.attributes();
 Assert.assertNotNull(attributes);
 Assert.assertEquals("value", attributes.get("key"));
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAttributesWhenNoAttributes() {
 LeafNode leafNode = new LeafNode();
 leafNode.ensureAttributes();
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("value");
Assert.assertEquals("value", leafNode.coreValue());
 LeafNode leafNodeWithAttributes = new LeafNode();
 leafNodeWithAttributes.coreValue("key", "value");
Assert.assertEquals("value", leafNodeWithAttributes.coreValue());
Assert.assertEquals("key", leafNodeWithAttributes.nodeName());
 }

 @Test
 public void testCoreValueSetter() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("newValue");
Assert.assertEquals("newValue", leafNode.coreValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCoreValueSetterWithNullValue() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue(null);
 }

 @Test
 public void testAttr() {
 LeafNode leafNode = new LeafNode();
 Assert.assertEquals(EmptyString, leafNode.attr("key"));
 LeafNode leafNodeWithAttributes = new LeafNode();
 leafNodeWithAttributes.coreValue("key", "value");
 Assert.assertEquals("value", leafNodeWithAttributes.attr("key"));
 Assert.assertEquals(EmptyString, leafNodeWithAttributes.attr("nonExistentKey"));
 }

 @Test
 public void testAttrSetterWithExistingKey() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("key", "value1");
 leafNode.attr("key", "value2");
 Assert.assertEquals("value2", leafNode.attr("key"));
 }

 @Test
 public void testAttrSetterWithNewKey() {
 LeafNode leafNode = new LeafNode();
 leafNode.coreValue("key1", "value1");
 leafNode.attr("key2", "value2");
 Assert.assertEquals("value1", leafNode.attr("key1"));
 Assert.assertEquals("value2", leafNode.attr("key2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrSetterWithNullKey() {
 LeafNode leafNode = new LeafNode();
 leafNode.attr(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrSetterWithEmptyKey() {
 LeafNode leafNode = new LeafNode();
 leafNode.attr("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrSetterWithNullValue() {
 LeafNode leafNode = new LeafNode();
 leafNode.attr("key", null);
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new LeafNode();
 Assert.assertFalse(leafNode.hasAttr("key"));
 LeafNode leafNodeWithAttributes = new LeafNode();
 leafNodeWithAttributes.coreValue("key", "value");
 Assert.assertTrue(leafNodeWithAttributes.hasAttr("key"));
 Assert.assertFalse(leafNodeWithAttributes.hasAttr("nonExistentKey"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNodeWithAttributes = new LeafNode();
 leafNodeWithAttributes.coreValue("key", "value");
 leafNodeWithAttributes.removeAttr("key");
 Assert.assertFalse(leafNodeWithAttributes.hasAttr("key"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new LeafNode();
 Assert.assertEquals(EmptyString, leafNode.absUrl("key"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode = new LeafNode();
 Assert.assertEquals(EmptyString, leafNode.baseUri());
 LeafNode parentNode = new Node();
 parentNode.baseUri("http://example.com");
 LeafNode childNode = new LeafNode();
 childNode.parent(parentNode);
 Assert.assertEquals("http://example.com", childNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode leafNode = new LeafNode();
 Assert.assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new LeafNode();
 leafNode.ensureChildNodes();
 }
}