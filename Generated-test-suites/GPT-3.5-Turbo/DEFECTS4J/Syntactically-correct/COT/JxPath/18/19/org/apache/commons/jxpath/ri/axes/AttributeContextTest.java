import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeContextTest {
	
	@Test
	public void testGetCurrentNodePointer() {
		// Create a parent context and a node test
		EvalContext parentContext = new EvalContext();
		NodeTest nodeTest = new NodeNameTest(new QName("test"));
		
		// Create an AttributeContext object
		AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
		
		// Check if the current node pointer is not null
		assertNotNull(attributeContext.getCurrentNodePointer());
	}
	
	@Test
	public void testReset() {
		// Create a parent context and a node test
		EvalContext parentContext = new EvalContext();
		NodeTest nodeTest = new NodeNameTest(new QName("test"));
		
		// Create an AttributeContext object
		AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
		
		// Reset the attribute context
		attributeContext.reset();
		
		// Check if the setStarted flag is set to false
		assertFalse(attributeContext.setStarted);
		
		// Check if the iterator is null
		assertNull(attributeContext.iterator);
		
		// Check if the parent context is also reset
		assertTrue(parentContext.isReset());
	}
	
	@Test
	public void testSetPosition() {
		// Create a parent context and a node test
		EvalContext parentContext = new EvalContext();
		NodeTest nodeTest = new NodeNameTest(new QName("test"));
		
		// Create an AttributeContext object
		AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
		
		// Set the position to 1
		assertTrue(attributeContext.setPosition(1));
		
		// Check if the current position is 1
		assertEquals(1, attributeContext.getCurrentPosition());
	}
	
	@Test
	public void testNextNode() {
		// Create a parent context and a node test
		EvalContext parentContext = new EvalContext();
		NodeTest nodeTest = new NodeNameTest(new QName("test"));
		
		// Create an AttributeContext object
		AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
		
		// Check if nextNode() returns false when setStarted is false
		assertFalse(attributeContext.nextNode());
		
		// Set setStarted to true and check if nextNode() returns false when the iterator is null
		attributeContext.setStarted = true;
		assertFalse(attributeContext.nextNode());
		
		// Create a node iterator and set it as the iterator in the attribute context
		NodeIterator iterator = new NodeIterator();
		attributeContext.iterator = iterator;
		
		// Check if nextNode() returns true when the iterator position is incremented
		iterator.setPosition(1);
		assertTrue(attributeContext.nextNode());
		
		// Check if the currentNodePointer is set to the node pointer in the iterator
		assertNotNull(attributeContext.currentNodePointer);
		assertEquals(iterator.getNodePointer(), attributeContext.currentNodePointer);
	}
}