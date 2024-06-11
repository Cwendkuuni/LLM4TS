package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        NodePointer parent = new NodePointer();
        QName name = new QName("test", "attr");
        attributeIterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNotNull(nodePointer);
        assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();

        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(1));
        assertTrue(attributeIterator.setPosition(2));
        assertFalse(attributeIterator.setPosition(3));
    }
}