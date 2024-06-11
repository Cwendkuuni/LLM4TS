package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer1 = new NodePointer();
        NodePointer nodePointer2 = new NodePointer();
        NodePointer nodePointer3 = new NodePointer();
        BasicNodeSet nodeSet1 = new BasicNodeSet();
        nodeSet1.add(nodePointer1);
        nodeSet1.add(nodePointer2);
        BasicNodeSet nodeSet2 = new BasicNodeSet();
        nodeSet2.add(nodePointer2);
        nodeSet2.add(nodePointer3);
        EvalContext evalContext1 = new EvalContext();
        evalContext1.setNodeSet(nodeSet1);
        EvalContext evalContext2 = new EvalContext();
        evalContext2.setNodeSet(nodeSet2);
        EvalContext[] contexts = {evalContext1, evalContext2};
        UnionContext unionContext = new UnionContext(new EvalContext(), contexts);
        
         unionContext.setPosition(1);
        assertNotNull(unionContext.getNodeSet());
    }
}