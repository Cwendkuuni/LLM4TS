package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getValuePointer());
        assertFalse(nullPropertyPointer.isActual());
        assertEquals("", nullPropertyPointer.asPath());
    }

    @Test
    public void testNullPropertyPointerWithParent() {
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = new DummyNodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        assertEquals(nodePointer, nullPropertyPointer.getParentPointer());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getValuePointer());
        assertFalse(nullPropertyPointer.isActual());
        assertEquals("$/", nullPropertyPointer.asPath());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(new QName("*"), nullPropertyPointer.getName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName qname = new QName("test");
        nullPropertyPointer.setPropertyName(qname.getLocalPart());
        assertEquals(qname, nullPropertyPointer.getName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals(new QName("test"), nullPropertyPointer.getName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = new DummyNodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        NodePointer result = nullPropertyPointer.createPath(context);
        assertEquals(nodePointer, result.getParentPointer());
        assertNull(result.getImmediateNode());
        assertNull(result.getBaseValue());
        assertNull(result.getValuePointer());
        assertFalse(result.isActual());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = new DummyNodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createPath(context, value);
        assertEquals(nodePointer, result.getParentPointer());
        assertNull(result.getImmediateNode());
        assertSame(value, result.getBaseValue());
        assertNull(result.getValuePointer());
        assertFalse(result.isActual());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        NodePointer nodePointer = new DummyNodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        NodePointer result = nullPropertyPointer.createChild(context, name);
        assertEquals(nodePointer, result.getParentPointer());
        assertNull(result.getImmediateNode());
        assertNull(result.getBaseValue());
        assertNull(result.getValuePointer());
        assertFalse(result.isActual());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        Object value = new Object();
        NodePointer nodePointer = new DummyNodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        NodePointer result = nullPropertyPointer.createChild(context, name, value);
        assertEquals(nodePointer, result.getParentPointer());
        assertNull(result.getImmediateNode());
        assertSame(value, result.getBaseValue());
        assertNull(result.getValuePointer());
        assertFalse(result.isActual());
    }

    @Test
    public void testSetValue() {
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = new PropertyOwnerDummyNodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        Object value = new Object();
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(value));
    }

    private static class DummyNodePointer implements NodePointer {
        @Override
        public NodePointer createPath(JXPathContext context) {
            return this;
        }

        @Override
        public NodePointer createChild(JXPathContext context, QName name, int index) {
            return new DummyNodePointer();
        }

        @Override
        public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
            return new DummyNodePointer();
        }

        @Override
        public NodePointer createAttribute(JXPathContext context, QName name) {
            return new DummyNodePointer();
        }

        @Override
        public NodePointer createAttribute(JXPathContext context, QName name, Object value) {
            return new DummyNodePointer();
        }

        @Override
        public NodePointer getParentPointer() {
            return null;
        }

        @Override
        public JXPathContext getContext() {
            return context;
        }

        @Override
        public String asPath() {
            return "";
        }
    }

    private static class PropertyOwnerDummyNodePointer extends DummyNodePointer implements PropertyOwnerPointer {
        @Override
        public PropertyPointer getPropertyPointer() {
            return new DummyPropertyPointer();
        }
    }

    private static class DummyPropertyPointer implements PropertyPointer {
        @Override
        public void setPropertyName(QName name) {
            // Empty
        }

        @Override
        public void setPropertyValue(Object value) {
            // Empty
        }

        @Override
        public NodePointer createPath(JXPathContext context) {
            return this;
        }

        @Override
        public NodePointer createChild(JXPathContext context, QName name, int index) {
            return new DummyNodePointer();
        }

        @Override
        public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
            return new DummyNodePointer();
        }

        @Override
        public NodePointer getParentPointer() {
            return null;
        }

        @Override
        public JXPathContext getContext() {
            return null;
        }

        @Override
        public String asPath() {
            return "";
        }

        @Override
        public boolean isDynamicPropertyDeclarationSupported() {
            return true;
        }
    }
}