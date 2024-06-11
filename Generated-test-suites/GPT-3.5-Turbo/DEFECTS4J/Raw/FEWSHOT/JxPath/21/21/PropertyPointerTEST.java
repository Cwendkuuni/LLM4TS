package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {
                
            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return null;
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(10);
        assertEquals(10, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertNull(propertyPointer.getName());
    }

    @Test
    public void testIsActual() {
        assertFalse(propertyPointer.isActual());
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
        assertNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }

    @Test
    public void testCompareChildNodePointers() {
        assertEquals(0, propertyPointer.compareChildNodePointers(null, null));
    }
}