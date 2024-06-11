import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new LeafNode("test"); {
 Value value = new Value("testValue");
 leafNode.value = value;
 };

 assertTrue(leafNode.hasAttributes());

 LeafNode leafNode2 = new LeafNode("test"); {};
 assertFalse(leafNode2.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new LeafNode("test") {
 Value value = new Attributes();
 };
 leafNode.coreValue("testAttribute");

 Assertions.assertNotNull(leafNode.attributes());
 assertEquals("testAttribute", leafNode.attributes().get("test"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAttributesWhenHasNoAttributes() {
 LeafNode leafNode = new LeafNode("test") {};
 leafNode.ensureAttributes();
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new LeafNode("test") {
 Value value = "testValue";
 };
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 LeafNode leafNode = new LeafNode("test") {};
 leafNode.coreValue("newValue");
 assertEquals("newValue", leafNode.coreValue());
 }

 @Test
 public void testAttr() {
 LeafNode leafNode = new LeafNode("test") {
 Value value = new Attributes();
 };
 leafNode.coreValue("test");
 leafNode.attr("test", "testValue");
 assertEquals("testValue", leafNode.attr("test"));
 }

 @Test
 public void testAttrSetter() {
 LeafNode leafNode = new LeafNode("test") {};
 leafNode.attr("test", "testValue");
 assertEquals("testValue", leafNode.attr("test"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new LeafNode("test") {
 Value value = new Attributes();
 };
 leafNode.coreValue("test");
 leafNode.attr("test", "testValue");
 assertTrue(leafNode.hasAttr("test"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new LeafNode("test") {
 Value value = new Attributes();
 };
 leafNode.coreValue("test");
 leafNode.attr("test", "testValue");
 leafNode.removeAttr("test");
 assertFalse(leafNode.hasAttr("test"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new LeafNode("test") {
 Value value = new Attributes();
 };
 leafNode.coreValue("test");
 leafNode.attr("abs:url", "http://test.com");
 assertEquals("http://test.com", leafNode.absUrl("abs:url"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode1 = new LeafNode("test1") {};
 LeafNode parentNode = new Elements.Element("parent") {};
 parentNode.add(leafNode1);
 assertEquals("", leafNode1.baseUri());

 LeafNode leafNode2 = new Elements.Element("test2") {};
 parentNode.add(leafNode2);
 assertEquals("", leafNode2.baseUri());

 LeafNode leafNode3 = new Elements.Element("test3") {};
 parentNode.add(leafNode3);
 parentNode.attr("base", "http://test.com");
 assertEquals("http://test.com", leafNode3.baseUri());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 LeafNode leafNode = new LeafNode("test") {};
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new LeafNode("test") {};
 leafNode.ensureChildNodes();
 }
}