import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.jsoup.helper.Validate.*;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setUp() {
 leafNode = new LeafNode();
 }

 // Test for hasAttributes() method
 @Test
 public void testHasAttributes() {
 // Basic test
 assertTrue(leafNode.hasAttributes());
 // Test with value as String
 leafNode.coreValue("test");
 assertFalse(leafNode.hasAttributes());
 // Test with value as Attributes
 Attributes attributes = new Attributes();
 leafNode.value = attributes;
 assertTrue(leafNode.hasAttributes());
 }

 // Test for attributes() method
 @Test
 public void testAttributes() {
 // Test with no attributes
 assertNull(leafNode.attributes());
 // Test with attributes
 Attributes attributes = new Attributes();
 leafNode.value = attributes;
 attributes.put("test", "value");
 assertEquals(attributes, leafNode.attributes());
 }

 // Test for coreValue() method
 @Test(expected = UnsupportedOperationException.class)
 public void testCoreValueWithEmptyNode() {
 leafNode.coreValue();
 }

 @Test
 public void testCoreValueWithStringValue() {
 leafNode.coreValue("test");
 assertEquals("test", leafNode.coreValue());
 }

 @Test
 public void testCoreValueWithAttributeMap() {
 Attributes attributes = new Attributes();
 leafNode.value = attributes;
 attributes.put("test", "value");
 assertEquals("value", leafNode.coreValue());
 }

 // Test for coreValue(String value) method
 @Test
 public void testCoreValueWithString() {
 leafNode.coreValue("test");
 leafNode.coreValue("newValue");
 assertEquals("newValue", leafNode.coreValue());
 }

 // Test for attr(String key) method
 @Test
 public void testAttrWithEmptyNode() {
 assertNull(leafNode.attr("test"));
 }

 @Test
 public void testAttrWithStringValue() {
 leafNode.coreValue("test");
 assertEquals("test", leafNode.attr("nodeName"));
 }

 @Test
 public void testAttrWithAttributeMap() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 leafNode.value = attributes;
 assertEquals("value", leafNode.attr("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrWithNullKey() {
 leafNode.attr(null);
 }

 // Test for attr(String key, String value) method
 @Test
 public void testAttrWithEmptyNodeAndKeyEqualsNodeName() {
 leafNode.coreValue("test");
 leafNode.attr("nodeName", "value");
 assertEquals("value", leafNode.coreValue());
 }

 @Test
 public void testAttrWithEmptyNodeAndDifferentKey() {
 leafNode.coreValue("test");
 leafNode.attr("differentKey", "value");
 assertEquals("test", leafNode.coreValue());
 }

 @Test
 public void testAttrWithAttributeMapAndKeyEqualsNodeName() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 leafNode.value = attributes;
 leafNode.attr("nodeName", "newValue");
 assertEquals("newValue", leafNode.coreValue());
 }

 @Test
 public void testAttrWithAttributeMapAndDifferentKey() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 leafNode.value = attributes;
 leafNode.attr("differentKey", "newValue");
 assertEquals("value", leafNode.attr("test"));
 }

 // Test for hasAttr(String key) method
 @Test
 public void testHasAttrWithEmptyNode() {
 assertFalse(leafNode.hasAttr("test"));
 }

 @Test
 public void testHasAttrWithStringValue() {
 leafNode.coreValue("test");
 assertTrue(leafNode.hasAttr("nodeName"));
 }

 @Test
 public void testHasAttrWithAttributeMap() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 leafNode.value = attributes;
 assertTrue(leafNode.hasAttr("test"));
 }

 // Test for removeAttr(String key) method
 @Test
 public void testRemoveAttrWithEmptyNode() {
 assertFalse(leafNode.hasAttr("test"));
 leafNode.removeAttr("test");
 assertFalse(leafNode.hasAttr("test"));
 }

 @Test
 public void testRemoveAttrWithStringValue() {
 leafNode.coreValue("test");
 leafNode.removeAttr("nodeName");
 assertFalse(leafNode.hasAttr("nodeName"));
 }

 @Test
 public void testRemoveAttrWithAttributeMap() {
 Attributes attributes = new Attributes();attributes.put("test", "value");
 leafNode.value = attributes;
 attributes.remove("test");
 assertFalse(leafNode.hasAttr("test"));
 }

 // Test for absUrl(String key) method
 @Test
 public void testAbsUrlWithEmptyNode() {
 assertNull(leafNode.absUrl("test"));
 }

 // Test for baseUri() method
 @Test
 public void testBaseUriWithEmptyNode() {
 assertEquals("", leafNode.baseUri());
 }

 @Test
 public void testBaseUriWithParentNode() {
 LeafNode parentNode = new LeafNode();
 parentNode.coreValue("http://test.com");
 leafNode.parent(parentNode);
 assertEquals("http://test.com", leafNode.baseUri());
 }

 // Test for doSetBaseUri(String baseUri) method
 @Test
 public void testDoSetBaseUri() {
 // noop
 }

 // Test for childNodeSize() method
 @Test
 public void testChildNodeSize() {
 assertEquals(0, leafNode.childNodeSize());
 }

 // Test for ensureChildNodes() method
 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 leafNode.ensureChildNodes();
 }
}