import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

 @Test
 public void testHasAttributes() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue");

 assertTrue(leafNode.hasAttributes());
 leafNode.value = "testValue";
 assertFalse(leafNode.hasAttributes());
 }

 @Test
 public void testAttributes() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue", new Attributes().put("testAttr", "testValue"));

 Attributes attributes = leafNode.attributes();
 assertEquals("testValue", attributes.get("test"));
 assertEquals("testValue", attributes.get("testAttr"));
 }

 @Test
 public void testCoreValue() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue");

 assertEquals("testValue", leafNode.coreValue());
 }

 @Test
 public void testCoreValueSetter() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue");
 leafNode.coreValue("newTestValue");

 assertEquals("newTestValue", leafNode.coreValue());
 }

 @Test(expected = AssertionError.class)
 public void testGetAttributeWhenNoAttributes() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue");

 assertEquals("testValue", leafNode.attr("test"));
 // This assertion will fail as there is no attribute set
 assertEquals("testValue", leafNode.attr("testAttr"));
 }

 @Test
 public void testSetAttribute() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue");

 leafNode.attr("test", "testValue");
 leafNode.attr("testAttr", "testValue");

 assertEquals("testValue", leafNode.attr("test"));
 assertEquals("testValue", leafNode.attr("testAttr"));
 }

 @Test
 public void testHasAttr() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue", new Attributes().put("testAttr", "testValue"));

 assertTrue(leafNode.hasAttr("test"));
 assertTrue(leafNode.hasAttr("testAttr"));
 }

 @Test
 public void testRemoveAttr() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue", new Attributes().put("testAttr", "testValue"));

 leafNode.removeAttr("testAttr");
 assertFalse(leafNode.hasAttr("testAttr"));
 }

 @Test
 public void testAbsUrl() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue", new Attributes().put("testAttr", "testValue").put("abs:url", "http://test.com"));

 assertEquals("http://test.com", leafNode.absUrl("testAttr"));
 }

 @Test
 public void testBaseUri() {
 LeafNode leafNode1 = new LeafNode() {
 @Override
 public String nodeName() {
 return "test1";
 }
 };
 leafNode1.coreValue("testValue1");
 LeafNode parentNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "parent";
 }
 };
 parentNode.coreValue("parentValue", new Attributes().put("base:uri", "http://parent.com"));
 parentNode.appendChild(leafNode1);

 assertEquals("http://parent.com", leafNode1.baseUri());
 LeafNode leafNode2 = new LeafNode() {
 @Override
 public String nodeName() {
 return "test2";
 }
 };
 leafNode2.coreValue("testValue2");
 leafNode1.appendChild(leafNode2);

 assertEquals("", leafNode2.baseUri());
}

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue");

 assertEquals(0, leafNode.childNodeSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 LeafNode leafNode = new LeafNode() {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 leafNode.coreValue("testValue");

 leafNode.ensureChildNodes();
 }
}