package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName qname = nullPropertyPointer.getName();
        assertEquals("*", qname.getLocalPart());
        assertEquals("", qname.getNamespaceURI());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(1, 1));
        QName qname1 = nullPropertyPointer1.getName();
        assertEquals("*", qname1.getLocalPart());
        assertEquals("", qname1.getNamespaceURI());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("test");
        QName qname = nullPropertyPointer.getName();
        assertEquals("test", qname.getLocalPart());
        assertEquals("", qname.getNamespaceURI());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(1, 1));
        assertTrue(nullPropertyPointer1.isContainer());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(1, 1));
        assertFalse(nullPropertyPointer1.isLeaf());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(1, 1));
        assertFalse(nullPropertyPointer1.isActual());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(1, 1));
        assertFalse(nullPropertyPointer1.isActualProperty());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(1);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createPath(new JXPathContext());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createPath(new JXPathContext(), null);
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer child = nullPropertyPointer.createChild(context, new QName("test"), 1);
        assertNull(child);
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer child = nullPropertyPointer.createChild(context, new QName("test"), 1, "test");
        assertNull(child);
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(1, 1));
        assertEquals("//*[1]", nullPropertyPointer1.asPath());

        NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(new NodePointer(1, 1));
        nullPropertyPointer2.setPropertyName("test");
        assertEquals("/../*[name='test']", nullPropertyPointer2.asPath());

        NullPropertyPointer nullPropertyPointer3 = new NullPropertyPointer(new NodePointer(1, 1));
        nullPropertyPointer3.setPropertyName("test");
        nullPropertyPointer3.setNameAttributeValue("color");
        assertEquals("/../*[@name='test' and self::node()/@name='color']", nullPropertyPointer3.asPath());
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(1, 1));
        assertEquals("*", nullPropertyPointer1.getPropertyName());

        NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(new NodePointer(1, 1));
        nullPropertyPointer2.setPropertyName("test");
        assertEquals("test", nullPropertyPointer2.getPropertyName());

        NullPropertyPointer nullPropertyPointer3 = new NullPropertyPointer(new NodePointer(1, 1));
        nullPropertyPointer3.setNameAttributeValue("color");
        assertEquals("color", nullPropertyPointer3.getPropertyName());
    }

    @Test
    public void testEscape() {
        String test = "'test'\"test\"";
        assertEquals("&apos;test&quot;test&quot;", NullPropertyPointer.escape(test));
    }
}