import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new TextNode("Test Text");
 assertFalse(leafNode.hasAttributes());

 LeafNode leafNodeWithAttributes = new TextNode("Test Text");
 Attributes attributes = new Attributes();
 attributes.put("testAttribute", "testValue");
 leafNodeWithAttributes.setValue(attributes);
 assertTrue(leafNodeWithAttributes.hasAttributes());
 }

 @Test
 public void testGetAttributes() {
 LeafNode leafNodeWithAttributes = new TextNode("Test Text");
 Attributes attributes = new Attributes();
 attributes.put("testAttribute1", "testValue1");
 attributes.put("testAttribute2", "testValue2");
 leafNodeWithAttributes.setValue(attributes);

 Attributes actualAttributes = leafNodeWithAttributes.attributes();
 assertEquals("testValue1", actualAttributes.get("testAttribute1"));
 assertEquals("testValue2", actualAttributes.get("testAttribute2"));
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNodeWithCoreValue = new TextNode("Test Text");
 leafNodeWithCoreValue.coreValue("Test Core Value");
 assertEquals("Test Core Value", leafNodeWithCoreValue.coreValue());
 }

 @Test
 public void testCoreValue_Null() {
 LeafNode leafNode = new TextNode("Test Text");
 assertNull(leafNode.coreValue());
 }

 @Test
 public void testCoreValue_EmptyString() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.coreValue("");
 assertEquals("", leafNode.coreValue());
 }

 @Test
 public void testAttr_String() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr("testAttribute", "testValue");
 assertEquals("testValue", leafNode.attr("testAttribute"));
 }

 @Test
 public void testAttr_Null() {
 LeafNode leafNode = new TextNode("Test Text");
 assertNull(leafNode.attr("testAttribute"));
 }

 @Test
 public void testAttr_EmptyString() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr("testAttribute", "");
 assertEquals("", leafNode.attr("testAttribute"));
 }

 @Test
 public void testAttr_NonExistent() {
 LeafNode leafNode = new TextNode("Test Text");
 assertNull(leafNode.attr("nonExistentAttribute"));
 }

 @Test(expected = NullPointerException.class)
 public void testAttr_NullValue() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.attr("testAttribute", null);
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNodeWithAttributes = new TextNode("Test Text");
 Attributes attributes = new Attributes();
 attributes.put("testAttribute1", "testValue1");
 attributes.put("testAttribute2", "testValue2");
 leafNodeWithAttributes.setValue(attributes);

 assertTrue(leafNodeWithAttributes.hasAttr("testAttribute1"));
 assertTrue(leafNodeWithAttributes.hasAttr("testAttribute2"));
 assertFalse(leafNodeWithAttributes.hasAttr("nonExistentAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNodeWithAttributes = new TextNode("Test Text");
 Attributes attributes = new Attributes();
 attributes.put("testAttribute1", "testValue1");
 attributes.put("testAttribute2", "testValue2");
 leafNodeWithAttributes.setValue(attributes);

 leafNodeWithAttributes.removeAttr("testAttribute1");
 assertFalse(leafNodeWithAttributes.hasAttr("testAttribute1"));
 assertTrue(leafNodeWithAttributes.hasAttr("testAttribute2"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new TextNode("Test Text");
 assertNull(leafNode.absUrl("testKey"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode = new TextNode("Test Text");
 LeafNode parentNode = new Elements.Element("Test Parent");
 parentNode.coreValue("Test Base Uri");
 leafNode.parent(parentNode);
 assertEquals("Test Base Uri", leafNode.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode leafNode = new TextNode("Test Text");
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new TextNode("Test Text");
 leafNode.ensureChildNodes();
 }
}