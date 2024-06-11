package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(new QName("*"), nullPropertyPointer.getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        // Test the method without any assertion as it has no return value
        nullPropertyPointer.setPropertyIndex(0);
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
        assertEquals(true, nullPropertyPointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NullPointer nullPointer = new NullPointer(null, new QName(nullPropertyPointer.getPropertyName()));
        assertEquals(nullPointer, nullPropertyPointer.getValuePointer());
    }
    
    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(false, nullPropertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(false, nullPropertyPointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(true, nullPropertyPointer.isContainer());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }
    
    @Test
    public void testCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(nullPropertyPointer, nullPropertyPointer.createPath(null));
    }
    
    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createPath(null, "value"));
    }
    
    @Test
    public void testCreateChildWithIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createChild(null, new QName(nullPropertyPointer.getPropertyName()), 0));
    }
    
    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createChild(null, new QName(nullPropertyPointer.getPropertyName()), 0, "value"));
    }
    
    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("propertyName");
        assertEquals("propertyName", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertEquals(true, nullPropertyPointer.isAttributeNameValue());
    }
    
    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(false, nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] expected = new String[0];
        assertEquals(expected, nullPropertyPointer.getPropertyNames());
    }
    
    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        StringBuilder buffer = new StringBuilder();
        buffer.append(nullPropertyPointer.getImmediateParentPointer().asPath());
        buffer.append("[@name='");
        buffer.append(nullPropertyPointer.escape(nullPropertyPointer.getPropertyName()));
        buffer.append("']");
        if (nullPropertyPointer.getIndex() != NullPropertyPointer.WHOLE_COLLECTION) {
            buffer.append('[').append(nullPropertyPointer.getIndex() + 1).append(']');
        }
        assertEquals(buffer.toString(), nullPropertyPointer.asPath());
    }
    

}