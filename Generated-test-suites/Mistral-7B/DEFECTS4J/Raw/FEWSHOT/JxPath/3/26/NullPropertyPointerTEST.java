package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

public class NullPropertyPointerTest {
    
    @Test
    public void testNullPropertyPointer_new() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertNull(pointer.getPropertyName());
        assertFalse(pointer.isActual());
        assertFalse(pointer.isActualProperty());
        assertFalse(pointer.isContainer());
        assertNull(pointer.getImmediateNode());
        assertNull(pointer.getValuePointer());
    }
    
    @Test
    public void testNullPropertyPointer_getName() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setPropertyName("name");
        
        assertEquals("name", pointer.getPropertyName());
    }

    @Test
    public void testNullPropertyPointer_setNameAttributeValue() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setNameAttributeValue("name");
        
        assertEquals("name", pointer.getPropertyName());
        assertTrue(pointer.isByNameAttribute());
    }

    @Test
    public void testNullPropertyPointer_isCollection() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        assertFalse(pointer.isCollection());
    }

    @Test
    public void testNullPropertyPointer_getPropertyCount() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testNullPropertyPointer_getPropertyNames() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        String[] expected = new String[0];
        
        assertArrayEquals(expected, pointer.getPropertyNames());
    }

    @Test
    public void testNullPropertyPointer_asPath() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setPropertyName("name");
        String expected = "/name";

        assertEquals(expected, pointer.asPath());
    }

    @Test
    public void testNullPropertyPointer_asPath_withNameAttribute() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        pointer.setNameAttributeValue("name");
        pointer.setIndex(0);
        String expected = "/@name[1]";

        assertEquals(expected, pointer.asPath());
    }

    @Test
    public void testNullPropertyPointer_createPath() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = null;

        NodePointer newParent = null;
        if (context != null) {
            newParent = pointer.createPath(context);
        }
        
        assertNull(newParent);
    }

    @Test
    public void testNullPropertyPointer_createPath_PropertyOwnerPointer() {
        NodePointer parent = new PropertyOwnerPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = null;
        PropertyOwnerPointer newParent = null;

        if (context != null) {
            newParent = (PropertyOwnerPointer) pointer.createPath(context);
        }
        
        PropertyPointer propertyPointer = newPropertyPointer(newPropertyName("name"), newPropertyOwnerPointer(parent), context);
        assertSame(propertyPointer, newParent);
    }

    @Test
    public void testNullPropertyPointer_createPath_NullPointer() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = null;
        NodePointer newParent = null;

        if (context != null) {
            newParent = pointer.createPath(context);
        }
        
        assertNull(newParent);
    }

    @Test
    public void testNullPropertyPointer_setValue() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);

        try {
            pointer.setValue(new Object());
            fail();
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }

    @Test
    public void testNullPropertyPointer_createChild() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = null;
        QName name = new QName("name");

        NodePointer newChild = null;
        if (context != null) {
            newChild = pointer.createChild(context, name, 0);
        }

        assertNull(newChild);
    }

    @Test
    public void testNullPropertyPointer_createChild_Value() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = null;
        QName name = new QName("name");
        Object value = new Object();

        NodePointer newChild = null;
        if (context != null) {
            newChild = pointer.createChild(context, name, 0, value);
        }

        assertNull(newChild);
    }

    @Test
    public void testNullPropertyPointer_createChild_Index() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = null;
        QName name = new QName("name");
        int index = 0;

        NodePointer newChild = null;
        if (context != null) {
            newChild = pointer.createChild(context, name, index);
        }

        assertNull(newChild);
    }

    @Test
    public void testNullPropertyPointer_createChild_Value_Index() {
        NodePointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = null;
        QName name = new QName("name");
        Object value = new Object();
        int index = 0;

        NodePointer newChild = null;
        if (context != null) {
            newChild = pointer.createChild(context, name, index, value);
        }

        assertNull(newChild);
    }
}