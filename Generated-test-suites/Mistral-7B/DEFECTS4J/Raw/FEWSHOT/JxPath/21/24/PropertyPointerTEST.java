package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.apache.commons.jxpath.util.SimpleNodePointer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyPointerTest {

    private static final Object UNINITIALIZED = new Object();

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertEquals(-1, pointer.getPropertyIndex());

        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());

        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());

        pointer.setPropertyIndex(-1);
        assertEquals(-1, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer(), bean);
        assertEquals(bean, pointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertNull(pointer.getName());

        pointer.setPropertyName("propertyName");
        QName name = pointer.getName();
        assertEquals("propertyName", name.getLocalPart());
        assertEquals("", name.getNamespaceURI());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());

        pointer.setPropertyName("propertyName");
        assertEquals("propertyName", pointer.getPropertyName());

        pointer.setPropertyName(null);
        assertNull(pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertEquals(-1, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertNull(pointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertFalse(pointer.isActual());

        pointer.setActual(true);
        assertTrue(pointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertNull(pointer.getImmediateNode());

        pointer.setValue(new Object());
        assertNotNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testGetLength() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        assertEquals(-1, pointer.getLength());

        pointer.setValue(new Object()[3]);
        assertEquals(3, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());

        pointer.setValue(new Object());
        NodePointer valuePointer = pointer.getImmediateValuePointer();
        assertNotNull(valuePointer);
        assertEquals(pointer.getName(), valuePointer.getName());
        assertEquals(pointer.getBean(), valuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new DummyBean());
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());

        pointer.setPropertyName("property");
        pointer.setPropertyIndex(0);
        pointer.createPath(context);

        assertEquals("property", pointer.getName().getLocalPart());
        assertEquals(new DummyBean(), pointer.getBean());
        assertEquals(0, pointer.getPropertyIndex());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(new DummyBean());
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());

        pointer.setPropertyName("property");
        pointer.setPropertyIndex(0);
        pointer.setValue(new Object());
        pointer.createPath(context, new Object());

        assertEquals("property", pointer.getName().getLocalPart());
        assertEquals(new DummyBean(), pointer.getBean());
        assertEquals(0, pointer.getPropertyIndex());
        assertEquals(new Object(), pointer.getImmediateNode());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new DummyBean());
        PropertyPointer parentPointer = new DummyPropertyPointer(new DummyParentPointer());
        PropertyPointer childPointer = parentPointer.createChild(context, new QName("child"), 0);

        assertEquals("child", childPointer.getName().getLocalPart());
        assertEquals(parentPointer.getBean(), childPointer.getBean());
        assertEquals(-1, childPointer.getPropertyIndex());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(new DummyBean());
        PropertyPointer parentPointer = new DummyPropertyPointer(new DummyParentPointer());

        parentPointer.setPropertyName("property");
        parentPointer.setPropertyIndex(0);
        parentPointer.setValue(new Object()[3]);

        PropertyPointer childPointer = parentPointer.createChild(context, new QName("child"), 0, new Object());

        assertEquals("child", childPointer.getName().getLocalPart());
        assertEquals(parentPointer.getBean(), childPointer.getBean());
        assertEquals(-1, childPointer.getPropertyIndex());
        assertEquals(new Object(), childPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithValueAndName() {
        JXPathContext context = new JXPathContext(new DummyBean());
        PropertyPointer parentPointer = new DummyPropertyPointer(new DummyParentPointer());

        parentPointer.setPropertyName("property");
        parentPointer.setPropertyIndex(0);
        parentPointer.setValue(new Object()[3]);

        PropertyPointer childPointer = parentPointer.createChild(context, new QName("child", "namespace"), 0, new Object());

        assertEquals("child", childPointer.getName().getLocalPart());
        assertEquals("namespace", childPointer.getName().getNamespaceURI());
        assertEquals(parentPointer.getBean(), childPointer.getBean());
        assertEquals(-1, childPointer.getPropertyIndex());
        assertEquals(new Object(), childPointer.getImmediateNode());
    }

    @Test
    public void testClone() {
        PropertyPointer pointer = new DummyPropertyPointer(new DummyParentPointer());
        PropertyPointer clonedPointer = (PropertyPointer) pointer.clone();

        assertEquals(pointer.getName(), clonedPointer.getName());
        assertEquals(pointer.getBean(), clonedPointer.getBean());
        assertEquals(pointer.getPropertyIndex(), clonedPointer.getPropertyIndex());
        assertEquals(pointer.isActual(), clonedPointer.isActual());
        assertEquals(pointer.getValue(), clonedPointer.getValue());
        assertEquals(pointer.getParent(), clonedPointer.getParent());
    }

    @Test
    public void testHashCode() {
        PropertyPointer pointer1 = new DummyPropertyPointer(new DummyParentPointer());
        PropertyPointer pointer2 = new DummyPropertyPointer(new DummyParentPointer());

        pointer1.setPropertyName("name1");
        pointer1.setPropertyIndex(1);
        pointer1.setIndex(3);

        pointer2.setPropertyName("name2");
        pointer2.setPropertyIndex(0);
        pointer2.setIndex(3);

        assertEquals(pointer1.hashCode(), pointer1.hashCode());
        assertEquals(pointer1.hashCode(), pointer1.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer pointer1 = new DummyPropertyPointer(new DummyParentPointer());
        PropertyPointer pointer2 = new DummyPropertyPointer(new DummyParentPointer());

        PropertyPointer pointer3 = new DummyPropertyPointer(new DummyParentPointer());

        pointer1.setPropertyName("name1");
        pointer1.setPropertyIndex(1);
        pointer1.setIndex(3);

        pointer2.setPropertyName("name1");
        pointer2.setPropertyIndex(1);
        pointer2.setIndex(3);

        pointer3.setPropertyName("name2");
        pointer3.setPropertyIndex(0);
        pointer3.setIndex(3);

        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer2.equals(pointer3));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer1 = new DummyPropertyPointer(new DummyParentPointer());
        PropertyPointer pointer2 = new DummyPropertyPointer(new DummyParentPointer());

        PropertyPointer pointer3 = new DummyPropertyPointer(new DummyParentPointer());

        pointer1.setPropertyName("name1");
        pointer1.setPropertyIndex(1);
        pointer1.setIndex(3);

        pointer2.setPropertyName("name1");
        pointer2.setPropertyIndex(1);
        pointer2.setIndex(4);

        pointer3.setPropertyName("name2");
        pointer3.setPropertyIndex(0);
        pointer3.setIndex(3);

        NodePointer valuePointer1 = new SimpleNodePointer(new DummyBean(), "value1");
        NodePointer valuePointer2 = new SimpleNodePointer(new DummyBean(), "value2");

        int result = pointer1.compareChildNodePointers(pointer1, pointer2);
        assertEquals(0, result);

        result = pointer1.compareChildNodePointers(pointer1, pointer3);
        assertEquals(-1, result);

        result = pointer1.compareChildNodePointers(pointer2, pointer3);
        assertEquals(1, result);

        result = pointer2.compareChildNodePointers(pointer1, pointer2);
        assertEquals(-1, result);

        result = pointer2.compareChildNodePointers(pointer2, pointer3);
        assertEquals(0, result);
    }

    private static class DummyParentPointer extends NodePointer {
        DummyParentPointer() {
            super(null, null, null);
        }

        DummyParentPointer(NodePointer parent) {
            super(parent, null, null);
        }

        DummyParentPointer(NodePointer parent, Object value) {
            super(parent, value, null);
        }

        @Override
        public String getPropertyName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPropertyName(String propertyName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getPropertyCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String[] getPropertyNames() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isActualProperty() {
            throw new UnsupportedOperationException();
        }
    }

    private static class DummyPropertyPointer extends PropertyPointer {
        DummyPropertyPointer() {
            super(null);
        }

        DummyPropertyPointer(NodePointer parent) {
            super(parent);
        }

        DummyPropertyPointer(NodePointer parent, Object bean) {
            super(parent);
            this.bean = bean;
        }

        @Override
        public String getPropertyName() {
            return "property";
        }

        @Override
        public void setPropertyName(String propertyName) {
            // empty
        }

        @Override
        public int getPropertyCount() {
            return 3;
        }

        @Override
        public String[] getPropertyNames() {
            return new String[]{"name1", "name2", "name3"};
        }

        @Override
        public boolean isActualProperty() {
            return true;
        }

        @Override
        public void setActual(boolean actual) {
            // empty
        }

        @Override
        public Object getBaseValue() {
            return new Object()[3];
        }
    }

    private static class DummyBean {
        // empty
    }
}