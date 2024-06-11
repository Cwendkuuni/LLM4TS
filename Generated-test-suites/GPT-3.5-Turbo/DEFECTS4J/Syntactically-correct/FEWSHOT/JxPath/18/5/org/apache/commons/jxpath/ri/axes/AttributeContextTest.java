package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.AbstractTest;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest extends AbstractTest {
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        JXPathContext context = JXPathContext.newContext(new Object());
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        attributeContext = new AttributeContext(context, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(0));
        assertFalse(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(2));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
}