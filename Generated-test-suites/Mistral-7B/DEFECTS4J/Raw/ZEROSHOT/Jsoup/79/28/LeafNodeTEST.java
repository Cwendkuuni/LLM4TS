import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode createLeafNode() {
 return new LeafNode() {
 };
 }

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = createLeafNode();
 assertFalse(leafNode.hasAttributes());

 LeafNode leafNodeWithAttributes = new LeafNode() {
 @Override
 public Attributes attributes() {
 return new Attributes();
 }
 };
 assertTrue(leafNodeWithAttributes.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = createLeafNode();
 assertNull(leafNode.attributes());

 LeafNode leafNodeWithAttributes = new LeafNode() {
 @Override
 public Attributes attributes() {
 return newAttributes();
 }
 };
 Attributes attributes = leafNodeWithAttributes.attributes();
 assertNotNull(attributes);
 assertTrue(attributes.isEmpty());
 }

 @Test
 public void testEnsureAttributes() {
 LeafNode leafNode = createLeafNode();
 leafNode.coreValue("testValue");
 Attributes attributes = leafNode.attributes();
 assertEquals("testValue", attributes.get("name"));
 assertEquals(1, attributes.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testEnsureAttributesTwice() {
 LeafNode leafNode = createLeafNode();
 leafNode.coreValue("testValue");
 leafNode.ensureAttributes();
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = createLeafNode();
 assertNull(leafNode.coreValue());

 LeafNode leafNodeWithValue = new LeafNode() {
 @Override
 public String coreValue() {
 return "testValue";
 }
 };
 assertEquals("testValue", leafNodeWithValue.coreValue());
 }

 @Test
 public void testCoreValueSet() {
 LeafNode leafNode = createLeafNode();
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testAttr() {
 LeafNode leafNode = createLeafNode();
 assertEquals(EmptyString, leafNode.attr("name"));

 LeafNode leafNodeWithValue = new LeafNode() {
 @Override
 public String attr(String key) {
 return "testValue";
 }
 };
 assertEquals("testValue", leafNodeWithValue.attr("name"));
 }

 @Test
 public void testAttrSet() {
 LeafNode leafNode = createLeafNode();
 leafNode.attr("name", "testValue");
 assertEquals("testValue", leafNode.attr("name"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = createLeafNode();
 assertFalse(leafNode.hasAttr("name"));

 LeafNode leafNodeWithAttribute = new LeafNode() {
 @Override
 public boolean hasAttr(String key) {
 return true;
 }
 };
 assertTrue(leafNodeWithAttribute.hasAttr("name"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = createLeafNode();
 leafNode.coreValue("testValue");
 leafNode.attr("name", "testValue");
 leafNode.removeAttr("name");
 assertNull(leafNode.attr("name"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = createLeafNode();
 assertEquals(EmptyString, leafNode.absUrl(""));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode = createLeafNode();
 assertEquals(EmptyString, leafNode.baseUri());

 LeafNode leafNodeWithParent = new LeafNode() {
 @Override
 public Node parent() {
 return new ElementNode("parent");
 }
 };
 assertEquals("parent", leafNodeWithParent.baseUri());
 }

 @Test
 public void testChildNodeSize() {
 LeafNode leafNode = createLeafNode();
 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = createLeafNode();
 leafNode.ensureChildNodes();
 }
}