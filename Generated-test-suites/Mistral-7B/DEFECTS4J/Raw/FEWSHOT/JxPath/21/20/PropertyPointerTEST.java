package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testAbstractPropertyPointer() {
        AbstractPropertyPointer abstractPropertyPointer = new AbstractPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // NOP
            }

            @Override
            public int getPropertyCount() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[0];
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        assertFalse(abstractPropertyPointer.isActual());
        assertNull(abstractPropertyPointer.getValue());
        assertNull(abstractPropertyPointer.getName());
        assertEquals(-1, abstractPropertyPointer.getLength());
        assertNull(abstractPropertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testPropertyPointer() {
        NodePointer parent = NodePointer.newChildNodePointer(
            null, new QName("parent"), new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // NOP
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"p1", "p2", "p3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals(parent, propertyPointer.getImmediateParentPointer());
        assertEquals("testProperty", propertyPointer.getName().getLocalPart());
        assertTrue(propertyPointer.isActual());
        assertNotNull(propertyPointer.getValue());
        assertEquals(3, propertyPointer.getPropertyCount());
        assertArrayEquals(new String[]{"p1", "p2", "p3"}, propertyPointer.getPropertyNames());
        assertEquals(1, propertyPointer.getLength());
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testPropertyPointerCreatePath() {
        NodePointer parent = NodePointer.newChildNodePointer(
            null, new QName("parent"), new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // NOP
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"p1", "p2", "p3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Object bean = new Object();
        propertyPointer.setBean(bean);

        Object value = new Object();
        propertyPointer.setValue(value);

        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory() {
            @Override
            public Object createObject(
                JXPathContext context,
                NodePointer parent,
                Object bean,
                String propertyName,
                int index) {
                // NOP
                return null;
            }
        };

        PropertyPointer createdPropertyPointer = (PropertyPointer) propertyPointer.createPath(context);
        assertEquals(propertyPointer, createdPropertyPointer);
        assertEquals(bean, createdPropertyPointer.getBean());
        assertEquals(value, createdPropertyPointer.getValue());
        assertEquals("testProperty", createdPropertyPointer.getName().getLocalPart());
        assertEquals(3, createdPropertyPointer.getPropertyCount());
        assertArrayEquals(new String[]{"p1", "p2", "p3"}, createdPropertyPointer.getPropertyNames());
        assertEquals(1, createdPropertyPointer.getLength());
        assertNotNull(createdPropertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testPropertyPointerCreatePathWithValue() {
        NodePointer parent = NodePointer.newChildNodePointer(
            null, new QName("parent"), new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // NOP
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"p1", "p2", "p3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Object bean = new Object();
        propertyPointer.setBean(bean);

        Object value = new Object();
        propertyPointer.setValue(value);

        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory() {
            @Override
            public Object createObject(
                JXPathContext context,
                NodePointer parent,
                Object bean,
                String propertyName,
                int index) {
                // NOP
                return null;
            }
        };

        PropertyPointer createdPropertyPointer = (PropertyPointer) propertyPointer.createPath(context, new Object());
        assertEquals(propertyPointer, createdPropertyPointer);
        assertEquals(bean, createdPropertyPointer.getBean());
        assertEquals(value, createdPropertyPointer.getValue());
        assertEquals("testProperty", createdPropertyPointer.getName().getLocalPart());
        assertEquals(3, createdPropertyPointer.getPropertyCount());
        assertArrayEquals(new String[]{"p1", "p2", "p3"}, createdPropertyPointer.getPropertyNames());
        assertEquals(1, createdPropertyPointer.getLength());
        assertNotNull(createdPropertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testPropertyPointerCreateChild() {
        NodePointer parent = NodePointer.newChildNodePointer(
            null, new QName("parent"), new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // NOP
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"p1", "p2", "p3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer childPropertyPointer1 = (PropertyPointer) propertyPointer.createChild(
            new JXPathContext(), new QName("child1"));
        PropertyPointer childPropertyPointer2 = (PropertyPointer) propertyPointer.createChild(
            new JXPathContext(), new QName("child1"), 1);

        assertEquals(propertyPointer, childPropertyPointer1.getImmediateParentPointer());
        assertEquals(propertyPointer, childPropertyPointer2.getImmediateParentPointer());
        assertEquals("child1", childPropertyPointer1.getName().getLocalPart());
        assertEquals("child1", childPropertyPointer2.getName().getLocalPart());
        assertEquals(3, childPropertyPointer1.getPropertyCount());
        assertArrayEquals(new String[]{"p1", "p2", "p3"}, childPropertyPointer1.getPropertyNames());
        assertEquals(1, childPropertyPointer2.getPropertyCount());
        assertArrayEquals(new String[0], childPropertyPointer2.getPropertyNames());
        assertEquals(-1, childPropertyPointer1.getLength());
        assertEquals(-1, childPropertyPointer2.getLength());
        assertNull(childPropertyPointer1.getValue());
        assertNull(childPropertyPointer2.getValue());
        assertNotNull(childPropertyPointer1.getImmediateValuePointer());
        assertNotNull(childPropertyPointer2.getImmediateValuePointer());
    }

    @Test
    public void testPropertyPointerCreateChildWithValue() {
        NodePointer parent = NodePointer.newChildNodePointer(
            null, new QName("parent"), new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // NOP
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"p1", "p2", "p3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Object childValue = new Object();

        PropertyPointer childPropertyPointer1 = (PropertyPointer) propertyPointer.createChild(
            new JXPathContext(), new QName("child1"), null, childValue);
        PropertyPointer childPropertyPointer2 = (PropertyPointer) propertyPointer.createChild(
            new JXPathContext(), new QName("child1"), 1, childValue);

        assertEquals(propertyPointer, childPropertyPointer1.getImmediateParentPointer());
        assertEquals(propertyPointer, childPropertyPointer2.getImmediateParentPointer());
        assertEquals("child1", childPropertyPointer1.getName().getLocalPart());
        assertEquals("child1", childPropertyPointer2.getName().getLocalPart());
        assertEquals(3, childPropertyPointer1.getPropertyCount());
        assertArrayEquals(new String[]{"p1", "p2", "p3"}, childPropertyPointer1.getPropertyNames());
        assertEquals(1, childPropertyPointer2.getPropertyCount());
        assertArrayEquals(new String[0], childPropertyPointer2.getPropertyNames());
        assertEquals(1, childPropertyPointer1.getLength());
        assertEquals(1, childPropertyPointer2.getLength());
        assertEquals(childValue, childPropertyPointer1.getValue());
        assertEquals(childValue, childPropertyPointer2.getValue());
        assertNotNull(childPropertyPointer1.getImmediateValuePointer());
        assertNotNull(childPropertyPointer2.getImmediateValuePointer());
    }
}