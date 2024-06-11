import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setUp() {
 leafNode = new Element("testElement");
 }

 @Test
 public void testHasAttributes() {
 LeafNode leafNodeWithAttributes = new Element("testElement").attr("testAttribute", "testValue");
 assertTrue(leafNodeWithAttributes.hasAttributes());
 LeafNode leafNodeWithoutAttributes = new TextNode("testText");
 assertFalse(leafNodeWithoutAttributes.hasAttributes());
 assertEquals(leafNode.hasAttributes(), false);
 }

 @Test
 public void testAttributes() {
 leafNode.coreValue("testValue");
 Attributes attributes = leafNode.attributes();
 assertEquals(attributes.size(), 1);
 assertEquals(attributes.get("name"), "testValue");
 }

 @Test
 public void testCoreValue() {
 leafNode.coreValue("testValue");
 assertEquals(leafNode.coreValue(), "testValue");
 }

 @Test
 public void testCoreValueSetter() {
 leafNode.coreValue("newTestValue");
 assertEquals(leafNode.coreValue(), "newTestValue");
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullKey() {
 leafNode.attr(null, "testValue");
 }

 @Test(expected = NullPointerException.class)
 public void testAttrNullValue() {
 leafNode.attr("testKey", null);
 }

 @Test
 public void testAttrSetter() {
 leafNode.attr("testKey", "testValue");
 Attributes attributes = leafNode.attributes();
 assertEquals(attributes.size(), 1);
 assertEquals(attributes.get("testKey"), "testValue");
 }

 @Test
 public void testAttrGetter() {
 leafNode.attr("testKey", "testValue");
 assertEquals(leafNode.attr("testKey"), "testValue");
 }

 @Test
 public void testHasAttr() {
 leafNode.attr("testKey", "testValue");
 assertTrue(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testRemoveAttr() {
 leafNode.attr("testKey", "testValue");
 Node removedNode = leafNode.removeAttr("testKey");
 assertNotNull(removedNode);
 assertFalse(leafNode.hasAttr("testKey"));
 }

 @Test
 public void testAbsUrl() {
 leafNode.absUrl("testKey", "http://test.com");
 assertEquals(leafNode.absUrl("testKey"), "http://test.com");
 }

 @Test
 public void testBaseUri() {
 Element parentElement = new Element("testParent");
 parentElement.add(leafNode);
 parentElement.absUrl("testKey", "http://test.com");
 assertEquals(leafNode.baseUri(), "http://test.com");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 leafNode.childNodeSize();
 }
}