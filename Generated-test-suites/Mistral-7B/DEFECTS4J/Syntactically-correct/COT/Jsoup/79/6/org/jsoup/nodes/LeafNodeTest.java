import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.jsoup.helper.Validate.notNull;

public class LeafNodeTest {

 LeafNode node;
 Attributes attributes;

 @Before
 public void setUp() {
 node = new LeafNode();
 attributes = new Attributes();
 }

 // Testing constructor and class level methods

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new LeafNode();
 Assert.assertFalse(leafNode.hasAttributes());

 leafNode.value = attributes;
 Assert.assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new LeafNode();
 leafNode.value = attributes;
 Attributes attributes = leafNode.attributes();
 Assert.assertSame(attributes, leafNode.value);
 }

 // Testing coreValue method

 @Test
 public void testCoreValue() {
 node.coreValue("Test Value");
 Assert.assertEquals("Test Value", node.coreValue());
 }

 @Test
 public void testCoreValue_Empty() {
 node.coreValue(null);
 Assert.assertNull(node.coreValue());
 }

 // Testing attr method

 @Test
 public void testAttr_ValidKey() {
 node.attr("testKey", "Test Value");
 Assert.assertEquals("Test Value", node.attr("testKey"));
 }

 @Test
 public void testAttr_NullKey() {
 notNull(node.nodeName());
 Assert.assertNull(node.attr(null));
 }

 @Test
 public void testAttr_SameKey() {
 node.coreValue("Test Value");
 Assert.assertEquals("Test Value", node.attr(node.nodeName()));
 }

 // Testing attr method with attributes

 @Test
 public void testAttr_ValidKey_WithAttributes() {
 node.value = attributes;
 node.attr("testKey", "Test Value");
 Assert.assertEquals("Test Value", node.attr("testKey"));
 }

 @Test
 public void testAttr_NullKey_WithAttributes() {
 node.value = attributes;
 notNull(node.nodeName());
 Assert.assertNull(node.attr(null));
 }

 @Test
 public void testAttr_SameKey_WithAttributes() {
 node.value = attributes;
 node.attr(node.nodeName(), "Test Value");
 Assert.assertEquals("Test Value", node.attr(node.nodeName()));
 }

 // Testing hasAttr method

 @Test
 public void testHasAttr_ValidKey() {
 node.value = attributes;
 node.attr("testKey", "Test Value");
 Assert.assertTrue(node.hasAttr("testKey"));
 }

 @Test
 public void testHasAttr_InvalidKey() {
 node.value = attributes;
 Assert.assertFalse(node.hasAttr("TestInvalidKey"));
 }

 @Test
 public void testHasAttr_EmptyNode() {
 LeafNode emptyNode = new LeafNode();
 Assert.assertFalse(emptyNode.hasAttr("TestKey"));
 }

 // Testing removeAttr method

 @Test
 public void testRemoveAttr_ValidKey() {
 node.value = attributes;
 node.attr("testKey", "Test Value");
 node.removeAttr("testKey");
 Assert.assertFalse(node.hasAttr("testKey"));
 }

 @Test
 public void testRemoveAttr_NullKey() {
 node.value = attributes;
 Assert.assertFalse(node.removeAttr(null));
 }

 @Test
 public void testRemoveAttr_NonExistentKey() {
 node.value = attributes;
 Assert.assertFalse(node.removeAttr("TestInvalidKey"));
 }

 // Testing absUrl method

 @Test
 public void testAbsUrl() {
 node.value = attributes;
 String baseUri = "http://test.com";
 attributes.put("abs:href", "http://test.com/test");
 String expected = "http://test.com/test";
 String actual = node.absUrl("href");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAbsUrl_Empty() {
 String baseUri = "http://test.com";
 node.value = attributes;
 String expected = baseUri;
 String actual = node.absUrl(null);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAbsUrl_NullValue() {
 node.value = attributes;
 String actual = node.absUrl(null);
 Assert.assertNull(actual);
 }

 // Testing baseUri method

 @Test
 public void testBaseUri() {
 LeafNode leafNode = new LeafNode();
 leafNode.parent = new ElementNode("parent");
 leafNode.parent.value = "http://test.com";
 Assert.assertEquals("http://test.com", leafNode.baseUri());
 }

 @Test
 public void testBaseUri_NoParent() {
 LeafNode leafNode = new LeafNode();
 Assert.assertEquals("", leafNode.baseUri());
 }

 // Testing doSetBaseUri method

 @Test(expected = UnsupportedOperationException.class)
 public void testDoSetBaseUri() {
 node.doSetBaseUri("Test Base URI");
 }

 // Testing childNodeSize method

 @Test
 public void testChildNodeSize() {
 int actual = node.childNodeSize();
 Assert.assertEquals(0, actual);
 }

 @Test
 public void testChildNodeSize_EmptyNode() {
 LeafNode emptyNode = new LeafNode();
 int actual = emptyNode.childNodeSize();
 Assert.assertEquals(0, actual);
 }

 // Testing ensureChildNodes method

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 }

}