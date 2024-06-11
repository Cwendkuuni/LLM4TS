package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    private NullPropertyPointer nullPropertyPointer;
    
    @Test
    public void testGetName() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(new QName("*"), nullPropertyPointer.getName());
    }
    
    @Test
    public void testGetPropertyIndex() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(1);
        // No assertion can be made as this method does nothing
    }
    
    @Test
    public void testGetLength() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(new NullPointer(nullPropertyPointer, new QName("*")), nullPropertyPointer.getValuePointer());
    }
    
    @Test
    public void testIsActualProperty() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isContainer());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParent() {
        nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNonNullParent() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("value");
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = JXPathContext.newContext(null);
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(nullPropertyPointer, nullPropertyPointer.createPath(context));
    }
    
    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.createPath(context, "value"));
    }
    
    @Test
    public void testCreateChildWithoutValue() {
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("child");
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.createChild(context, name, 1));
    }
    
    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("child");
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.createChild(context, name, 1, "value"));
    }
    
    @Test
    public void testGetPropertyName() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("newName");
        assertEquals("newName", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("newAttributeValue");
        assertEquals("newAttributeValue", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
    }
    
    @Test
    public void testAsPath() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("nullPointer[@name='*']", nullPropertyPointer.asPath());
    }
    
    @Test
    public void testEscape() {
        NullPointer parent = new NullPointer(null, new QName(""));
        nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("string", nullPropertyPointer.escape("string"));
    }
}