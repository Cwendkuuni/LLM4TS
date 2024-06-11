package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PropertyPointerTest {
    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyIndex(5);
        
        assertEquals(5, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyIndex(5);
        
        assertEquals(5, propertyPointer.getPropertyIndex());
        
        propertyPointer.setPropertyIndex(10);
        
        assertEquals(10, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setBean("test");
        
        assertEquals("test", propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        
        assertEquals("name", propertyPointer.getName().toString());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        
        assertEquals("name", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        
        assertEquals("name", propertyPointer.getPropertyName());
        
        propertyPointer.setPropertyName("newName");
        
        assertEquals("newName", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        
        assertEquals(0, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);

        String[] propertyNames = propertyPointer.getPropertyNames();
        
        assertEquals(0, propertyNames.length);

        propertyPointer.setPropertyName("name");
        propertyNames = propertyPointer.getPropertyNames();
        
        assertEquals(1, propertyNames.length);
        assertEquals("name", propertyNames[0]);
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setValue("test");
        
        assertEquals("test", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        
        assertFalse(propertyPointer.isCollection());
        
        propertyPointer.setIndex(1);
        
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        
        assertTrue(propertyPointer.isLeaf());
        
        propertyPointer.setValue("test");
        
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        
        assertEquals(-1, propertyPointer.getLength());
        
        propertyPointer.setValue(new String[] {"item1", "item2"});
        
        assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        propertyPointer.setValue("value");
        
        NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
        
        assertEquals("value", nodePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        
        propertyPointer.createPath(new JXPathContext());
        
        assertEquals("name", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        
        propertyPointer.createPath(new JXPathContext(), "value");
        
        assertEquals("name", propertyPointer.getImmediateNode());
        assertEquals("value", propertyPointer.getValue());
    }

    @Test
    public void testCreateChildWithIndexAndValue() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        propertyPointer.setIndex(1);
        
        NodePointer nodePointer = propertyPointer.createChild(new JXPathContext(), null, 0, "value");
        
        assertEquals("value", nodePointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithIndex() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        propertyPointer.setIndex(1);
        
        NodePointer nodePointer = propertyPointer.createChild(new JXPathContext(), null, 0);
        
        assertEquals("name", nodePointer.getImmediateNode());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        propertyPointer.setPropertyName("name");
        propertyPointer.setIndex(1);
        
        assertEquals(31 * 31 + 1 + 1, propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(null);
        propertyPointer1.setPropertyName("name");
        propertyPointer1.setIndex(1);
        
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(null);
        propertyPointer2.setPropertyName("name");
        propertyPointer2.setIndex(1);
        
        PropertyPointer propertyPointer3 = new PropertyPointerImpl(null);
        propertyPointer3.setPropertyName("newName");
        propertyPointer3.setIndex(2);
        
        assertTrue(propertyPointer1.equals(propertyPointer2));
        assertFalse(propertyPointer1.equals(propertyPointer3));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(new PropertyPointerImpl(null));
        
        int result = propertyPointer.compareChildNodePointers(null, null);
        
        assertEquals(0, result);
    }

    private static class PropertyPointerImpl extends PropertyPointer {
        public PropertyPointerImpl(NodePointer parent) {
            super(parent);
        }

        public String getPropertyName() {
            return null;
        }

        public void setPropertyName(String propertyName) {
            
        }

        public int getPropertyCount() {
            return 0;
        }

        public String[] getPropertyNames() {
            return null;
        }

        protected boolean isActualProperty() {
            return true;
        }
    }
}