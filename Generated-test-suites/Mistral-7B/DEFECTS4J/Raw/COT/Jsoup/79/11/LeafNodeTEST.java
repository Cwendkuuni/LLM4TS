import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import static org.jsoup.nodes.AttributeSelectors.attr;
import static org.jsoup.nodes.AttributeSelectors.hasAttr;
import static org.jsoup.nodes.AttributeSelectors.hasAttrValue;
import static org.jsoup.nodes.ElementSelectors.tag;
import static org.jsoup.helper.W3C.CSS_DISPLAY_NONE;

public class LeafNodeTest {

 LeafNode testLeafNode;
 Attributes testAttributes;

 @Before
 public void setUp() {
 testLeafNode = new Element("testLeafNode");
 testAttributes = new Attributes();
 testLeafNode.value = testAttributes;
 }

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new Element("testLeafNode");
 assertFalse(leafNode.hasAttributes());

 testLeafNode.coreValue("testKey", "testValue");
 assertTrue(testLeafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 testLeafNode.coreValue("testKey1", "testValue1");
 testLeafNode.coreValue("testKey2", "testValue2");

 Attributes attributes = testLeafNode.attributes();
 assertEquals("testValue1", attributes.get("testKey1"));
 assertEquals("testValue2", attributes.get("testKey2"));
 }

 @Test
 public void testEnsureAttributes() {
 testLeafNode.value = "testValueBefore";
 testLeafNode.ensureAttributes();
 assertEquals("testValueBefore", testLeafNode.attr(testLeafNode.nodeName()));

 testLeafNode.value = testAttributes;
 testLeafNode.ensureAttributes();
 assertEquals("testKey1", testLeafNode.nodeName());
 assertEquals("testValue1", testLeafNode.attr("testKey1"));
 assertEquals("testValue2", testLeafNode.attr("testKey2"));
 }

 @Test
 public void testCoreValue() {
 testLeafNode.coreValue("testValue");
 assertEquals("testValue", testLeafNode.coreValue());
 }

 @Test
 public void testCoreValueWithAttributes() {
 testLeafNode.coreValue("testKey", "testValueBefore");
 testLeafNode.coreValue("testKey", "testValue");

 assertEquals("testValue", testLeafNode.coreValue());
 assertEquals("testValueBefore", testLeafNode.attr("testKey"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullKey() {
 testLeafNode.attr(null);
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullValue() {
 testLeafNode.attr("testKey", null);
 }

 @Test
 public void testAttrWithEmptyValue() {
 testLeafNode.coreValue("testKey", "");
 assertEquals("", testLeafNode.attr("testKey"));
 }

 @Test
 public void testAttrWithEmptyStringAttributes() {
 testLeafNode.value = testAttributes;
 testLeafNode.attr("testKey", "");
 assertEquals("", testLeafNode.attr("testKey"));
 }

 @Test
 public void testAttrWithSameKeyAndValueMultipleTimes() {
 testLeafNode.value = testAttributes;
 testLeafNode.attr("testKey", "testValue");
 testLeafNode.attr("testKey", "testValue2");

 assertEquals("testValue2", testLeafNode.attr("testKey"));
 }

 @Test
 public void testAttrWithDifferentKeys() {
 testLeafNode.value = testAttributes;
 testLeafNode.attr("testKey1", "testValue1");
 testLeafNode.attr("testKey2", "testValue2");

 assertEquals("testValue1", testLeafNode.attr("testKey1"));
 assertEquals("testValue2", testLeafNode.attr("testKey2"));
 }

 @Test
 public void testHasAttr() {
 testLeafNode.coreValue("testKey", "testValue");
 assertTrue(testLeafNode.hasAttr("testKey"));
 }

 @Test
 public void testHasAttrFalse() {
 testLeafNode.coreValue("testKey1", "testValue1");
 testLeafNode.coreValue("testKey2", "testValue2");

 assertFalse(testLeafNode.hasAttr("testKey3"));
 }

 @Test
 public void testRemoveAttr() {
 testLeafNode.coreValue("testKey", "testValue");
 testLeafNode.removeAttr("testKey");
 assertFalse(testLeafNode.hasAttr("testKey"));
 }

 @Test
 public void testAttrWithEmptyValueAndRemove() {
 testLeafNode.coreValue("testKey", "");
 testLeafNode.attr("testKey", "testValue");
 testLeafNode.removeAttr("testKey");
 assertFalse(testLeafNode.hasAttr("testKey"));
 }

 @Test
 public void testAttrWithNullValueAndRemove() {
 testLeafNode.coreValue("testKey", null);
 testLeafNode.attr("testKey", "testValue");
 testLeafNode.removeAttr("testKey");
 assertFalse(testLeafNode.hasAttr("testKey"));
 }

 @Test
 public void testAttrWithSameKeyAndValueMultipleTimesAndRemove() {
 testLeafNode.coreValue("testKey", "testValue");
 testLeafNode.attr("testKey", "testValue2");
 testLeafNode.attr("testKey", "testValue3");
 testLeafNode.removeAttr("testKey");
 assertFalse(testLeafNode.hasAttr("testKey"));
 }

 @Test
 public void testAttrWithDifferentKeysAndRemove() {
 testLeafNode.coreValue("testKey1", "testValue1");
 testLeafNode.coreValue("testKey2", "testValue2");
 testLeafNode.attr("testKey1", "testValue1_1");
 testLeafNode.removeAttr("testKey1");
 assertFalse(testLeafNode.hasAttr("testKey1"));
 assertEquals("testValue1", testLeafNode.attr("testKey1"));
 }

 @Test
 public void testAttrWithDifferentKeysAndRemoveAll() {
 testLeafNode.coreValue("testKey1", "testValue1");
 testLeafNode.coreValue("testKey2", "testValue2");
 testLeafNode.attr("testKey1", "testValue1_1");
 testLeafNode.attr("testKey2", "testValue2_1");
 testLeafNode.removeAttrs();
 assertFalse(testLeafNode.hasAttr("testKey1"));
 assertFalse(testLeafNode.hasAttr("testKey2"));
 }

 @Test
 public void testAbsUrlNullKey() {
 testLeafNode.coreValue("testKey", null);
 assertEquals(null, testLeafNode.absUrl("testKey"));
 }

 @Test
 public void testBaseUriEmpty() {
 testLeafNode.coreValue("testKey", "testValue");
 assertEquals("", testLeafNode.baseUri());
 }

 @Test
 public void testBaseUriWithParentNode() {
 Document document = new Document();
 Element parentNode = tag("parent", "id", "testParent");
 document.appendElement(parentNode);
 testLeafNode = tag("testLeafNode", "id", "testLeafNode");
 parentNode.appendElement(testLeafNode);
 assertEquals("testParent", testLeafNode.baseUri());
 }

 @Test
 public void testDoSetBaseUri() {
 testLeafNode.coreValue("testKey", "testValue");
 testLeafNode.doSetBaseUri("testBaseUri");
 assertEquals("testBaseUri", testLeafNode.baseUri());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 testLeafNode.childNodeSize();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 testLeafNode.ensureChildNodes();
 }
}