package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {

    private PropertyOwnerPointer parentPointer;
    private PropertyPointer propertyPointer;
    private JXPathContext context;
    private AbstractFactory factory;

    @Before
    public void setUp() {
        parentPointer = new PropertyOwnerPointer();
        propertyPointer = new PropertyPointer(parentPointer) {

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
        context = JXPathContext.newContext(null);
        factory = new AbstractFactory() {
            @Override
            public boolean createObject(JXPathContext context, NodePointer pointer, Object parent, String property, int index) {
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
        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());
        assertEquals(NodePointer.WHOLE_COLLECTION, propertyPointer.getIndex());
    }

    @Test
    public void testGetBean() {
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        QName name = propertyPointer.getName();
        assertNull(name.getPrefix());
        assertNull(name.getName());
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
        assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(propertyPointer, valuePointer.getParent());
        assertEquals(new QName(null, null), valuePointer.getName());
        assertNull(valuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        NodePointer path = propertyPointer.createPath(context);
        assertEquals(propertyPointer, path);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer path = propertyPointer.createPath(context, "value");
        assertEquals(propertyPointer, path);
        assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathWithException() {
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreateChildWithValue() {
        QName name = new QName("prefix", "name");
        NodePointer child = propertyPointer.createChild(context, name, 0, "value");
        assertEquals(propertyPointer, child.getParent());
        assertEquals(name, child.getName());
        assertEquals("value", child.getNode());
    }

    @Test
    public void testCreateChildWithoutValue() {
        QName name = new QName("prefix", "name");
        NodePointer child = propertyPointer.createChild(context, name, 0);
        assertEquals(propertyPointer, child.getParent());
        assertEquals(name, child.getName());
        assertNull(child.getNode());
    }

    @Test
    public void testHashCode() {
        NodePointer pointer = new PropertyPointer(parentPointer) {

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
        pointer.setIndex(5);
        propertyPointer.setIndex(5);
        assertEquals(pointer.hashCode(), propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer pointer = new PropertyPointer(parentPointer) {

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
        pointer.setIndex(5);
        propertyPointer.setIndex(5);
        assertTrue(propertyPointer.equals(pointer));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new PropertyPointer(parentPointer) {

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
                return true;
            }

            @Override
            public boolean compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return false;
            }
        };
        NodePointer pointer2 = new PropertyPointer(parentPointer) {

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
                return true;
            }

            @Override
            public boolean compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return false;
            }
        };
        assertFalse(propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}