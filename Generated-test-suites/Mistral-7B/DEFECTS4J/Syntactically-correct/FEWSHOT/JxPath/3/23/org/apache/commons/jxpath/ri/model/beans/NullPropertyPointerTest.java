package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

import java.util.Arrays;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName name = nullPropertyPointer.getName();
        assertEquals(new QName("*"), name);
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String newPropertyName = "newName";
        nullPropertyPointer.setPropertyName(newPropertyName);
        assertEquals(newPropertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parent = mock(NodePointer.class);
        when(parent.createPath(context)).thenReturn(parent);

        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertSame(parent, nodePointer);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parent = mock(NodePointer.class);
        when(parent.createPath(context)).thenReturn(parent);

        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = "value";
        NodePointer nodePointer = nullPropertyPointer.createPath(context, value);
        assertSame(parent, nodePointer.getParentPointer());
        assertEquals(value, nodePointer.getValue());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testSetValueWithParent() {
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parent = mock(NodePointer.class);
        when(parent.isContainer()).thenReturn(true);
        when(parent.createPath(context)).thenReturn(parent);

        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
        assertEquals(value, parent.getPropertyPointer().getValue());
    }

    @Test
    public void testCreatePathWithAttribute() {
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parent = mock(NodePointer.class);
        when(parent.createAttribute(context, new QName("name"))).thenReturn(parent);

        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer nodePointer = nullPropertyPointer.createPath(context, new QName("name"));
        assertSame(parent, nodePointer);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("/", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointerWithAttribute = new NullPropertyPointer(null);
        nullPropertyPointerWithAttribute.setPropertyName("name");
        nullPropertyPointerWithAttribute.setNameAttributeValue("value");
        nullPropertyPointerWithAttribute.setIndex(0);
        assertEquals("/[@name='value']", nullPropertyPointerWithAttribute.asPath());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parent = mock(NodePointer.class);
        when(parent.createPath(context)).thenReturn(parent);

        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("name");
        NodePointer child = nullPropertyPointer.createChild(context, name);
        NodePointer expectedChild = parent.createChild(context, name, 0);
        assertSame(expectedChild, child);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parent = mock(NodePointer.class);
        when(parent.createPath(context)).thenReturn(parent);

        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("name");
        Object value = new Object();
        NodePointer child = nullPropertyPointer.createChild(context, name, 0, value);
        NodePointer expectedChild = parent.createChild(context, name, 0, value);
        assertSame(expectedChild, child);
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        int propertyCount = nullPropertyPointer.getPropertyCount();
        assertEquals(0, propertyCount);
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testEscape() {
        String string = "&'\"";
        String escapedString = NullPropertyPointer.escape(string);
        String expectedEscapedString = "&amp;&apos;&quot;";
        assertEquals(expectedEscapedString, escapedString);
    }
}