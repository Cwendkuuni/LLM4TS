
package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext(new JXPathContext(), new NullPointer());
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentPointer = attributeContext.getCurrentNodePointer();

        assertNull(currentPointer);
    }

    @Test
    public void testReset() {
        attributeContext.setCurrentPosition(5);
        attributeContext.reset();

        assertEquals(0, attributeContext.getCurrentPosition());
        assertEquals(false, attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNotNull(attributeContext.getParentContext());
    }

    @Test
    public void testSetPosition() {
        attributeContext.setPosition(2);

        assertEquals(2, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.isSetStarted());
        assertNotNull(attributeContext.getIterator());
    }

    @Test
    public void testSetPosition_Invalid() {
        attributeContext.setPosition(5);

        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
    }

    @Test
    public void testNextNode() {
        attributeContext.setStarted(false);
        attributeContext.setParentContext(new EvalContext(new JXPathContext(), new NullPointer()));

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_NoIterator() {
        attributeContext.setStarted(false);
        attributeContext.setParentContext(new EvalContext(new JXPathContext(), new NullPointer()));
        attributeContext.setIterator(null);

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}
