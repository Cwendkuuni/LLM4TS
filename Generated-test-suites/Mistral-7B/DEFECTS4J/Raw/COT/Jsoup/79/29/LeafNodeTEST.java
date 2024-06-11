import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Node.*;

public class LeafNodeTest {

 private LeafNode node;

 @Test
 public void testHasAttributes() {
 node = new Element("tagName");
 Assert.assertFalse(node.hasAttributes());

 node.coreValue("testAttribute");
 Assert.assertTrue(node.hasAttributes());
 }

 @Test
 public void testAttributes() {
 node = new Element("tagName");
 Attributes attributes = node.attributes();
 Assert.assertNull(attributes);

 node.coreValue("testAttribute:value");
 attributes = node.attributes();
 Assert.assertEquals("value", attributes.get("testAttribute"));
 }

 @Test
 public void testCoreValue() {
 node = new Element("tagName");
 node.coreValue("testValue");
 Assert.assertEquals("testValue", node.coreValue());
 }

 @Test
 public void testCoreValueSetting() {
 node = new Element("tagName");
 node.coreValue("testValue");
 node.coreValue("newValue");
 Assert.assertEquals("newValue", node.coreValue());
 }

 @Test
 public void testAttr() {
 node = new Element("tagName");
 Assert.assertEquals(EmptyString, node.attr("testAttribute"));

 node.coreValue("testAttribute:value");
 Assert.assertEquals("value", node.attr("testAttribute"));
 }

 @Test
 public void testAttrSetting() {
 node = new Element("tagName");
 node.attr("testAttribute", "value");
 Assert.assertEquals("value", node.attr("testAttribute"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullKey() {
 node.attr(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullValue() {
 node.attr("testAttribute", null);
 }

 @Test
 public void testHasAttr() {
 node = new Element("tagName");
 Assert.assertFalse(node.hasAttr("testAttribute"));

 node.coreValue("testAttribute:value");
 Assert.assertTrue(node.hasAttr("testAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 node = new Element("tagName");
 node.coreValue("testAttribute:value");
 node.removeAttr("testAttribute");
 Assert.assertFalse(node.hasAttr("testAttribute"));
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