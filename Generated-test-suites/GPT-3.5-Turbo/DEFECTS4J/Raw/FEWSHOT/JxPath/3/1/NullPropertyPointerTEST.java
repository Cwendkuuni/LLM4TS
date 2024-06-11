package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {
    private NullPropertyPointer nullPropertyPointer;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = new ParentNodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("propertyName");
    }

    @Test
    public void testGetName() {
        QName expected = new QName("propertyName");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        int index = 5;
        nullPropertyPointer.setPropertyIndex(index);
    }

    @Test
    public void testGetLength() {
        int expected = 0;
        int actual = nullPropertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        Object expected = null;
        Object actual = nullPropertyPointer.getBaseValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        Object expected = null;
        Object actual = nullPropertyPointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        boolean actual = nullPropertyPointer.isLeaf();
        assertTrue(actual);
    }

    @Test
    public void testGetValuePointer() {
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actual = nullPropertyPointer.getValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        boolean actual = nullPropertyPointer.isActualProperty();
        assertFalse(actual);
    }

    @Test
    public void testIsActual() {
        boolean actual = nullPropertyPointer.isActual();
        assertFalse(actual);
    }

    @Test
    public void testIsContainer() {
        boolean actual = nullPropertyPointer.isContainer();
        assertTrue(actual);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        nullPropertyPointer.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithParentAsContainer() {
        NodePointer parent = new ParentNodePointer();
        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue(null);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = null;
        NodePointer expected = new ParentNodePointer();
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithAttribute() {
        JXPathContext context = null;
        NodePointer expected = new ParentNodePointer();
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = null;
        Object value = new Object();
        NodePointer expected = new ParentNodePointer();
        NodePointer actual = nullPropertyPointer.createPath(context, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = null;
        QName name = new QName("childName");
        int index = 5;
        NodePointer expected = new ParentNodePointer();
        NodePointer actual = nullPropertyPointer.createChild(context, name, index);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = null;
        QName name = new QName("childName");
        int index = 5;
        Object value = new Object();
        NodePointer expected = new ParentNodePointer();
        NodePointer actual = nullPropertyPointer.createChild(context, name, index, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        String expected = "propertyName";
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "newPropertyName";
        nullPropertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "newAttributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        nullPropertyPointer.setPropertyIndex(5);
        boolean actual = nullPropertyPointer.isCollection();
        assertTrue(actual);
    }

    @Test
    public void testGetPropertyCount() {
        int expected = 0;
        int actual = nullPropertyPointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        String[] expected = new String[0];
        String[] actual = nullPropertyPointer.getPropertyNames();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPath() {
        String expected = "";
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    private class ParentNodePointer extends NodePointer {
        @Override
        public QName getName() {
            return null;
        }

        @Override
        public void setValue(Object value) {
        }

        @Override
        public boolean isContainer() {
            return false;
        }

        @Override
        public NodePointer createPath(JXPathContext context) {
            return this;
        }

        @Override
        public NodePointer createChild(JXPathContext context, QName name, int index) {
            return null;
        }

        @Override
        public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
            return null;
        }
    }
}