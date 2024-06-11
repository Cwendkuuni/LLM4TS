package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private JXPathContext context;
    
    @Before
    public void setup() {
        context = JXPathContext.newContext(null);
        propertyPointer = new PropertyPointerStub(null);
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(2);
        assertEquals(2, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(3);
        assertEquals(3, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNull(propertyPointer.getBean());
        propertyPointer.setBean("test");
        assertEquals("test", propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        QName expected = new QName(null, propertyPointer.getPropertyName());
        assertEquals(expected, propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        assertNotNull(propertyPointer.getPropertyName());
        propertyPointer.setPropertyName("test");
        assertEquals("test", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("property");
        assertEquals("property", propertyPointer.getPropertyName());
        propertyPointer.setPropertyName("newProperty");
        assertEquals("newProperty", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(0, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] expected = {};
        assertEquals(expected, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
        propertyPointer.setBean("test");
        assertTrue(propertyPointer.isActual());
        propertyPointer.setBean(null);
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
        propertyPointer.setBean(ValueUtils.createList(1, 2, 3));
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
        propertyPointer.setBean("test");
        assertTrue(propertyPointer.isLeaf());
        propertyPointer.setBean(null);
        assertTrue(propertyPointer.isLeaf());
        propertyPointer.setBean(ValueUtils.createMap("key", "value"));
        assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertEquals(0, propertyPointer.getLength());
        propertyPointer.setBean(ValueUtils.createList(1, 2, 3));
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePathWithNullBaseValue() {
        propertyPointer.createPath(context);
        assertNotNull(propertyPointer.getNode());
    }

    @Test
    public void testCreatePathWithNonNullBaseValue() {
        propertyPointer.setBean("test");
        propertyPointer.createPath(context);
        assertTrue(propertyPointer.isActual());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathWithAbstractFactoryException() {
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreatePathWithValue() {
        propertyPointer.setBean(ValueUtils.createList(1, 2, 3));
        propertyPointer.createPath(context, "test");
        assertTrue(propertyPointer.isActual());
        assertEquals("test", propertyPointer.getNode());
    }

    @Test
    public void testCreateChildWithValue() {
        QName name = new QName("", "property");
        PropertyPointer child = (PropertyPointer) propertyPointer.createChild(context, name, 0, "test");
        assertNotNull(child);
        assertTrue(propertyPointer.isActual());
        assertEquals("property", propertyPointer.getPropertyName());
        assertEquals("test", propertyPointer.getNode());
    }

    @Test
    public void testCreateChildWithoutValue() {
        QName name = new QName("", "property");
        PropertyPointer child = (PropertyPointer) propertyPointer.createChild(context, name, 0);
        assertNotNull(child);
        assertTrue(propertyPointer.isActual());
        assertEquals("property", propertyPointer.getPropertyName());
        assertNull(propertyPointer.getNode());
    }

    private class PropertyPointerStub extends PropertyPointer {

        public PropertyPointerStub(PropertyPointer parent) {
            super(parent);
        }

        @Override
        public String getPropertyName() {
            return "property";
        }

        @Override
        public void setPropertyName(String propertyName) {
            // Do nothing
        }

        @Override
        public int getPropertyCount() {
            return 0;
        }

        @Override
        public String[] getPropertyNames() {
            return new String[]{};
        }

        @Override
        protected boolean isActualProperty() {
            return true;
        }

        @Override
        protected void setValue(Object value) {
            // Do nothing
        }

        @Override
        protected AbstractFactory getAbstractFactory(JXPathContext context) {
            return null;
        }
    }

}