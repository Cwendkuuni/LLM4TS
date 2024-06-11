
package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    
    private PropertyPointer propertyPointer;
    
    @Before
    public void setUp() {
        propertyPointer = new PropertyPointerMock();
    }
    
    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testSetPropertyIndexAgain() {
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        assertEquals(2, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testGetBean() {
        assertNull(propertyPointer.getBean());
    }
    
    @Test
    public void testGetName() {
        assertEquals(new QName(null, "propertyName"), propertyPointer.getName());
    }
    
    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNull(propertyPointer.getImmediateNode());
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
        NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
        assertNotNull(valuePointer);
        assertEquals(propertyPointer, valuePointer.getParent());
        assertEquals(new QName(null, "propertyName"), valuePointer.getName());
        assertNull(valuePointer.getImmediateNode());
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = JXPathContext.newContext(new Object());
        propertyPointer.createPath(context);
        assertNotNull(propertyPointer.getImmediateNode());
    }
    
    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathFailure() {
        AbstractFactory factory = new AbstractFactory() {
            @Override
            public boolean createObject(
                    JXPathContext context, 
                    NodePointer pointer, 
                    Object parent, 
                    String property, 
                    int index) {
                return false;
            }
        };
        JXPathContext context = JXPathContext.newContext(new Object());
        context.setFactory(factory);
        propertyPointer.createPath(context);
    }
    
    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = JXPathContext.newContext(new Object());
        propertyPointer.createPath(context, "value");
        assertNotNull(propertyPointer.getImmediateNode());
        assertEquals("value", propertyPointer.getImmediateNode());
    }
    
    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = JXPathContext.newContext(new Object());
        QName name = new QName(null, "childName");
        NodePointer childPointer = propertyPointer.createChild(context, name, 1, "value");
        assertNotNull(childPointer);
        assertTrue(childPointer instanceof PropertyPointer);
        assertEquals(propertyPointer, childPointer.getParent());
        assertEquals(name, childPointer.getName());
        assertEquals(1, childPointer.getIndex());
        assertNotNull(childPointer.getImmediateNode());
        assertEquals("value", childPointer.getImmediateNode());
    }
    
    @Test
    public void testCreateChildWithoutValue() {
        JXPathContext context = JXPathContext.newContext(new Object());
        QName name = new QName(null, "childName");
        NodePointer childPointer = propertyPointer.createChild(context, name, 1);
        assertNotNull(childPointer);
        assertTrue(childPointer instanceof PropertyPointer);
        assertEquals(propertyPointer, childPointer.getParent());
        assertEquals(name, childPointer.getName());
        assertEquals(1, childPointer.getIndex());
        assertNull(childPointer.getImmediateNode());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(
                propertyPointer.getImmediateParentPointer().hashCode() + 
                propertyPointer.getPropertyIndex() + 
                propertyPointer.getIndex(), 
                propertyPointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        PropertyPointer pointer1 = new PropertyPointerMock();
        PropertyPointer pointer2 = new PropertyPointerMock();
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(propertyPointer.equals(new Object()));
    }
    
    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new PropertyPointerMock();
        NodePointer pointer2 = new PropertyPointerMock();
        assertEquals(
                propertyPointer.getImmediateValuePointer().compareChildNodePointers(pointer1, pointer2), 
                pointer1.compareChildNodePointers(pointer1, pointer2));
    }
    
    private class PropertyPointerMock extends PropertyPointer {

        public PropertyPointerMock() {
            super(null);
        }

        @Override
        public String getPropertyName() {
            return "propertyName";
        }

        @Override
        public void setPropertyName(String propertyName) {
            // do nothing
        }

        @Override
        public int getPropertyCount() {
            return 0;
        }

        @Override
        public String[] getPropertyNames() {
            return new String[0];
        }

        @Override
        protected boolean isActualProperty() {
            return true;
        }
        
    }
}
