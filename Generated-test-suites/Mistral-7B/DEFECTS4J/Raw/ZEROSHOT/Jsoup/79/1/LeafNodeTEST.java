import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setup() {
 leafNode = new LeafNode("test") {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 }

 @Test
 public void testHasAttributes() {
 assertFalse(leafNode.hasAttributes());
 leafNode.coreValue("testAttribute:value");
 assertTrue(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 assertNull(leafNode.attributes());
 leafNode.coreValue("testAttribute:value1");
 leafNode.coreValue("testAttribute:value2", "testAttribute:value3");
 Attributes attributes = leafNode.attributes();
 assertEquals("value1", attributes.get("testAttribute"));
 assertEquals("value3", attributes.get("testAttribute").get(1).getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureAttributes() {
 leafNode.ensureAttributes();
 }

 @Test
 public void testCoreValue() {
 assertEquals("test", leafNode.coreValue());
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testAttr() {
 assertEquals(Emptystring.instance(), leafNode.attr("testAttribute"));
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.attr("testAttribute"));
 }

 @Test
 public void testAttrSetter() {
 leafNode.coreValue("testValue1");
 leafNode.attr("testAttribute", "testValue2");
 assertEquals("testValue2", leafNode.attr("testAttribute"));
 }

 @Test
 public void testHasAttr() {
 assertFalse(leafNode.hasAttr("testAttribute"));
 leafNode.coreValue("testValue");
 assertTrue(leafNode.hasAttr("testAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 leafNode.coreValue("testValue1");
 leafNode.attr("testAttribute", "testValue2");
 leafNode.removeAttr("testAttribute");
 assertFalse(leafNode.hasAttr("testAttribute"));
 }

 @Test
 public void testAbsUrl() {
 assertEquals("", leafNode.absUrl("testAttribute"));
 }

 @Test
 public void testBaseUri() {
 LeafNode parentNode = new LeafNode("parentTest") {
 @Override
 public String nodeName() {
 return "parentNode";
 }
 };
 leafNode.parent(parentNode);
 assertEquals("parentTest", leafNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 leafNode.ensureChildNodes();
 }
}