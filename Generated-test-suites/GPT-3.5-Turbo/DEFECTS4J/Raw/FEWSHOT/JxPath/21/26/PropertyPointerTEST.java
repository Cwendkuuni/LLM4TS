//PropertyPointerTest.java
package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    private NodePointer parentPointer;
    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        parentPointer = new NodePointer();
        propertyPointer = new PropertyPointer(parentPointer) {
            public String getPropertyName() {
                return "property";
            }

            public void setPropertyName(String propertyName) {
                // not implemented
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[] { "property" };
            }

            protected boolean isActualProperty() {
                return true;
            }
        };
    }

    @Test
    public void testConstructor() {
        assertEquals(parentPointer, propertyPointer.getParent());
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
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
        propertyPointer.setBaseValue(new Object());
        assertEquals(propertyPointer.getBaseValue(), propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertEquals("property", propertyPointer.getName().getName());
    }

    @Test
    public void testIsActual() {
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        Object value = new Object();
        propertyPointer.setBaseValue(value);
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        propertyPointer.setBaseValue(new Object[0]);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        propertyPointer.setBaseValue(new Object());
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(valuePointer.getBaseValue(), valuePointer.getValue());
    }

    @Test
    public void testCreatePathWithNullBaseValue() {
        propertyPointer.setBaseValue(null);
        JXPathContext context = JXPathContext.newContext(new Object());
        propertyPointer.createPath(context);
        assertEquals(ValueUtils.getValue(propertyPointer.getBaseValue()), ValueUtils.getValue(context.getValue("property")));
    }

    @Test
    public void testCreatePathWithValue() {
        Object value = new Object();
        JXPathContext context = JXPathContext.newContext(new Object());
        propertyPointer.createPath(context, value);
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithIndex() {
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer childPointer = propertyPointer.createChild(context, null, 0);
        assertEquals(childPointer.getBaseValue(), childPointer.getValue());
        assertEquals(propertyPointer.getPropertyIndex(), childPointer.getPropertyIndex());
    }

    @Test
    public void testCreateChildWithValue() {
        Object value = new Object();
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer childPointer = propertyPointer.createChild(context, null, value);
        assertEquals(value, childPointer.getImmediateNode());
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}