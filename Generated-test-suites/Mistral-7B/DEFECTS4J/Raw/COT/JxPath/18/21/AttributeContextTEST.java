package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AttributeContext
 */
public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;

 @Before
 public void setUp() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest(new QName("test"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 // Test empty constructor
 AttributeContext emptyAttributeContext = new AttributeContext(parentContext, null);
 Assert.assertNull(emptyAttributeContext.getNodeTest());

 // Test constructor with NodeTest
 AttributeContext attributeContextWithNodeTest = new AttributeContext(parentContext, nodeTest);
 Assert.assertEquals(nodeTest, attributeContextWithNodeTest.getNodeTest());
 }

 /**
 * Test getCurrentNodePointer method
 */
 @Test
 public void testGetCurrentNodePointer() {
 NodePointer expectedNodePointer = new NodePointer(new Object(), 0, 0);
 attributeContext.currentNodePointer = expectedNodePointer;
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertEquals(expectedNodePointer, actualNodePointer);
 }

 /**
 * Test reset method
 */
 @Test
 public void testReset() {
 attributeContext.reset();
 Assert.assertFalse(attributeContext.isSetStarted());
 Assert.assertNull(attributeContext.iterator);
 }

 /**
 * Test setPosition method with valid position
 */
 @Test
 public void testSetPositionWithValidPosition() {
 int validPosition = 1;
 attributeContext.setPosition(validPosition);
 Assert.assertTrue(attributeContext.isSetStarted());
 Assert.assertNotNull(attributeContext.iterator);
 }

 /**
 * Test setPosition method with invalid position
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithInvalidPosition() {
 int invalidPosition = -1;
 attributeContext.setPosition(invalidPosition);
 }

 /**
 * Test nextNode method with empty iterator
 */
 @Test
 public void testNextNodeWithEmptyIterator() {
 attributeContext.iterator = new NodeIterator() {
 @Override
 public NodeIterator getNodeIterator() {
 return this;
 }

 @Override
 public boolean setPosition(int position) {
 return false;
 }

 @Override
 public NodePointer getNodePointer() {
 return null;
 }

 @Override
 public int getPosition() {
 return 0;
 }

 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public NodePointer nextNode() {
 return null;
 }
 };
 attributeContext.nextNode();
 }

 /**
 * Test nextNode method with non-empty iterator
 */
 @Test
 public void testNextNodeWithNonEmptyIterator() {
 NodePointer expectedNodePointer = new NodePointer(new Object(), 0, 0);
 NodePointer actualNodePointer;
 NodeIterator nodeIterator = new NodeIterator() {
 @Override
 public NodeIterator getNodeIterator() {
 return this;
 }

 @Override
 public boolean setPosition(int position) {
 return position == 0 ? true : false;
 }

 @Override
 public NodePointer getNodePointer() {
 return expectedNodePointer;
 }

 @Override
 public int getPosition() {
 return 0;
 }

 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public NodePointer nextNode() {
 actualNodePointer = getNodePointer();
 return nextNode();
 }
 };
 attributeContext.iterator = nodeIterator;
 actualNodePointer = attributeContext.nextNode().getNodePointer();
 Assert.assertEquals(expectedNodePointer, actualNodePointer);
 }

 /**
 * Test nextNode method with end of iterator
 */
 @Test
 public void testNextNodeWithEndOfIterator() {
 NodePointer expectedNullNodePointer = null;
 NodeIterator nodeIterator = new NodeIterator() {
 @Override
 public NodeIterator getNodeIterator() {
 return this;
 }

 @Override
 public boolean setPosition(int position) {
 return true;
 }

 @Override
 public NodePointer getNodePointer() {
 return expectedNullNodePointer;
 }

 @Override
 public int getPosition() {
 return 0;
 }

 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public NodePointer nextNode() {
 return null;
 }
 };
 attributeContext.iterator = nodeIterator;
 assertNull(attributeContext.nextNode().getNodePointer());
 }
}