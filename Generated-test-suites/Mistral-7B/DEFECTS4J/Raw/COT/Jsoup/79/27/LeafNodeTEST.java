import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LeafNodeTest {

 LeafNode leafNode;
 Attributes attributes;

 @Before
 public void setUp() {
 leafNode = new LeafNode() {
 };
 attributes = new Attributes();
 }

 @Test
 public void testHasAttributes() {
 assertFalse(leafNode.hasAttributes());
 }

 @Test
 public void testGetAttributes() {
 leafNode.value = attributes;
 assertTrue(leafNode.hasAttributes());
 assertSame(attributes, leafNode.attributes());
 }

 @Test
 public void testCoreValue() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueEmpty() {
 assertNull(leafNode.coreValue());
 }

 @Test(expected = NullPointerException.class)
 public void testCoreValueNullKey() {
 leafNode.coreValue(null);
 }

 @Test
 public void testAttrGetAndSet() {
 leafNode.attr("testKey", "testValue");
 assertEquals("testValue", leafNode.attr("testKey"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttrGetNullKey() {
 leafNode.attr(null, "testValue");
 }

 @Test(expected = NullPointerException.class)
 public void testAttrSetNullValue() {
 leafNode.attr("testKey", null);
 }

 @Test
 public void testHasAttr() {
 leafNode.attr("testKey", "testValue");
 assertTrue(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testRemoveAttr() {
 leafNode.attr("testKey", "testValue");
 leafNode.removeAttr("testKey");
 assertFalse(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testBaseUri() {
 leafNode.coreValue("http://test.com");
 assertEquals("http://test.com", leafNode.baseUri());
 }

 @Test
 public void testBaseUriEmpty() {
 assertNull(leafNode.baseUri());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodesSize() {
 leafNode.childNodeSize();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 leafNode.ensureChildNodes();
 }
}