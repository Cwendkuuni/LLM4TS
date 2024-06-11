package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodesTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("test", "attr"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertSame(parentContext, attributeContext.getParentContext());
        assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionLessThanCurrent() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.setPosition(0));
    }

    @Test
    public void testSetPositionCurrent() {
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(getCurrentPosition()));
        assertSame(attributeContext.getCurrentNodePointer(),
                   iterator.getNodePointer());
    }

    @Test
    public void testSetPositionGreater() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(getCurrentPosition() + 2));
        assertSame(attributeContext.getCurrentNodePointer(),
                   iterator.getNodePointer());
    }

    @Test
    public void testNextNodeNoAttributeFound() {
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeAttributeFound() {
        parentContext.setCurrentNodePointer(new NodePointer(new NodesTest(1), 0));
        iterator = new NodeIterator(null, null, 0, 1, null);
        iterator.getNodePointer(0);
        attributeContext.nextNode();
        assertTrue(attributeContext.nextNode());
        assertSame(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeAttributeFoundWithAttributeTest() {
        parentContext.setCurrentNodePointer(new NodePointer(new NodesTest(1), 0));
        iterator = new NodeIterator(null, new NodeNameTest(new QName("test", "attr")), 0, 1, null);
        iterator.getNodePointer(0);
        attributeContext.nextNode();
        assertTrue(attributeContext.nextNode());
        assertSame(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeDuplicateAttributes() {
        parentContext.setCurrentNodePointer(new NodePointer(new NodesTest(1), 0));
        iterator = new NodeIterator(null, new NodeNameTest(new QName("test", "attr")), 0, 2, null);
        iterator.getNodePointer(0);
        attributeContext.nextNode();
        assertTrue(attributeContext.nextNode());
        assertSame(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
        attributeContext.nextNode(); // should throw an exception
    }

    private int getCurrentPosition() {
        return parentContext.getCurrentPosition();
    }
}