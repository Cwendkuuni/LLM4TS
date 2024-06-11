package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractFactory;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerCreation() {
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        assertEquals(parentPointer, propertyPointer.getImmediateParentPointer());
        assertNull(propertyPointer.getName());
        assertNull(propertyPointer.getValue());
        assertFalse(propertyPointer.isActual());
        assertSame(WHOLE_COLLECTION, propertyPointer.getPropertyIndex());
        assertSame(WHOLE_COLLECTION, propertyPointer.getIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        assertEquals(new QName(null, "testProperty"), propertyPointer.getName());
    }

    @Test
    public void testSetName() {
        String propertyName = "testProperty";
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public void setPropertyName(String propertyName) {
                this.propertyName = propertyName;
            }
        };
        propertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 3;
            }
        };
        assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1", "property2", "property3"};
            }
        };
        assertArrayEquals(new String[]{"property1", "property2", "property3"}, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() throws Exception {
        Object baseValue = new Object[]{1, 2, 3};
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };
        propertyPointer.setPropertyIndex(1);
        Object immediateNode = propertyPointer.getImmediateNode();
        assertEquals(2, immediateNode);
    }

    @Test
    public void testIsCollection() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new Integer(1);
            }
        };
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };
        propertyPointer.setIndex(2);
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() throws Exception {
        Object baseValue = new Object[]{1, 2, 3};
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };
        propertyPointer.setIndex(1);
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(2, immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePath() throws Exception {
        AbstractFactory factory = mock(AbstractFactory.class);
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setBean(new Object());

        when(factory.createObject(context, propertyPointer, any(), anyString(), anyInt()))
                .thenReturn(NodePointer.newChildNodePointer(null, null, null));

        NodePointer result = propertyPointer.createPath(context);
        assertEquals(propertyPointer, result);
    }

    @Test
    public void testCreatePathWithValue() throws Exception {
        AbstractFactory factory = mock(AbstractFactory.class);
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setBean(new Object());

        when(factory.createObject(context, propertyPointer, any(), anyString(), anyInt()))
                .thenThrow(new Exception("Test exception"));

        Object value = new Object();
        NodePointer result = propertyPointer.createPath(context, value);
        assertEquals(propertyPointer, result);
    }

    @Test
    public void testCreateChildNodePointer() throws Exception {
        AbstractFactory factory = mock(AbstractFactory.class);
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setBean(new Object());

        NodePointer result = propertyPointer.createChild(context, null, 0, null);
        assertNotNull(result);
        assertEquals(propertyPointer.getClass(), result.getClass());
    }

    @Test
    public void testCreateChildNodePointerWithName() throws Exception {
        AbstractFactory factory = mock(AbstractFactory.class);
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setBean(new Object());

        when(factory.createObject(context, propertyPointer, any(), anyString(), anyInt()))
                .thenThrow(new Exception("Test exception"));

        String childName = "testChild";
        NodePointer result = propertyPointer.createChild(context, new QName(null, childName), 0, null);
        assertNotNull(result);
        assertEquals(propertyPointer.getClass(), result.getClass());
    }

    @Test
    public void testCreateChildNodePointerWithIndex() throws Exception {
        AbstractFactory factory = mock(AbstractFactory.class);
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setBean(new Object());

        NodePointer result = propertyPointer.createChild(context, null, 0, null);
        assertNotNull(result);
        assertEquals(propertyPointer.getClass(), result.getClass());

        when(factory.createObject(context, propertyPointer, any(), anyString(), anyInt()))
                .thenThrow(new Exception("Test exception"));

        NodePointer childPointer = NodePointer.newChildNodePointer(null, null, null);
        NodePointer result2 = propertyPointer.createChild(context, null, 1, childPointer);
        assertNotNull(result2);
        assertEquals(propertyPointer.getClass(), result2.getClass());
    }

    @Test
    public void testCreateChildNodePointerWithNameAndIndex() throws Exception {
        AbstractFactory factory = mock(AbstractFactory.class);
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setBean(new Object());

        NodePointer result = propertyPointer.createChild(context, new QName(null, "testChild"), 0, null);
        assertNotNull(result);
        assertEquals(propertyPointer.getClass(), result.getClass());

        when(factory.createObject(context, propertyPointer, any(), anyString(), anyInt()))
                .thenThrow(new Exception("Test exception"));

        NodePointer childPointer = NodePointer.newChildNodePointer(null, null, null);
        NodePointer result2 = propertyPointer.createChild(context, new QName(null, "testChild2"), 1, childPointer);
        assertNotNull(result2);
        assertEquals(propertyPointer.getClass(), result2.getClass());

        verify(factory, atLeast(2)).createObject(context, propertyPointer, any(), anyString(), anyInt());
    }

    @Test
    public void testHashCode() {
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
            @Override
            public int getPropertyIndex() {
                return 42;
            }
        };

        assertEquals(propertyPointer.hashCode(), propertyPointer2.hashCode());
        assertNotEquals(propertyPointer.hashCode(), propertyPointer3.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
            @Override
            public int getPropertyIndex() {
                return 42;
            }
        };

        assertTrue(propertyPointer.equals(propertyPointer2));
        assertFalse(propertyPointer.equals(propertyPointer3));
        assertFalse(propertyPointer.equals(new Object()));
        assertFalse(propertyPointer.equals(null));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer parentPointer = NodePointer.newChildNodePointer(null, null, null);
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer) {
            @Override
            public NodePointer getValuePointer() {
                return NodePointer.newChildNodePointer(null, null, null);
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer) {
            @Override
            public NodePointer getValuePointer() {
                return NodePointer.newChildNodePointer(null, null, null);
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
            @Override
            public NodePointer getValuePointer() {
                return NodePointer.newChildNodePointer(null, null, null);
            }
        };
        NodePointer childNodePointer1 = NodePointer.newChildNodePointer(null, null, null);
        NodePointer childNodePointer2 = NodePointer.newChildNodePointer(null, null, null);

        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1));
        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer2));
        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer3));
        assertEquals(0, propertyPointer1.compareChildNodePointers(childNodePointer1, childNodePointer1));
        assertEquals(0, propertyPointer1.compareChildNodePointers(childNodePointer2, childNodePointer2));

        assertLessThan(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1));
        assertLessThan(0, propertyPointer1.compareChildNodePointers(childNodePointer1, propertyPointer1));
        assertLessThan(0, propertyPointer1.compareChildNodePointers(propertyPointer3, childNodePointer1));

        assertGreaterThan(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer2));
        assertGreaterThan(0, propertyPointer1.compareChildNodePointers(childNodePointer2, propertyPointer1));
        assertGreaterThan(0, propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer2));
    }
}