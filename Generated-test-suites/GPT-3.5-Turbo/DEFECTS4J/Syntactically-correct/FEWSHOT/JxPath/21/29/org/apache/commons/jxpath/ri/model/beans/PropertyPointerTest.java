package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    
    private PropertyPointer propertyPointer;
    private JXPathContext context;
    private Object baseValue;
    
    @Before
    public void setup() {
        this.baseValue = new Object();
        NodePointer parent = new NodePointer();
        this.propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[] { "testProperty" };
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        this.propertyPointer.setBaseValue(baseValue);
        this.propertyPointer.setIndex(0);
        this.context = JXPathContext.newContext(baseValue);
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
    public void testGetBean() {
        assertEquals(baseValue, propertyPointer.getBean());
    }
    
    @Test
    public void testGetName() {
        QName expected = new QName(null, "testProperty");
        assertEquals(expected, propertyPointer.getName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("testProperty", propertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("newProperty");
        assertEquals("newProperty", propertyPointer.getPropertyName());
    }
    
    @Test
    public void testGetPropertyCount() {
        assertEquals(1, propertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        String[] expected = { "testProperty" };
        assertEquals(expected, propertyPointer.getPropertyNames());
    }
    
    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }
    
    @Test
    public void testIsActual_False() {
        PropertyPointer pointer = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        assertFalse(pointer.isActual());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertEquals(baseValue, propertyPointer.getImmediateNode());
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
        NodePointer expected = new NodePointer();
        expected.setBaseValue(baseValue);
        expected.setName(new QName(null, "testProperty"));
        expected.setImmediateNode(baseValue);
        assertEquals(expected, propertyPointer.getImmediateValuePointer());
    }
    
    @Test
    public void testCreatePath() {
        propertyPointer.createPath(context);
        assertEquals(baseValue, propertyPointer.getBaseValue());
    }
    
    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePath_Exception() {
        propertyPointer.setBaseValue(null);
        propertyPointer.createPath(context);
    }
    
    @Test
    public void testCreatePathWithValue() {
        Object value = new Object();
        propertyPointer.createPath(context, value);
        assertEquals(value, baseValue);
    }
    
    @Test
    public void testCreateChild_WithValue() {
        QName name = new QName(null, "child");
        int index = 0;
        NodePointer expected = new NodePointer();
        expected.setBaseValue(baseValue);
        expected.setName(name);
        expected.setIndex(index);
        expected.setImmediateNode(baseValue);
        
        NodePointer actual = propertyPointer.createChild(context, name, index, baseValue);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateChild_WithoutValue() {
        QName name = new QName(null, "child");
        int index = 0;
        NodePointer expected = new NodePointer();
        expected.setBaseValue(baseValue);
        expected.setName(name);
        expected.setIndex(index);
        expected.setImmediateNode(baseValue);
        
        NodePointer actual = propertyPointer.createChild(context, name, index);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        int expected = propertyPointer.getImmediateParentPointer().hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex();
        assertEquals(expected, propertyPointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyName("newProperty");
        propertyPointer.setIndex(1);
        
        PropertyPointer other = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        other.setBaseValue(baseValue);
        other.setPropertyIndex(1);
        other.setPropertyName("newProperty");
        
        assertTrue(propertyPointer.equals(propertyPointer));
        assertFalse(propertyPointer.equals(null));
        assertFalse(propertyPointer.equals(new Object()));
        assertFalse(propertyPointer.equals(other));
        
        other.setIndex(1);
        assertTrue(propertyPointer.equals(other));
    }
    
    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}