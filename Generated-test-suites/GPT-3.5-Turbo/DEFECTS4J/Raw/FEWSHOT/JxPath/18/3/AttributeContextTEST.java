package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributeContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertEquals(false, attributeContext.isSetStarted());
        assertEquals(null, attributeContext.getIterator());
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.setSetStarted(true);
        attributeContext.setIterator(new NodeIterator());
        attributeContext.setCurrentNodePointer(new NodePointer());

        attributeContext.reset();

        assertEquals(false, attributeContext.isSetStarted());
        assertEquals(null, attributeContext.getIterator());
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_currentPositionLessThanPosition() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(5);

        assertEquals(5, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition_currentPositionGreaterThanPosition() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(5);
        attributeContext.setPosition(3);

        assertEquals(3, attributeContext.getCurrentPosition());
    }
    
    @Test
    public void testNextNode_notStartedAndNodeTestNotInstanceOfNodeNameTest() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setSetStarted(false);

        assertEquals(false, attributeContext.nextNode());
    }

    @Test
    public void testNextNode_iteratorIsNull() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setSetStarted(true);

        assertEquals(false, attributeContext.nextNode());
    }

    @Test
    public void testNextNode_iteratorSetPositionFalse() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setSetStarted(true);
        attributeContext.setIterator(new NodeIterator());

        assertEquals(false, attributeContext.nextNode());
    }

    @Test
    public void testNextNode_iteratorSetPositionTrue() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setSetStarted(true);
        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        attributeContext.setIterator(iterator);

        assertEquals(true, attributeContext.nextNode());
    }
}