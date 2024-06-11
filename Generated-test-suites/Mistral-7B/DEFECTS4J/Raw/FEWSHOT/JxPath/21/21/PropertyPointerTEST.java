package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer1 = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals(parent, propertyPointer1.getImmediateParentPointer());
        assertEquals("property1", propertyPointer1.getPropertyName());
        assertEquals(1, propertyPointer1.getPropertyCount());
        assertArrayEquals(new String[]{"property1"}, propertyPointer1.getPropertyNames());
        assertTrue(propertyPointer1.isActual());

        Object value = new Object();
        propertyPointer1.setValue(value);
        assertEquals(value, propertyPointer1.getImmediateNode());

        assertFalse(propertyPointer1.isCollection());
        assertFalse(propertyPointer1.isLeaf());
        assertEquals(-1, propertyPointer1.getLength());

        NodePointer valuePointer = propertyPointer1.getValuePointer();
        assertEquals(propertyPointer1, valuePointer.getParentPointer());
        assertEquals(value, valuePointer.getNode());

        NodePointer child = propertyPointer1.getImmediateValuePointer();
        assertEquals(propertyPointer1, child.getParentPointer());
        assertEquals(value, child.getNode());

        PropertyPointer propertyPointer2 = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property2"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertNotSame(propertyPointer1, propertyPointer2);
        assertEquals(parent, propertyPointer2.getImmediateParentPointer());
        assertEquals("property2", propertyPointer2.getPropertyName());
        assertEquals(1, propertyPointer2.getPropertyCount());
        assertArrayEquals(new String[]{"property2"}, propertyPointer2.getPropertyNames());
        assertTrue(propertyPointer2.isActual());

        propertyPointer2.setValue(value);
        assertEquals(value, propertyPointer2.getImmediateNode());

        NodePointer valuePointer2 = propertyPointer2.getValuePointer();
        assertEquals(propertyPointer2, valuePointer2.getParentPointer());
        assertEquals(value, valuePointer2.getNode());

        NodePointer child2 = propertyPointer2.getImmediateValuePointer();
        assertEquals(propertyPointer2, child2.getParentPointer());
        assertEquals(value, child2.getNode());

        PropertyPointer propertyPointer3 = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1", "property2"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertNotSame(propertyPointer1, propertyPointer3);
        assertEquals(parent, propertyPointer3.getImmediateParentPointer());
        assertEquals("property1", propertyPointer3.getPropertyName());
        assertEquals(2, propertyPointer3.getPropertyCount());
        assertArrayEquals(new String[]{"property1", "property2"}, propertyPointer3.getPropertyNames());
        assertTrue(propertyPointer3.isActual());

        Object value2 = new Object();
        propertyPointer3.setValue(value2);
        assertEquals(value2, propertyPointer3.getImmediateNode());

        NodePointer valuePointer3 = propertyPointer3.getValuePointer();
        assertEquals(propertyPointer3, valuePointer3.getParentPointer());
        assertEquals(value2, valuePointer3.getNode());

        NodePointer child3 = propertyPointer3.getImmediateValuePointer(0);
        assertEquals(propertyPointer3, child3.getParentPointer());
        assertEquals(value, child3.getNode());

        NodePointer child4 = propertyPointer3.getImmediateValuePointer(1);
        assertEquals(propertyPointer3, child4.getParentPointer());
        assertEquals(value2, child4.getNode());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "property";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());

        Object value = new Object();
        propertyPointer.setValue(value);
        PropertyPointer propertyPointer2 = propertyPointer.createPath(null);
        assertEquals(propertyPointer, propertyPointer2);
        assertEquals(value, propertyPointer2.getNode());

        QName name = new QName(null, "property");
        NodePointer child = propertyPointer.createChild(null, name, 0, value);
        assertEquals(propertyPointer, child.getParentPointer());
        assertEquals(value, child.getNode());

        NodePointer context = null;
        propertyPointer2 = propertyPointer.createPath(context);
        assertEquals(propertyPointer, propertyPointer2);
        assertEquals(value, propertyPointer2.getNode());

        context = new JXPathContext();
        propertyPointer2 = propertyPointer.createPath(context);
        assertEquals(propertyPointer, propertyPointer2);
        assertEquals(value, propertyPointer2.getNode());

        Object value2 = new Object();
        propertyPointer.setValue(value2);
        propertyPointer2 = propertyPointer.createPath(context, value);
        assertEquals(propertyPointer, propertyPointer2);
        assertNotSame(value, propertyPointer2.getNode());
        assertEquals(value2, propertyPointer2.getNode());

        propertyPointer.setValue(value);
        propertyPointer2 = propertyPointer.createPath(context, value2);
        assertEquals(propertyPointer, propertyPointer2);
        assertNotSame(value, propertyPointer2.getNode());
        assertNotSame(value2, propertyPointer2.getNode());

        propertyPointer.setValue(null);
        assertNull(propertyPointer.getNode());
        propertyPointer2 = propertyPointer.createPath(context, value2);
        assertEquals(propertyPointer, propertyPointer2);
        assertNull(propertyPointer2.getNode());
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            @Override
            public String getPropertyName() {
                return "property";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());

        Object value = new Object();
        propertyPointer.setValue(value);
        PropertyPointer propertyPointer2 = propertyPointer.createChild(null, null, 0, value);
        assertEquals(propertyPointer, propertyPointer2.getParentPointer());
        assertEquals(value, propertyPointer2.getNode());

        QName name = new QName(null, "property");
        PropertyPointer propertyPointer3 = propertyPointer.createChild(null, name, 0, value);
        assertEquals(propertyPointer, propertyPointer3.getParentPointer());
        assertEquals(name.toString(), propertyPointer3.getPropertyName());
        assertEquals(value, propertyPointer3.getNode());

        NodePointer context = null;
        propertyPointer2 = propertyPointer.createChild(context, null, 0, value);
        assertEquals(propertyPointer, propertyPointer2.getParentPointer());
        assertEquals(value, propertyPointer2.getNode());

        context = new JXPathContext();
        propertyPointer2 = propertyPointer.createChild(context, null, 0, value);
        assertEquals(propertyPointer, propertyPointer2.getParentPointer());
        assertEquals(value, propertyPointer2.getNode());

        propertyPointer.setValue(null);
        assertNull(propertyPointer.getNode());
        propertyPointer2 = propertyPointer.createChild(context, null, 0, value);
        assertEquals(propertyPointer, propertyPointer2.getParentPointer());
        assertNull(propertyPointer2.getNode());

        propertyPointer.setValue(value);
        propertyPointer2 = propertyPointer.createChild(context, null, 0, value2);
        assertEquals(propertyPointer, propertyPointer2.getParentPointer());
        assertNotSame(value, propertyPointer2.getNode());
        assertEquals(value2, propertyPointer2.getNode());

        propertyPointer.setValue(value2);
        propertyPointer2 = propertyPointer.createChild(context, null, 0, value);
        assertEquals(propertyPointer, propertyPointer2.getParentPointer());
        assertNotSame(value, propertyPointer2.getNode());
        assertNotSame(value2, propertyPointer2.getNode());
    }
}