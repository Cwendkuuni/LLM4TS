package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.SimplePropertyPointer;

public class PropertyPointerTest {

    @Test
    public void testSimplePropertyPointer() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        assertEquals("testProperty", propertyPointer.getPropertyName());
        assertNull(propertyPointer.getBean());
        assertEquals(Integer.MIN_VALUE, propertyPointer.getPropertyIndex());

        propertyPointer.setBean(new Object());
        assertSame(new Object(), propertyPointer.getBean());

        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // empty implementation
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
                return true;
            }
        };

        propertyPointer.setPropertyName("testProperty2");
    }

    @Test
    public void testGetName() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetLength() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        propertyPointer.setBaseValue(new String[]{"test1", "test2"});
        assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateNode() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        propertyPointer.setBaseValue(new String[]{"test1", "test2"});
        assertEquals("test1", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePath() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        propertyPointer.setBaseValue(new String[]{"test1", "test2"});
        NodePointer nodePointer = propertyPointer.createPath(null);
        assertEquals("testProperty[1]", nodePointer.asPath());
    }

    @Test
    public void testCreatePathWithValue() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        NodePointer nodePointer = propertyPointer.createPath(null, "test3");
        assertEquals("testProperty[\"test3\"]", nodePointer.asPath());
    }

    @Test
    public void testCreateChild() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        PropertyPointer childPropertyPointer = propertyPointer.createChild(null, "testChildProperty");
        assertEquals("testProperty/testChildProperty", childPropertyPointer.asPath());
    }

    @Test
    public void testCreateChildWithValue() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        PropertyPointer childPropertyPointer = propertyPointer.createChild(null, "testChildProperty", 1);
        assertEquals("testProperty/testChildProperty[1]", childPropertyPointer.asPath());
    }

    @Test
    public void testIsCollection() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        propertyPointer.setBaseValue(new String[]{"test1", "test2"});
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new Object(), "testProperty");
        propertyPointer.setBaseValue("test1");
        assertFalse(propertyPointer.isCollection());
    }
}