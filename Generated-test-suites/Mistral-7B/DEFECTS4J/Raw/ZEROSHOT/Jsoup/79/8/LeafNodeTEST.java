import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 assertTrue(leafNode.hasAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 assertEquals(0, leafNode.childNodeSize());
 }

 @Test
 public void testGetAttributes() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");
 leafNode.attr("testAttr", "testValue");

 Attributes attributes = leafNode.attributes();
 assertEquals("testValue", attributes.get("testAttr"));
 }

 @Test
 public void testGetCoreValue() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testSetCoreValue() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };

 leafNode.coreValue(null);
 leafNode.coreValue("testValue");

 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testAttrGet() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");
 leafNode.attr("testAttr", "testValue");

 assertEquals("testValue", leafNode.attr("testAttr"));
 }

 @Test
 public void testAttrSet() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 leafNode.attr("testAttr", "newTestValue");
 assertEquals("newTestValue", leafNode.attr("testAttr"));
 }

 @Test
 public void testAttrDoesNotExist() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 assertNull(leafNode.attr("nonExistentAttr"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");
 leafNode.attr("testAttr", "testValue");

 assertTrue(leafNode.hasAttr("testAttr"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");
 leafNode.attr("testAttr", "testValue");

 leafNode.removeAttr("testAttr");
 assertFalse(leafNode.hasAttr("testAttr"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 assertEquals("", leafNode.absUrl("testAttr"));
 }

 @Test
 public void testBaseUri() {
 LeafNode parentNode = new TextNode("Parent Node");
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 parentNode.appendChild(leafNode);
 leafNode.coreValue("testValue");

 assertEquals("", leafNode.baseUri());
 assertEquals("Parent Node", parentNode.baseUri());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetBaseUri() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 leafNode.setBaseUri("testBaseUri");
}

 @Test
 public void testEnsureAttributes() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 leafNode.coreValue("testValue");

 leafNode.ensureAttributes();
 assertTrue(leafNode.hasAttributes());
 }
}