package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.SimpleBean;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        SimpleBean bean = new SimpleBean();
        bean.setName("John Doe");
        bean.setAge(30);
        bean.setAddresses(new String[]{"123 Main St", "456 Elm St"});

        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
            @Override
            public String getPropertyName() {
                return "name";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // No-op
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name", "age", "addresses"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals("John Doe", propertyPointer.getValue());
        assertEquals("John Doe", propertyPointer.getStringValue());
        assertEquals(30, propertyPointer.getImmediateNode().hashCode());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertEquals(1, propertyPointer.getLength());

        PropertyPointer propertyPointer2 = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "age";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // No-op
            }
        };

        assertEquals(30, propertyPointer2.getValue());
        assertEquals(30, propertyPointer2.getIntValue());
        assertNotNull(propertyPointer2.getImmediateValuePointer());
        assertTrue(propertyPointer2.isCollection());
        assertTrue(propertyPointer2.isLeaf());
        assertEquals(-1, propertyPointer2.getLength());

        PropertyPointer propertyPointer3 = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "addresses";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // No-op
            }
        };

        assertNotNull(propertyPointer3.getImmediateValuePointer());
        assertTrue(propertyPointer3.isCollection());
        assertTrue(propertyPointer3.isLeaf());
        assertEquals(2, propertyPointer3.getLength());
    }

    @Test
    public void testPropertyPointerWithArray() {
        SimpleBean bean = new SimpleBean();
        bean.setName("John Doe");
        bean.setAge(30);
        bean.setAddresses(new String[]{"123 Main St", "456 Elm St"});

        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
            @Override
            public String getPropertyName() {
                return "addresses";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // No-op
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name", "age", "addresses"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer1 = propertyPointer.createChild(null, null, 0);
        assertEquals("123 Main St", propertyPointer1.getValue());
        assertEquals("123 Main St", propertyPointer1.getStringValue());
        assertEquals(1, propertyPointer1.getLength());

        PropertyPointer propertyPointer2 = propertyPointer.createChild(null, null, 1);
        assertEquals("456 Elm St", propertyPointer2.getValue());
        assertEquals("456 Elm St", propertyPointer2.getStringValue());
        assertEquals(1, propertyPointer2.getLength());
    }

    @Test
    public void testPropertyPointerWithNull() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // No-op
            }
        };

        assertNull(propertyPointer.getValue());
        assertNull(propertyPointer.getStringValue());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertEquals(-1, propertyPointer.getLength());
    }
}