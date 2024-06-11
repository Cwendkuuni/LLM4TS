package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testUnionContextEmptyList() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[0];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertFalse(unionContext.isPrepared());
        assertEquals(0, unionContext.getDocumentOrder());
        assertEquals(0, unionContext.size());
    }

    @Test
    public void testUnionContextOneContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        context.addNode(new NodePointer(1, 1));
        EvalContext contexts[] = new EvalContext[]{context};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.isPrepared());
        assertEquals(1, unionContext.getDocumentOrder());
        assertEquals(1, unionContext.size());
        assertEquals(new NodePointer(1, 1), unionContext.nextNode());
    }

    @Test
    public void testUnionContextMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext ctx1 = new EvalContext(new BasicNodeSet());
        EvalContext ctx2 = new EvalContext(new BasicNodeSet());
        ctx1.addNode(new NodePointer(1, 1));
        ctx1.addNode(new NodePointer(1, 2));
        ctx2.addNode(new NodePointer(2, 1));
        EvalContext contexts[] = new EvalContext[]{ctx1, ctx2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.isPrepared());
        assertEquals(1, unionContext.getDocumentOrder());
        assertEquals(3, unionContext.size());
        assertEquals(new NodePointer(1, 1), unionContext.nextNode());
        assertEquals(new NodePointer(1, 2), unionContext.nextNode());
        assertEquals(new NodePointer(2, 1), unionContext.nextNode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnionContextRemoveNode() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext ctx1 = new EvalContext(new BasicNodeSet());
        EvalContext ctx2 = new EvalContext(new BasicNodeSet());
        ctx1.addNode(new NodePointer(1, 1));
        ctx1.addNode(new NodePointer(1, 2));
        ctx2.addNode(new NodePointer(2, 1));
        EvalContext contexts[] = new EvalContext[]{ctx1, ctx2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.removeNode(new NodePointer(1, 1));
    }
}