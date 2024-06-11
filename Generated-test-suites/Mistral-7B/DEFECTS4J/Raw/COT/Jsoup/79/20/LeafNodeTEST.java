import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setup() {
 leafNode = new LeafNode() {
 };
 }

 // Test for hasAttributes() method
 @Test
 public void testHasAttributes() {
 // Test when value is a string
 leafNode.value = "testString";
 assertFalse(leafNode.hasAttributes());

 // Test when value is an Attributes object
 Attributes attributes = new Attributes();
 leafNode.value = attributes;
 attributes.put("testKey", "testValue");
 assertTrue(leafNode.hasAttributes());
 }

 // Test for attributes() method
 @Test
 public void testAttributes() {
 // Test when no attributes are present
 assertNull(leafNode.attributes());

 // Test when attributes are present
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.value = attributes;
 Assert.assertEquals(attributes, leafNode.attributes());
 }

 // Test for coreValue() method
 @Test
 public void testCoreValue() {
 leafNode.value = "testString";
 assertEquals("testString", leafNode.coreValue());
 }

 // Test for coreValue(String) method
 @Test
 public void testCoreValue_Set() {
 leafNode.coreValue("testString");
 assertEquals("testString", leafNode.coreValue());
 }

 // Test for attr(String) method
 @Test
 public void testAttr() {
 // Test when no attributes are present
 assertEquals(EmptyString, leafNode.attr("testKey"));

 // Test when attributes are present
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.value = attributes;
 assertEquals("testValue1", leafNode.attr("testKey1"));
 }

 // Test for attr(String, String) method
 @Test
 public void testAttr_Set() {
 // Test when no attributes are present
 leafNode.attr("testKey", "testValue");
 assertEquals("testValue", leafNode.attr("testKey"));

 // Test when attributes are already present
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.value = attributes;
 leafNode.attr("testKey3", "testValue3");
 assertEquals("testValue3", leafNode.attr("testKey3"));
 }

 // Test for hasAttr(String) method
 @Test
 public void testHasAttr() {
 // Test when no attributes are present
 assertFalse(leafNode.hasAttr("testKey"));

 // Test when attributes are present
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.value = attributes;
 assertTrue(leafNode.hasAttr("testKey1"));
 }

 // Test for removeAttr(String) method
 @Test
 public void testRemoveAttr() {
 // Test when no attributes are present
 assertFalse(leafNode.hasAttr("testKey"));
 leafNode.removeAttr("testKey");
 assertFalse(leafNode.hasAttr("testKey"));

 // Test when attributes are present
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.value = attributes;
 assertEquals(2, leafNode.attributes().size());
 leafNode.removeAttr("testKey1");
 assertEquals(1, leafNode.attributes().size());
 }

 // Test for absUrl(String) method
 @Test(expected = UnsupportedOperationException.class)
 public void testAbsUrl() {
 leafNode.absUrl("testKey");
 }

 // Test for baseUri() method
 @Test
 public void testBaseUri() {
 Node parentNode = new Node() {};
 parentNode.coreValue("http://test.com");
 leafNode.coreValue("testString");
 leafNode.parent(parentNode);
 assertEquals("http://test.com", leafNode.baseUri());
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

 // Test for null argument in constructor
 @Test(expected = IllegalArgumentException.class)
 public void testLeafNodeNullConstructor() {
 new LeafNode(null);
 }

 // Test for null nodeName in attr method
 @Test(expected = IllegalArgumentException.class)
 public void testAttrNullNodeName() {
 leafNode.attr(null, "testValue");
 }

 // Test for null value in attr method
 @Test(expected = IllegalArgumentException.class)
 public void testAttrNullValue() {
 attributes.put("testKey", null);
 leafNode.value = attributes;
 try {
 leafNode.attr("testKey", null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for null value in coreValue method
 @Test(expected = IllegalArgumentException.class)
 public void testCoreValueNullValue() {
 leafNode.coreValue(null);
 }

 // Test for null key in attr and hasAttr methods
 @Test(expected = IllegalArgumentException.class)
 public void testAttrAndHasAttrNullKey() {
 leafNode.attr(null);
 leafNode.hasAttr(null);
 }
}