import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode node;

 @Before
 public void setUp() {
 node = new LeafNode() {
 };
 }

 // Testing hasAttributes() method
 @Test
 public void testHasAttributes() {
 assertFalse(node.hasAttributes());
 node.coreValue("test");
 assertTrue(node.hasAttributes());
 }

 // Testing attributes() method
 @Test
 public void testAttributes() {
 Attributes attributes = node.attributes();
 assertNull(attributes);
 node.coreValue("test", new Attributes());
 Attributes attributes1 = node.attributes();
 assertEquals("test", attributes1.get("name"));
 }

 // Testing ensureAttributes() method
 @Test(expected = IllegalStateException.class)
 public void testEnsureAttributesNoAttributes() {
 node.ensureAttributes();
 }

 @Test
 public void testEnsureAttributesWithAttributes() {
 node.coreValue("test", new Attributes().put("name", "value"));
 node.ensureAttributes();
 Attributes attributes = (Attributes) node.value;
 assertEquals("value", attributes.get("name"));
 }

 // Testing coreValue() and coreValue(String) methods
 @Test
 public void testCoreValue() {
 node.coreValue("test");
 assertEquals("test", node.coreValue());
 node.coreValue("newTest");
 assertEquals("newTest", node.coreValue());
 }

 // Testing attr(String) method with no attributes
 @Test
 public void testAttrWithoutAttributes() {
 assertNull(node.attr("key"));
 }

 // Testing attr(String) method with a core value
 @Test
 public void testAttrWithCoreValue() {
 node.coreValue("test");
 assertEquals("test", node.attr("nodeName"));
 }

 // Testing attr(String, String) method with no attributes
 @Test
 public void testAttrWithoutAttributesSetValue() {
 node.attr("key", "value");
 assertNull(node.attr("key"));
 }

 // Testing attr(String, String) method with a core value
 @Test
 public void testAttrWithCoreValueSetValue() {
 node.coreValue("test");
 node.attr("key", "value");
 assertEquals("value", node.attr("key"));
 }

 // Testing hasAttr(String) method with no attributes
 @Test
 public void testHasAttrWithoutAttributes() {
 assertFalse(node.hasAttr("key"));
 }

 // Testing hasAttr(String) method with a core value
 @Test
 public void testHasAttrWithCoreValue() {
 node.coreValue("test");
 assertTrue(node.hasAttr("nodeName"));
 }

 // Testing removeAttr(String) method with no attributes
 @Test
 public void testRemoveAttrWithoutAttributes() {
 node.removeAttr("key");
 assertNull(node.attr("key"));
 }

 // Testing removeAttr(String) method with a core value
 @Test
 public void testRemoveAttrWithCoreValue() {
 node.coreValue("test");
 node.removeAttr("nodeName");
 assertNull(node.attr("nodeName"));
 node.coreValue("newTest");
 assertNull(node.attr("nodeName"));
 }

 // Testing absUrl(String) method with no base URI
 @Test
 public void testAbsUrlWithoutBaseUri() {
 assertNull(node.absUrl("key"));
 }

 // Testing absUrl(String) method with a base URI
 @Test
 public void testAbsUrlWithBaseUri() {
 String baseUri = "http://example.com";
 node.parent().coreValue("baseUri", baseUri);
 assertEquals(baseUri + "/test", node.absUrl("key"));
 }

 // Testing baseUri() method
 @Test
 public void testBaseUri() {
 String baseUri = "";
 node.coreValue("baseUri", baseUri);
 assertEquals(baseUri, node.baseUri());
 }

 // Testing childNodeSize() method
 @Test
 public void testChildNodeSize() {
 assertEquals(0, node.childNodeSize());
 }

 // Testing ensureChildNodes() method
 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 }

}