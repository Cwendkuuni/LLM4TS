package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {
    
    private NullPropertyPointer nullPropertyPointer;
    
    @Before
    public void setUp() {
        NodePointer parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }
    
    @Test
    public void testGetName() {
        assertEquals(new QName("*"), nullPropertyPointer.getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        assertEquals(0, nullPropertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testGetLength() {
        assertEquals(0, nullPropertyPointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        assertNull(nullPropertyPointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNull(nullPropertyPointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        assertTrue(nullPropertyPointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertNotNull(valuePointer);
        assertTrue(valuePointer instanceof NullPointer);
        assertEquals(new QName("*"), valuePointer.getName());
    }
    
    @Test
    public void testIsActualProperty() {
        assertFalse(nullPropertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertFalse(nullPropertyPointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        assertTrue(nullPropertyPointer.isContainer());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        nullPropertyPointer.setValue("test");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = new NodePointer();
        parent.setContainer(false);
        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue("test");
    }
    
    @Test
    public void testCreatePath() {
        NodePointer newParent = new NodePointer();
        JXPathContext context = new JXPathContext();
        NodePointer path = nullPropertyPointer.createPath(context);
        assertNotNull(path);
        assertTrue(path instanceof NullPointer);
        assertNotNull(path.getParent());
        assertEquals(newParent, path.getParent());
        assertEquals(new QName("*"), path.getName());
    }
    
    @Test
    public void testCreatePathWithValue() {
        NodePointer newParent = new NodePointer();
        JXPathContext context = new JXPathContext();
        Object value = "test";
        NodePointer path = nullPropertyPointer.createPath(context, value);
        assertNotNull(path);
        assertTrue(path instanceof NullPointer);
        assertNotNull(path.getParent());
        assertEquals(newParent, path.getParent());
        assertEquals(new QName("*"), path.getName());
        assertEquals(value, path.getValue());
    }
    
    @Test
    public void testCreateChildWithoutIndex() {
        NodePointer newParent = new NodePointer();
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        NodePointer child = nullPropertyPointer.createChild(context, name);
        assertNotNull(child);
        assertFalse(child.isAttribute());
        assertEquals(name, child.getName());
        assertEquals(newParent, child.getParent());
        assertEquals(context, child.getContext());
    }
    
    @Test
    public void testCreateChildWithIndex() {
        NodePointer newParent = new NodePointer();
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 1;
        NodePointer child = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(child);
        assertFalse(child.isAttribute());
        assertEquals(name, child.getName());
        assertEquals(newParent, child.getParent());
        assertEquals(context, child.getContext());
        assertEquals(index, child.getIndex());
    }
    
    @Test
    public void testCreateChildWithValue() {
        NodePointer newParent = new NodePointer();
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 1;
        Object value = "test";
        NodePointer child = nullPropertyPointer.createChild(context, name, index, value);
        assertNotNull(child);
        assertFalse(child.isAttribute());
        assertEquals(name, child.getName());
        assertEquals(newParent, child.getParent());
        assertEquals(context, child.getContext());
        assertEquals(index, child.getIndex());
        assertEquals(value, child.getValue());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }
    
    @Test
    public void testIsCollection() {
        assertFalse(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        assertNull(nullPropertyPointer.getPropertyNames());
    }
    
    @Test
    public void testAsPath() {
        String path = nullPropertyPointer.asPath();
        assertNotNull(path);
        assertTrue(path.isEmpty());
    }
    
    @Test
    public void testEscape() {
        String string = "test'";
        String escapedString = nullPropertyPointer.escape(string);
        assertEquals("test&apos;", escapedString);
    }
}