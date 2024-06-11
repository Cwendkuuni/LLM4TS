
package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NullPropertyPointerTest {
	
	@Test
	public void testGetName() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertEquals(new QName("*"), pointer.getName());
	}
	
	@Test
	public void testSetPropertyIndex() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		pointer.setPropertyIndex(1);
		// No validation on setPropertyIndex() method
	}
	
	@Test
	public void testGetLength() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertEquals(0, pointer.getLength());
	}
	
	@Test
	public void testGetBaseValue() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertNull(pointer.getBaseValue());
	}
	
	@Test
	public void testGetImmediateNode() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertNull(pointer.getImmediateNode());
	}
	
	@Test
	public void testIsLeaf() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertTrue(pointer.isLeaf());
	}
	
	@Test
	public void testGetValuePointer() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		NodePointer valuePointer = pointer.getValuePointer();
		assertTrue(valuePointer instanceof NullPointer);
	}
	
	@Test
	public void testIsActualProperty() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertFalse(pointer.isActualProperty());
	}
	
	@Test
	public void testIsActual() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertFalse(pointer.isActual());
	}
	
	@Test
	public void testIsContainer() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertTrue(pointer.isContainer());
	}
	
	@Test
	public void testSetValue() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		// Case 1: parent is null
		try {
			pointer.setValue("test value");
			fail("Expected JXPathInvalidAccessException");
		} catch (JXPathInvalidAccessException e) {
			// expected exception
		}
		
		// Case 2: parent is PropertyOwnerPointer and supports dynamic property declaration
		PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
		PropertyPointer propertyPointer = new PropertyPointer(null);
		propertyOwnerPointer.setPropertyPointer(propertyPointer);
		pointer = new NullPropertyPointer(propertyOwnerPointer);
		propertyOwnerPointer.setDynamicPropertyDeclarationSupported(true);
		try {
			pointer.setValue("test value");
			assertEquals("test value", propertyPointer.getValue());
		} catch (JXPathInvalidAccessException e) {
			fail("Unexpected JXPathInvalidAccessException");
		}
		
		// Case 3: parent is PropertyOwnerPointer but does not support dynamic property declaration
		propertyOwnerPointer.setDynamicPropertyDeclarationSupported(false);
		try {
			pointer.setValue("test value");
			fail("Expected JXPathInvalidAccessException");
		} catch (JXPathInvalidAccessException e) {
			// expected exception
		}
	}
	
	@Test
	public void testCreatePath() {
		JXPathContext context = new JXPathContext(new NullPointer(null, null));
		
		NodePointer parent = new NullPointer(context, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		
		// Case 1: isAttribute() returns true
		pointer.setAttribute(true);
		NodePointer newPath = pointer.createPath(context);
		assertTrue(newPath instanceof NullPointer);
		
		// Case 2: isAttribute() returns false
		pointer.setAttribute(false);
		
		// Case 2.1: parent is PropertyOwnerPointer
		PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
		propertyOwnerPointer.setPropertyPointer(new PropertyPointer(null));
		pointer = new NullPropertyPointer(propertyOwnerPointer);
		newPath = pointer.createPath(context);
		assertTrue(newPath instanceof PropertyPointer);
		
		// Case 2.2: parent is not PropertyOwnerPointer
		parent = new NullPointer(context, null);
		pointer = new NullPropertyPointer(parent);
		newPath = pointer.createPath(context);
		assertTrue(newPath instanceof NullPointer);
	}
	
	@Test
	public void testCreatePathWithValue() {
		JXPathContext context = new JXPathContext(new NullPointer(null, null));
		
		NodePointer parent = new NullPointer(context, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		
		// Case 1: isAttribute() returns true
		pointer.setAttribute(true);
		NodePointer newPath = pointer.createPath(context, "test value");
		assertTrue(newPath instanceof NullPointer);
		
		// Case 2: isAttribute() returns false
		pointer.setAttribute(false);
		
		// Case 2.1: parent is PropertyOwnerPointer
		PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
		propertyOwnerPointer.setPropertyPointer(new PropertyPointer(null));
		pointer = new NullPropertyPointer(propertyOwnerPointer);
		newPath = pointer.createPath(context, "test value");
		assertTrue(newPath instanceof PropertyPointer);
		
		// Case 2.2: parent is not PropertyOwnerPointer
		parent = new NullPointer(context, null);
		pointer = new NullPropertyPointer(parent);
		newPath = pointer.createPath(context, "test value");
		assertTrue(newPath instanceof NullPointer);
	}
	
	@Test
	public void testCreateChildWithQNameAndIndex() {
		JXPathContext context = new JXPathContext(new NullPointer(null, null));
		
		NodePointer parent = new NullPointer(context, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		
		// Case 1: createPath() does not return NullPointer
		parent = new PropertyOwnerPointer(null);
		parent.setPropertyPointer(new PropertyPointer(null));
		pointer = new NullPropertyPointer(parent);
		NodePointer newPath = pointer.createChild(context, new QName("test"), 0);
		assertTrue(newPath instanceof PropertyPointer);
		
		// Case 2: createPath() returns NullPointer
		parent = new NullPointer(context, null);
		pointer = new NullPropertyPointer(parent);
		newPath = pointer.createChild(context, new QName("test"), 0);
		assertTrue(newPath instanceof NullPointer);
	}
	
	@Test
	public void testCreateChildWithQNameIndexAndValue() {
		JXPathContext context = new JXPathContext(new NullPointer(null, null));
		
		NodePointer parent = new NullPointer(context, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		
		// Case 1: createPath() does not return NullPointer
		parent = new PropertyOwnerPointer(null);
		parent.setPropertyPointer(new PropertyPointer(null));
		pointer = new NullPropertyPointer(parent);
		NodePointer newPath = pointer.createChild(context, new QName("test"), 0, "test value");
		assertTrue(newPath instanceof PropertyPointer);
		
		// Case 2: createPath() returns NullPointer
		parent = new NullPointer(context, null);
		pointer = new NullPropertyPointer(parent);
		newPath = pointer.createChild(context, new QName("test"), 0, "test value");
		assertTrue(newPath instanceof NullPointer);
	}
	
	@Test
	public void testGetPropertyName() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertEquals("*", pointer.getPropertyName());
	}
	
	@Test
	public void testSetPropertyName() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		pointer.setPropertyName("test");
		assertEquals("test", pointer.getPropertyName());
	}
	
	@Test
	public void testSetNameAttributeValue() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		pointer.setNameAttributeValue("test attribute value");
		assertEquals("test attribute value", pointer.getPropertyName());
	}
	
	@Test
	public void testIsCollection() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertFalse(pointer.isCollection());
	}
	
	@Test
	public void testGetPropertyCount() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertEquals(0, pointer.getPropertyCount());
	}
	
	@Test
	public void testGetPropertyNames() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertArrayEquals(new String[0], pointer.getPropertyNames());
	}
	
	@Test
	public void testAsPath() {
		NodePointer parent = new NullPointer(null, null);
		NullPropertyPointer pointer = new NullPropertyPointer(parent);
		assertEquals("*", pointer.asPath());
		
		parent = new NullPointer(null, new QName("test"));
		pointer = new NullPropertyPointer(parent);
		assertEquals("test[@name='*']", pointer.asPath());
	}
	
	// Add more tests if necessary
}
