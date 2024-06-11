package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;

 @Before
 public void setUp() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest(new QName("test:attribute"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 // Testing getCurrentNodePointer() method
 @Test
 public void testGetCurrentNodePointer() {
 NodePointer expectedNodePointer = new NodePointer(new Object(), 0);
 attributeContext.currentNodePointer = expectedNodePointer;
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 assertNotNull(actualNodePointer);
 assertTrue(actualNodePointer.equals(expectedNodePointer));
 }

 // Testing reset() method
 @Test
 public void testReset() {
 attributeContext.reset();
 assertFalse(attributeContext.isSetStarted());
 assertNull(attributeContext.getIterator());
 }

 // Testing setPosition() method with valid position
 @Test
 public void testSetPositionWithValidPosition() {
 attributeContext.setPosition(0);
 assertTrue(attributeContext.isSetStarted());
 assertNotNull(attributeContext.getIterator());
 }

 // Testing setPosition() method with invalid position
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithInvalidPosition() {
 attributeContext.setPosition(-1);
 }

 // Testing nextNode() method with no start
 @Test
 public void testNextNodeWithNoStart() {
 assertFalse(attributeContext.nextNode());
 }

 // Testing nextNode() method with end
 @Test
 public void testNextNodeWithEnd() {
 iterator = new NodeIterator() {
 @Override
 public boolean setPosition(int position) {
 return false;
 }

 @Override
 public NodePointer getNodePointer() {
 return null;
 }
 };
 attributeContext.iterator = iterator;
 assertFalse(attributeContext.nextNode());
 }

 // Testing nextNode() method with valid position
 @Test
 public void testNextNodeWithValidPosition() {
 NodePointer expectedNodePointer = new NodePointer(new Object(), 0);
 attributeContext.iterator = new NodeIterator() {
 @Override
 public boolean setPosition(int position) {
 return true;
 }

 @Override
 public NodePointer getNodePointer() {
 return expectedNodePointer;
 }
 };
 NodePointer actualNodePointer = attributeContext.nextNode().getNodePointer();
 assertNotNull(actualNodePointer);
 assertTrue(actualNodePointer.equals(expectedNodePointer));
 }

 // Testing nextNode() method with no more nodes
 @Test
 public void testNextNodeWithNoMoreNodes() {
 NodePointer expectedNodePointer = null;
 attributeContext.iterator = new NodeIterator() {
 @Override
 public boolean setPosition(int position) {
 return false;
 }

 @Override
 public NodePointer getNodePointer() {
 return expectedNodePointer;
 }
 };
 assertFalse(attributeContext.nextNode().getNodePointer() == null);
 }

 // Testing nextNode() method with null node test
 @Test(expected = IllegalArgumentException.class)
 public void testNextNodeWithNullNodeTest() {
 attributeContext.nodeTest = null;
 attributeContext.nextNode();
 }
}