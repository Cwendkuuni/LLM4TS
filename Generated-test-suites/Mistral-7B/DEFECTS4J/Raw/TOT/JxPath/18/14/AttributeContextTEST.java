package org.apache.commons.jxpath.ri.axes;

 import org.apache.commons.jxpath.ri.EvalContext;
 import org.apache.commons.jxpath.ri.NodeIterator;
 import org.apache.commons.jxpath.ri.NodeTest;
 import org.apache.commons.jxpath.ri.QName;
 import org.apache.commons.jxpath.ri.model.NodeIterator;
 import org.apache.commons.jxpath.ri.model.NodePointer;
 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.w3c.dom.Node;
 import static org.junit.Assert.*;

 /**
 * Tests for {@link AttributeContext} class.
 */
 public class AttributeContextTest {

 private static final String XML = "<root xmlns:x='http://test.com'><element x:attr1='value1'><subElement x:attr2='value2' /></element></root>";
 private Document document;
 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;

 @Before
 public void setUp() throws Exception {
 document = JaxpFactory.newDocumentBuilder().parse(new ByteArrayInputStream(XML.getBytes()));
 parentContext = new EvalContext(new EvalContext(document.getDocumentElement()), null);
 nodeTest = new NodeNameTest(new QName("{http://test.com}", "attr1"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @After
 public void tearDown() {
 attributeContext.reset();
 }

 /**
 * Test nextNode() method with valid node test and existing attribute.
 */
 @Test
 public void testNextNodeWithValidNodeTestAndExistingAttribute() {
 attributeContext.nextNode();
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals("subElement", currentNodePointer.getNodeName());
 assertTrue(currentNodePointer.isElementNode());
 assertEquals("value2", currentNodePointer.getAttributes().getNamedItem("attr2").getNodeValue());
 }

 /**
 * Test nextNode() method with valid node test and non-existing attribute.
 */
 @Test
 public void testNextNodeWithValidNodeTestAndNonExistingAttribute() {
 attributeContext.reset();
 attributeContext.nextNode();
 assertNull(attributeContext.getCurrentNodePointer());
 }

 /**
 * Test nextNode() method with invalid node test.
 */
 @Test(expected = IllegalStateException.class)
 public void testNextNodeWithInvalidNodeTest() {
 nodeTest = new NodeTest(NodeTest.ANY_NODE);
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.nextNode();
 }

 /**
 * Test setPosition() method with valid position.
 */
 @Test
 public void testSetPositionWithValidPosition() {
 attributeContext.setPosition(1);
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals("element", currentNodePointer.getNodeName());
 assertTrue(currentNodePointer.isElementNode());
 assertNull(currentNodePointer.getAttributes());
 }

 /**
 * Test setPosition() method with invalid position.
 */
 @Test
 public void testSetPositionWithInvalidPosition() {
 attributeContext.setPosition(-1);
 assertNull(attributeContext.getCurrentNodePointer());
 }

 /**
 * Test setPosition() method with position greater than total number of nodes.
 */
 @Test
 public void testSetPositionWithPositionGreaterThanTotalNumberOfNodes() {
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.setPosition(3);
 assertNull(attributeContext.getCurrentNodePointer());
 }
 }