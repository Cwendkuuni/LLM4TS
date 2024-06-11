import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

public class LeafNodeTest {

 private LeafNode node;

 @Before
 public void setup() {
 node = new LeafNode();
 }

 @Test
 public void testHasAttributes() {
 // Test regular LeafNode
 assertFalse(node.hasAttributes());

 // Test LeafNode with attributes
 node.coreValue("testKey", "testValue");
 assertTrue(node.hasAttributes());
 }

 @Test
 public void testAttributes() {
 // Test getting attributes for a LeafNode with no attributes
 assertNull(node.attributes());

 // Test getting attributes for a LeafNode with attributes
 node.coreValue("testKey1", "testValue1");
 node.coreValue("testKey2", "testValue2");
 Attributes attributes = node.attributes();
 assertEquals("testValue1", attributes.get("testKey1"));
 assertEquals("testValue2", attributes.get("testKey2"));
 }

 @Test
 public void testEnsureAttributes() {
 // Test ensuring attributes for a LeafNode with no attributes
 node.ensureAttributes();
 assertInstanceOf(Attributes.class, node.value);

 // Test ensuring attributes for a LeafNode with attributes
 node.coreValue("testKey1", "testValue1");
 node.coreValue("testKey2", "testValue2");
 node.ensureAttributes();
 assertSame(node.value, node.attributes());
 }

 @Test
 public void testCoreValue() {
 // Test getting core value for a LeafNode with a string value
 node.coreValue("testValue");
 assertEquals("testValue", node.coreValue());

 // Test getting core value for a LeafNode with attributes
 node.coreValue("testKey", "testValue");
 assertEquals("testValue", node.coreValue());
 assertEquals("testKey", node.nodeName());
 }

 @Test
 public void testCoreValueSetting() {
 // Test setting core value for a LeafNode with no attributes
 node.coreValue(null);
 assertNull(node.value);

 // Test setting core value for a LeafNode with attributes
 node.coreValue("testKey", "testValue");
 assertEquals("testValue", node.coreValue());
 assertEquals("testKey", node.nodeName());
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullKey() {
 node.attr(null, "testValue");
 }

 @Test
 public void testAttrSetString() {
 // Test setting attribute for a LeafNode with no attributes
 node.attr("testKey", "testValue");
 assertEquals("testValue", node.attr("testKey"));

 // Test setting attribute for a LeafNode with attributes
 node.coreValue("testKey1", "testValue1");
 node.coreValue("testKey2", "testValue2");
 node.attr("testKey3", "testValue3");
 assertEquals("testValue3", node.attr("testKey3"));
 }

 @Test
 public void testAttrSetNullValue() {
 node.coreValue("testKey", "testValue");
 node.attr("testKey", null);
 assertNull(node.attr("testKey"));
 }

 @Test
 public void testAttrSetSameKey() {
 node.coreValue("testKey1", "testValue1");
 node.attr("testKey1", "testValue2");
 assertEquals("testValue2", node.attr("testKey1"));
 }

 @Test
 public void testHasAttr() {
 // Test checking for attribute on a LeafNode with no attributes
 assertFalse(node.hasAttr("testKey"));

 // Test checking for attribute on a LeafNode with attributes
 node.coreValue("testKey", "testValue");
 assertTrue(node.hasAttr("testKey"));
 }

 @Test
 public void testRemoveAttr() {
 // Test removing attribute from a LeafNode with no attributes
 node.removeAttr("testKey");
 assertFalse(node.hasAttr("testKey"));

 // Test removing attribute from a LeafNode with attributes
 node.coreValue("testKey1", "testValue1");
 node.coreValue("testKey2", "testValue2");
 node.attr("testKey3", "testValue3");
 node.removeAttr("testKey3");
 assertFalse(node.hasAttr("testKey3"));
 }

 @Test
 public void testAbsUrl() {
 // Test getting absUrl for a LeafNode with no baseUri
 assertNull(node.absUrl("testKey"));

 // Test getting absUrl for a LeafNode with a baseUri
 node.coreValue("baseUri", "http://test.com");
 assertNotNull(node.absUrl("testKey"));
 }

 @Test
 public void testBaseUri() {
 // Test getting baseUri for a LeafNode with no parent
 assertEquals("", node.baseUri());

 // Test getting baseUri for a LeafNode with a parent
 node.coreValue("parentNode", new Element("parent"));
 assertEquals(node.parent().baseUri(), node.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 assertEquals(0, node.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 }
}