
package org.apache.commons.jxpath.ri.model.beans;

import org.junit.Assert;
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
        int expected = PropertyPointer.UNSPECIFIED_PROPERTY;
        int actual = propertyPointer.getPropertyIndex();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        int expected = 5;
        propertyPointer.setPropertyIndex(expected);
        int actual = propertyPointer.getPropertyIndex();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBean() {
        Object expected = null;
        Object actual = propertyPointer.getBean();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        String expected = "";
        String actual = propertyPointer.getName().getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        Object expected = null;
        Object actual = propertyPointer.getImmediateNode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsCollection() {
        boolean expected = false;
        boolean actual = propertyPointer.isCollection();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        boolean expected = false;
        boolean actual = propertyPointer.isLeaf();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLength() {
        int expected = -1;
        int actual = propertyPointer.getLength();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateValuePointer() {
        Object expected = null;
        Object actual = propertyPointer.getImmediateValuePointer().getNode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreatePath() {
        propertyPointer.createPath(null);
        // No exceptions should be thrown
        Assert.assertTrue(true);
    }

    @Test
    public void testCreatePathWithValue() {
        propertyPointer.createPath(null, null);
        // No exceptions should be thrown
        Assert.assertTrue(true);
    }

    @Test
    public void testCreateChildWithPathValue() {
        Object expected = null;
        Object actual = propertyPointer.createChild(null, null, 0, null).getNode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithPath() {
        Object expected = null;
        Object actual = propertyPointer.createChild(null, null, 0).getNode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = propertyPointer.getImmediateParentPointer().hashCode()
                + propertyPointer.getPropertyIndex()
                + propertyPointer.getIndex();
        int actual = propertyPointer.hashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        boolean expected = false;
        boolean actual = propertyPointer.equals(null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCompareChildNodePointers() {
        int expected = propertyPointer.getValuePointer().compareChildNodePointers(null, null);
        int actual = propertyPointer.compareChildNodePointers(null, null);
        Assert.assertEquals(expected, actual);
    }
}
