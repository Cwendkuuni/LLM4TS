package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

 // Test cases from all experts
 // ...

 // Test case for getCurrentNodePointer method with null currentNodePointer
 @Test(expected = NullPointerException.class)
 public void testGetCurrentNodePointerWithNullCurrentNodePointer() {
 AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("test"));
 attributeContext.nextNode();
 attributeContext.reset();
 assertNull(attributeContext.getCurrentNodePointer());
 }

 // Test case for setStarted method with already started flag
 @Test
 public void testSetStartedWithAlreadyStartedFlag() {
 AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("test"));
 attributeContext.nextNode();
 attributeContext.setStarted(true);
 assertTrue(attributeContext.isStarted());
}

 // Test case for reset method with already started flag
 @Test
 public void testResetWithAlreadyStartedFlag() {
 AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("test"));
 attributeContext.nextNode();
 attributeContext.setStarted(true);
 attributeContext.reset();
 assertFalse(attributeContext.isStarted());
}

 // Test case for iterator getNodeIterator method with null name
 @Test(expected = NullPointerException.class)
 public void testIteratorGetNodeIteratorWithNullName() {
 AttributeContext attributeContext = new AttributeContext(null, null);
 NodeIterator expectedNodeIterator = new NodeIterator() {};
 NodeIterator actualNodeIterator = attributeContext.parentContext.getCurrentNodePointer().attributeIterator(null);
 assertEquals(expectedNodeIterator, actualNodeIterator);
}

 // Test case for iterator getNodeIterator method with empty name
 @Test(expected = IllegalArgumentException.class)
 public void testIteratorGetNodeIteratorWithEmptyName() {
 AttributeContext attributeContext = new AttributeContext(null, null);
 NodeIterator expectedNodeIterator = new NodeIterator() {};
 NodeIterator actualNodeIterator = attributeContext.parentContext.getCurrentNodePointer().attributeIterator("");
 assertNotEquals(expectedNodeIterator, actualNodeIterator);
}

 // Test case for iterator getNodeIterator method with valid name
 @Test
 public void testIteratorGetNodeIteratorWithValidName() {
 AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("test"));
 NodeTest nodeTest = new NodeNameTest("testAttribute");
 NodeIterator expectedNodeIterator = new NodeIterator() {};
 NodeIterator actualNodeIterator = attributeContext.parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName());
 assertNotNull(actualNodeIterator);
}

}