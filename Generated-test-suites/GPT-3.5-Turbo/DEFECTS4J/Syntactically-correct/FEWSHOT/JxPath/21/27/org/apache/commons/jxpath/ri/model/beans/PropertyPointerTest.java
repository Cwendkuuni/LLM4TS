package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.pointer.MockNodePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
	
	private PropertyPointer propertyPointer;
	private NodePointer parentPointer;
	
	@Before
	public void setUp() {
		parentPointer = new MockNodePointer();
		propertyPointer = new PropertyPointer(parentPointer) {
			@Override
			public String getPropertyName() {
				return "property";
			}

			@Override
			public void setPropertyName(String propertyName) {
				// Do nothing
			}

			@Override
			public int getPropertyCount() {
				return 1;
			}

			@Override
			public String[] getPropertyNames() {
				return new String[] { "property" };
			}

			@Override
			protected boolean isActualProperty() {
				return true;
			}
		};
	}
	
	@Test
	public void testGetPropertyIndex() {
		assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
	}
	
	@Test
	public void testSetPropertyIndex() {
		propertyPointer.setPropertyIndex(0);
		assertEquals(0, propertyPointer.getPropertyIndex());
	}
	
	@Test
	public void testGetBean() {
		propertyPointer.setParent(null);
		assertEquals(null, propertyPointer.getBean());
		
		propertyPointer.setParent(new MockNodePointer("bean"));
		assertEquals("bean", propertyPointer.getBean());
	}
	
	@Test
	public void testGetName() {
		assertEquals("property", propertyPointer.getName().toString());
	}
	
	@Test
	public void testIsActual() {
		assertTrue(propertyPointer.isActual());
		
		propertyPointer.setPropertyIndex(0);
		assertFalse(propertyPointer.isActual());
	}
	
	@Test
	public void testGetImmediateNode() {
		assertEquals(0, propertyPointer.getImmediateNode());
		assertSame(propertyPointer.getImmediateNode(), propertyPointer.getImmediateNode());
	}
	
	@Test
	public void testIsCollection() {
		assertFalse(propertyPointer.isCollection());
	}
	
	@Test
	public void testIsLeaf() {
		assertTrue(propertyPointer.isLeaf());
	}
	
	@Test
	public void testGetLength() {
		assertEquals(-1, propertyPointer.getLength());
	}
	
	@Test
	public void testGetImmediateValuePointer() {
		propertyPointer.setParent(new MockNodePointer("value"));
		
		NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
		assertTrue(valuePointer instanceof PropertyPointer);
		assertSame(propertyPointer.getParent(), valuePointer.getParent());
		assertEquals("value", valuePointer.getName().toString());
	}
	
	@Test
	public void testHashCode() {
		assertEquals(parentPointer.hashCode() + PropertyPointer.UNSPECIFIED_PROPERTY + NodePointer.WHOLE_COLLECTION, propertyPointer.hashCode());
		
		propertyPointer.setParent(new MockNodePointer("new parent"));
		assertEquals(parentPointer.hashCode() + PropertyPointer.UNSPECIFIED_PROPERTY + NodePointer.WHOLE_COLLECTION, propertyPointer.hashCode());
		
		propertyPointer.setPropertyIndex(0);
		assertEquals(parentPointer.hashCode() + 0 + NodePointer.WHOLE_COLLECTION, propertyPointer.hashCode());
		
		parentPointer.setIndex(2);
		assertEquals(parentPointer.hashCode() + 0 + 2, propertyPointer.hashCode());
	}
	
	@Test
	public void testEquals() {
		assertTrue(propertyPointer.equals(propertyPointer));
		
		assertFalse(propertyPointer.equals(null));
		assertFalse(propertyPointer.equals(new Object()));
		assertFalse(propertyPointer.equals(new MockNodePointer("bean")));
		
		PropertyPointer pointer = new PropertyPointer(parentPointer) {
			@Override
			public String getPropertyName() {
				return "property";
			}

			@Override
			public void setPropertyName(String propertyName) {
				// Do nothing
			}

			@Override
			public int getPropertyCount() {
				return 1;
			}

			@Override
			public String[] getPropertyNames() {
				return new String[] { "property" };
			}

			@Override
			protected boolean isActualProperty() {
				return true;
			}
		};
		assertTrue(propertyPointer.equals(pointer));
		
		pointer.setPropertyIndex(0);
		assertFalse(propertyPointer.equals(pointer));
		
		propertyPointer.setPropertyIndex(0);
		assertTrue(propertyPointer.equals(pointer));
		
		pointer.setParent(new MockNodePointer("new parent"));
		assertFalse(propertyPointer.equals(pointer));
		
		propertyPointer.setParent(new MockNodePointer("new parent"));
		assertTrue(propertyPointer.equals(pointer));
		
		pointer.setIndex(2);
		assertFalse(propertyPointer.equals(pointer));
		
		propertyPointer.setIndex(2);
		assertTrue(propertyPointer.equals(pointer));
	}
	
	@Test
	public void testCompareChildNodePointers() {
		propertyPointer.setParent(new MockNodePointer("value"));
		NodePointer pointer1 = new MockNodePointer();
		NodePointer pointer2 = new MockNodePointer();
		
		assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer1));
		assertEquals(0, propertyPointer.compareChildNodePointers(pointer2, pointer2));
	}
	
	@Test
	public void testCreateChild() {
		JXPathContext context = new JXPathContext(null);
		QName name = new QName(null, "name");
		int index = 0;
		
		NodePointer childPointer1 = propertyPointer.createChild(context, name, index, "value");
		NodePointer childPointer2 = propertyPointer.createChild(context, name, index);
		
		assertTrue(childPointer1 instanceof PropertyPointer);
		assertTrue(childPointer2 instanceof PropertyPointer);
		assertSame(propertyPointer.getParent(), childPointer1.getParent());
		assertSame(propertyPointer.getParent(), childPointer2.getParent());
		assertEquals(name.toString(), childPointer1.getName().toString());
		assertEquals(name.toString(), childPointer2.getName().toString());
		assertEquals("value", childPointer1.getImmediateNode());
		assertEquals("value", childPointer2.getImmediateNode());
	}
	
}