package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getName().getPrefix());
        assertEquals("*", nullPropertyPointer.getName().getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(5);
    }
    
    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
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
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName())), nullPropertyPointer.getValuePointer());
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
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }
    
    @Test(expected=JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }
    
    @Test
    public void testCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createPath(null));
    }
    
    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createPath(null, "value"));
    }
    
    @Test
    public void testCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createChild(null, null, 0));
    }
    
    @Test
    public void testCreateChildWithValueTest() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createChild(null, null, 0, "value"));
    }
    
    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertTrue(nullPropertyPointer.isByNameAttribute());
        assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyNames().length);
    }
    
    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("[@name='']", nullPropertyPointer.asPath());
    }
    
    @Test
    public void testEscape() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(" ", nullPropertyPointer.escape(" "));
    }
}