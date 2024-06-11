package org.apache.commons.jxpath.ri.model.jdom;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertNotNull;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertSame;
 import static org.junit.Assert.assertTrue;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;

 import org.apache.commons.jxpath.ri.NodeIterator;
 import org.apache.commons.jxpath.ri.model.NodePointer;
 import org.apache.commons.jxpath.ri.model.NodePointerTest;
 import org.apache.commons.jxpath.ri.test.util.TestUtil;
 import org.jdom2.Attribute;
 import org.jdom2.Element;
 import org.jdom2.Namespace;

 /**
 * JDOMAttributeIteratorTest.java Test file for JDOMAttributeIterator class.
 */
 public class JDOMAttributeIteratorTest {

 private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
 private static final String PREFIX = "xml";
 private static final String LOCAL_NAME = "attr";
 private static final String NAMESPACE_URI = "http://test.com";

 @org.junit.BeforeClass
 public static void setUpBeforeClass() throws Exception {
 TestUtil.loadTestXML("JDOMAttributeIteratorTest.xml");
 }

 @org.junit.AfterClass
 public static void tearDownAfterClass() throws Exception {
 TestUtil.clearTestXML();
 }

 /**
 * Test constructor with a wildcard name.
 */
 @org.junit.Test
 public void testConstructorWithWildcardName() {
 // Create a test node
 Element root = TestUtil.getRootElement();
 Element parent = root.getChild("parent", "ns1");
 parent.addAttribute("attr1", "value1", "ns1");
 parent.addAttribute("attr2", "value2", "ns1");
 parent.addAttribute("attr3", "value3", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test attributes list size
 assertEquals(3, iterator.size());

 // Test getNodePointer() method
 NodeIterator testIterator = iterator;
 NodePointer testNodePointer = null;
 while (testIterator.hasNext()) {
 testNodePointer = testIterator.next();
 }
 assertEquals(3, testIterator.size());
 assertTrue(testNodePointer instanceof JDOMAttributePointer);

 // Test getAttribute() method of retrieved NodePointer
 Attribute attribute1 = ((JDOMAttributePointer) testNodePointer).getAttribute();
 assertEquals("value1", attribute1.getValue());
 assertSame(new QName(PREFIX, "attr1"), attribute1.getQName());

 testIterator = iterator;
 testNodePointer = null;
 while (testIterator.hasNext()) {
 testNodePointer = testIterator.next();
 }
 assertEquals(3, testIterator.size());
 Attribute attribute2 = ((JDOMAttributePointer) testNodePointer).getAttribute();
 assertEquals("value2", attribute2.getValue());
 assertSame(new QName(PREFIX, "attr2"), attribute2.getQName());

 testIterator = iterator;
 testNodePointer = null;
 while (testIterator.hasNext()) {
 testNodePointer = testIterator.next();
 }
 assertEquals(3, testIterator.size());
 Attribute attribute3 = ((JDOMAttributePointer) testNodePointer).getAttribute();
 assertEquals("value3", attribute3.getValue());
 assertSame(new QName(PREFIX, "attr3"), attribute3.getQName());
 }

 /**
 * Test constructor with a specific name.
 */
 @org.junit.Test
 public void testConstructorWithSpecificName() {
 // Create a test node
 Element root = TestUtil.getRootElement();
 Element parent = root.getChild("parent", "ns1");
 parent.addAttribute("attr1", "value1", "ns1");
 parent.addAttribute("attr2", "value2", "ns1");
 parent.addAttribute("attr3", "value3", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, LOCAL_NAME));

 // Test attributes list size
 assertEquals(1, iterator.size());

 // Test getNodePointer() method
 NodeIterator testIterator = iterator;
 NodePointer testNodePointer = null;
 while (testIterator.hasNext()) {
 testNodePointer = testIterator.next();
 }
 assertEquals(1, testIterator.size());
 assertTrue(testNodePointer instanceof JDOMAttributePointer);

 // Test getAttribute() method of retrieved NodePointer
 Attribute attribute = ((JDOMAttributePointer) testNodePointer).getAttribute();
 assertEquals("value1", attribute.getValue());
 assertSame(new QName(PREFIX, LOCAL_NAME), attribute.getQName());
 }

 /**
 * Test constructor with an empty list.
 */
 @org.junit.Test
 public void testConstructorWithEmptyList() {
 // Create a test node
 Element parent = new Element("parent", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test attributes list size
 assertEquals(0, iterator.size());
 }

 /**
 * Test getNodePointer() method with invalid position.
 */
 @org.junit.Test
 public void testGetNodePointerWithInvalidPosition() {
 // Create a test node
 Element root = TestUtil.getRootElement();
 Element parent = root.getChild("parent", "ns1");
 parent.addAttribute("attr1", "value1", "ns1");
 parent.addAttribute("attr2", "value2", "ns1");
 parent.addAttribute("attr3", "value3", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test getNodePointer() method with invalid position
 assertFalse(iterator.hasNext());
 assertNull(iterator.next());
 }

 /**
 * Test getNodePointer() method with valid position.
 */
 @org.junit.Test
 public void testGetNodePointerWithValidPosition() {
 // Create a test node
 Element root = TestUtil.getRootElement();
 Element parent = root.getChild("parent", "ns1");
 parent.addAttribute("attr1", "value1", "ns1");
 parent.addAttribute("attr2", "value2", "ns1");
 parent.addAttribute("attr3", "value3", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test getNodePointer() method with valid position
 assertTrue(iterator.hasNext());
 NodePointer testNodePointer = iterator.next();
 assertEquals(3, iterator.size());
 assertTrue(testNodePointer instanceof JDOMAttributePointer);
 }

 /**
 * Test getPosition() method with invalid position.
 */
 @org.junit.Test
 public void testGetPositionWithInvalidPosition() {
 // Create a test node
 Element parent = new Element("parent", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test getPosition() method with invalid position
 assertEquals(-1, iterator.getPosition());
 }

 /**
 * Test getPosition() method with valid position.
 */
 @org.junit.Test
 public void testGetPositionWithValidPosition() {
 // Create a test node
 Element root = TestUtil.getRootElement();
 Element parent = root.getChild("parent", "ns1");
 parent.addAttribute("attr1", "value1", "ns1");
 parent.addAttribute("attr2", "value2", "ns1");
 parent.addAttribute("attr3", "value3", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test getPosition() method with valid position
 iterator.next();
 iterator.next();
 assertEquals(2, iterator.getPosition());
 }

 /**
 * Test setPosition() method with invalid position.
 */
 @org.junit.Test
 public void testSetPositionWithInvalidPosition() {
 // Create a test node
 Element parent = new Element("parent", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test setPosition() method with invalid position
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.setPosition(5));
 }

 /**
 * Test setPosition() method with valid position.
 */
 @org.junit.Test
 public void testSetPositionWithValidPosition() {
 // Create a test node
 Element root = TestUtil.getRootElement();
 Element parent = root.getChild("parent", "ns1");
 parent.addAttribute("attr1", "value1", "ns1");
 parent.addAttribute("attr2", "value2", "ns1");
 parent.addAttribute("attr3", "value3", "ns1");

 // Test initialization
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(
 new NodePointer(parent), new QName(PREFIX, "*"));

 // Test setPosition() method with valid position
 iterator.next();
 iterator.next();
 assertTrue(iterator.setPosition(2));
 assertEquals(2, iterator.getPosition());
 }
 }

 /**
 * JDOMAttributePointer class test helper.
 */
 private static class JDOMAttributePointer extends NodePointerTest {

 public JDOMAttributePointer(NodePointer parent, Attribute attribute) {
 super(parent, new JDOMAttributeIterator.JDOMAttributePointer(parent, attribute));
 }

 @Override
 protected NodeIterator createNodeIterator(NodePointer nodePointer) {
 return new JDOMAttributeIterator(nodePointer, new QName(PREFIX, "*"));
 }
 }