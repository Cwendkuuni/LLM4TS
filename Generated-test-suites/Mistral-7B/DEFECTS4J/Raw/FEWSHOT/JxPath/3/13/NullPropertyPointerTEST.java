package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertFalse(nullPropertyPointer.isLeaf());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertTrue(nullPropertyPointer.isContainer());
        assertEquals("", nullPropertyPointer.asPath());
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testParentNullPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(mock(NodePointer.class));
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertFalse(nullPropertyPointer.isLeaf());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertTrue(nullPropertyPointer.isContainer());
        assertEquals("", nullPropertyPointer.asPath());
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testParentPropertyOwnerPointer() {
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.isContainer()).thenReturn(true);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        assertNull(nullPropertyPointer.getBaseValue());
        assertSame(parentPointer, nullPropertyPointer.getImmediateNode());
        assertFalse(nullPropertyPointer.isLeaf());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertTrue(nullPropertyPointer.isContainer());
        assertEquals("", nullPropertyPointer.asPath());
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(mock(NodePointer.class));
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(mock(NodePointer.class));
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testCreatePathContextNull() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(mock(NodePointer.class));
        assertNull(nullPropertyPointer.createPath(mock(JXPathContext.class)));
    }

    @Test
    public void testCreatePathContextNotNull() {
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.createPath(mock(JXPathContext.class))).thenReturn(mock(NodePointer.class));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        NodePointer nodePointer = mock(NodePointer.class);
        when(nodePointer.createChild(mock(JXPathContext.class), eq(new QName("test")), anyInt(), anyObject())).thenReturn(mock(NodePointer.class));
        when(nodePointer.createChild(mock(JXPathContext.class), eq(new QName("test")), anyInt(), anyObject(), anyObject())).thenReturn(mock(NodePointer.class));
        NodePointer result = nullPropertyPointer.createPath(mock(JXPathContext.class));
        assertSame(parentPointer.createPath(mock(JXPathContext.class)), result);
    }

    @Test
    public void testCreatePathContextWithValue() {
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.createPath(mock(JXPathContext.class))).thenReturn(mock(NodePointer.class));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        NodePointer nodePointer = mock(NodePointer.class);
        when(nodePointer.createAttribute(mock(JXPathContext.class), eq(new QName("test")))).thenReturn(mock(NodePointer.class));
        NodePointer result = nullPropertyPointer.createPath(mock(JXPathContext.class), "test");
        assertSame(parentPointer.createPath(mock(JXPathContext.class)), result);
    }

    @Test
    public void testCreateChildContextNull() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(mock(NodePointer.class));
        assertNull(nullPropertyPointer.createChild(mock(JXPathContext.class), new QName("test"), anyInt()));
        assertNull(nullPropertyPointer.createChild(mock(JXPathContext.class), new QName("test"), anyInt(), anyObject()));
    }

    @Test
    public void testCreateChildContextNotNull() {
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.createChild(mock(JXPathContext.class), eq(new QName("test")), anyInt())).thenReturn(mock(NodePointer.class));
        when(parentPointer.createChild(mock(JXPathContext.class), eq(new QName("test")), anyInt(), anyObject())).thenReturn(mock(NodePointer.class));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        NodePointer result = nullPropertyPointer.createChild(mock(JXPathContext.class), new QName("test"), 1);
        assertSame(parentPointer.createChild(mock(JXPathContext.class), new QName("test"), 1), result);
    }

    @Test
    public void testCreateChildContextWithValue() {
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.createChild(mock(JXPathContext.class), eq(new QName("test")), anyInt())).thenReturn(mock(NodePointer.class));
        when(parentPointer.createChild(mock(JXPathContext.class), eq(new QName("test")), anyInt(), anyObject())).thenReturn(mock(NodePointer.class));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        NodePointer result = nullPropertyPointer.createChild(mock(JXPathContext.class), new QName("test"), 1, "test");
        assertSame(parentPointer.createChild(mock(JXPathContext.class), new QName("test"), 1, "test"), result);
    }
}