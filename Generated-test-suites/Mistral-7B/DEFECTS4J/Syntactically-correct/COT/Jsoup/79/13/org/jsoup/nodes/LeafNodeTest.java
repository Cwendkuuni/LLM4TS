import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import static org.jsoup.helper.Validate.*;

public class LeafNodeTest {

 private LeafNode node;

 @Before
 public void setup() {
 node = new LeafNode() {
 };
 }

 @Test
 public void testHasAttributes() {
 assertFalse(node.hasAttributes());
 }

 @Test
 public void testAttributes() {
 node.coreValue("test");
 Attributes attributes = node.attributes();
 assertEquals("test", attributes.get("nodeName"));
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAttributesWhenNoValue() {
 node.ensureAttributes();
 }

 @Test
 public void testEnsureAttributesWhenValueIsString() {
 node.coreValue("test");
 node.ensureAttributes();
 Assert.assertSame(node.attributes(), node.value);
 }

 @Test
 public void testEnsureAttributesWhenValueIsAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 node.value = attributes;
 node.ensureAttributes();
 Assert.assertSame(node.attributes(), node.value);
 }

 @Test
 public void testCoreValue() {
 node.coreValue("test");
 assertEquals("test", node.coreValue());
 }

 @Test
 public void testCoreValueWithNullNodeName() {
 node.coreValue(null);
 assertNull(node.coreValue());
 }

 @Test
 public void testCoreValueWithEmptyString() {
 node.coreValue("");
 assertEquals("", node.coreValue());
 }

 @Test
 public void testCoreValueWhenHasAttributes() {
 node.ensureAttributes();
 node.coreValue("test");
 assertEquals("test", node.coreValue());
 }

 @Test
 public void testAttrWhenNoAttributes() {
 assertNull(node.attr("test"));
 }

 @Test
 public void testAttrWhenHasStringValue() {
 node.coreValue("test");
 assertEquals("test", node.attr("nodeName"));
 }

 @Test
 public void testAttrWhenHasAttributes() {
 node.ensureAttributes();
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 node.attr("nodeName", "newValue");
 assertEquals("newValue", node.attr("nodeName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrWithNullKey() {
 node.attr(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrWithEmptyStringKey() {
 node.attr("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrWithNullValue() {
 node.attr("test", null);
 }

 @Test
 public void testHasAttr() {
 node.ensureAttributes();
 assertTrue(node.hasAttr("nodeName"));
 }

 @Test
 public void testHasAttrWhenNoAttributes() {
 assertFalse(node.hasAttr("nodeName"));
 }

 @Test
 public void testRemoveAttr() {
 node.ensureAttributes();
 node.removeAttr("nodeName");
 assertFalse(node.hasAttr("nodeName"));
 }

 @Test
 public void testAttrWithExistingKey() {
 node.ensureAttributes();
 node.attr("test", "value1");
 node.attr("test", "value2");
 assertEquals("value2", node.attr("test"));
 }

 @Test
 public void testAbsUrlWithNullBaseUri() {
 assertEquals("", node.absUrl("test"));
 }

 @Test
 public void testAbsUrlWithEmptyBaseUri() {
 assertEquals("", node.absUrl("test"));
 }

 @Test
 public void testBaseUri() {
 Node parent = new Element("parent");
 parent.coreValue("baseUrl");
 node.parent(parent);
 assertEquals("baseUrl", node.baseUri());
 }

 @Test
 public void testBaseUriWhenNoParent() {
 assertEquals("", node.baseUri());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 node.childNodeSize();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 }

}