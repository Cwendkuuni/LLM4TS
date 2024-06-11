import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

public class LeafNodeTest {

 private LeafNode leafNode;

 @Before
 public void setUp() {
 leafNode = new LeafNode() {
 // override nodeName for testing purposes
 @Override
 public String nodeName() {
 return "testNode";
 }
 };
 }

 // Test for hasAttributes() method
 @Test
 public void testHasAttributes() {
 // Test case for when value is a string
 leafNode.coreValue("testValue");
 assertFalse(leafNode.hasAttributes());

 // Test case for when value is an Attributes object
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;
 assertTrue(leafNode.hasAttributes());
 }

 // Test for attributes() method
 @Test
 public void testAttributes() {
 // Test case for when value is a string
 assertNull(leafNode.attributes());

 // Test case for when value is an Attributes object
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.value = attributes;
 Attributes returnedAttributes = leafNode.attributes();
 assertEquals(attributes.size(), returnedAttributes.size());
 assertEquals(attributes.get("testKey1"), returnedAttributes.get("testKey1"));
 assertEquals(attributes.get("testKey2"), returnedAttributes.get("testKey2"));
 }

 // Test for ensureAttributes() method
 @Test
 public void testEnsureAttributes() {
 // Test case for when value is a string
 leafNode.coreValue("testValue");
 assertNull(leafNode.value);
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.ensureAttributes();
 assertEquals(attributes.size(), 2);
 assertEquals(attributes.get("testKey1"), leafNode.value.get("testKey1"));
 assertEquals(attributes.get("testKey2"), leafNode.value.get("testKey2"));

 // Test case for when value is an Attributes object
 Attributes attributes1 = new Attributes();
 attributes1.put("testKey1", "testValue1");
 leafNode.value = attributes1;
 leafNode.ensureAttributes();
 assertSame(attributes1, leafNode.value);
 }

 // Test for coreValue() method
 @Test
 public void testCoreValue() {
 leafNode.coreValue("testValue1");
 assertEquals("testValue1", leafNode.coreValue());
 }

 // Test for coreValue(String) method
 @Test
 public void testCoreValue_String() {
 leafNode.coreValue("testValue1");
 leafNode.coreValue("testValue2");
 assertEquals("testValue2", leafNode.coreValue());
 }

 // Test for attr(String) method with string value
 @Test
 public void testAttr_String() {
 leafNode.coreValue("testValue");
 assertEquals("testValue", leafNode.attr("testNode"));
 }

 // Test for attr(String, String) method with string value for key
 @Test
 public void testAttr_String_String_String() {
 leafNode.coreValue("testValue");
 leafNode.attr("testNode", "testValue2");
 assertEquals("testValue2", leafNode.attr("testNode"));
 }

 // Test for attr(String) method with Attributes object value
 @Test
 public void testAttr_String_Attributes() {
 Attributes attributes = new Attributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 leafNode.value = attributes;
 assertEquals("testValue1", leafNode.attr("testKey1"));
 assertEquals("testValue2", leafNode.attr("testKey2"));
 }

 // Test for attr(String, String) method with string value for key
 @Test
 public void testAttr_String_String() {
 leafNode.value = new Attributes();
 assertEquals(EmptyString, leafNode.attr("testNode"));
 leafNode.attr("testNode", "testValue");
 assertEquals("testValue", leafNode.attr("testNode"));
 }

 // Test for attr(String, String) method with Attributes object value for key
 @Test
 public void testAttr_String_Attributes() {
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;
 leafNode.attr("testKey", "testValue2");
 assertEquals("testValue2", leafNode.value.get("testKey"));
 }

 // Test for hasAttr(String) method
 @Test
 public void testHasAttr() {
 leafNode.coreValue("testValue");
 assertFalse(leafNode.hasAttr("testKey"));

 // Test case for when value is an Attributes object
 Attributes attributes = new Attributes();
 attributes.put("testKey", "testValue");
 leafNode.value = attributes;
 assertTrue(leafNode.hasAttr("testKey"));
 }

 // Test for removeAttr(String) method
 @Test
 public void testRemoveAttr() {
 leafNode.coreValue("testValue");
 leafNode.attr("testNode", "testValue");
 leafNode.removeAttr("testNode");
 assertFalse(leafNode.hasAttr("testNode"));
 }

 // Test for absUrl(String) method
 @Test
 public void testAbsUrl() {
 throw new UnsupportedOperationException("Not yet implemented");
 }

 // Test for baseUri() method
 @Test
 public void testBaseUri() {
 leafNode.ensureParent(new ElementNode());
 assertEquals("", leafNode.baseUri());
 }

 // Test for childNodeSize() method
 @Test
 public void testChildNodeSize() {
 assertEquals(0, leafNode.childNodeSize());
 }

 // Test for ensureChildNodes() method
 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 leafNode.ensureChildNodes();
 }
}