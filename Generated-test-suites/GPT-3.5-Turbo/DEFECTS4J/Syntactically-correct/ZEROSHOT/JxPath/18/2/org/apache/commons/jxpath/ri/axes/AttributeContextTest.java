package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, null);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        super.reset();
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
}