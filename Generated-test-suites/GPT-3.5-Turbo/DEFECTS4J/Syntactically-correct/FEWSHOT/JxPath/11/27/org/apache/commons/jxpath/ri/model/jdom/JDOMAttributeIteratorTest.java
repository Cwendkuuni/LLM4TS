package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private NodePointer parent;
    
    @Before
    public void setup() {
        // mock parent node
        parent = new NodePointer();
    }
    
    @Test
    public void testSingleNodeAttributeMatch() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr"));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertEquals(parent, iterator.getNodePointer().getParent());
        // assert attribute value
    }
    
    @Test
    public void testMultipleNodeAttributeMatch() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertEquals(parent, iterator.getNodePointer().getParent());
        // assert attribute value
    }
    
    @Test
    public void testNoNodeAttributeMatch() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("nonexistent"));
        assertFalse(iterator.setPosition(1));
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }
}