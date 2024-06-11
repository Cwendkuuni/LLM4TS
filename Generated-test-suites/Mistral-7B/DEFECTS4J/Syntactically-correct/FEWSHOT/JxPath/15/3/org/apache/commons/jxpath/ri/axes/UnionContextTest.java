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
    public void testUnionContext() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();

        BasicNodeSet nodeSetA = new BasicNodeSet();
        BasicNodeSet nodeSetB = new BasicNodeSet();

        nodeSetA.add(new NodePointer(1));
        nodeSetA.add(new NodePointer(2));

        nodeSetB.add(new NodePointer(2));
        nodeSetB.add(new NodePointer(3));

        EvalContext[] contexts = new EvalContext[]{contextA, contextB};
        UnionContext unionContext = new UnionContext(null, contexts);

        assertEquals(3, unionContext.getSize());
        assertEquals(new NodePointer(1), unionContext.getNodeSet().get(0));
        assertEquals(new NodePointer(2), unionContext.getNodeSet().get(1));
        assertEquals(new NodePointer(3), unionContext.getNodeSet().get(2));
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_validPosition() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();

        BasicNodeSet nodeSetA = new BasicNodeSet();
        BasicNodeSet nodeSetB = new BasicNodeSet();

        nodeSetA.add(new NodePointer(1));
        nodeSetA.add(new NodePointer(2));

        nodeSetB.add(new NodePointer(2));
        nodeSetB.add(new NodePointer(3));

        EvalContext[] contexts = new EvalContext[]{contextA, contextB};
        UnionContext unionContext = new UnionContext(null, contexts);

        unionContext.setPosition(0);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodeSetA.get(0)));

        unionContext.next();
        assertTrue(unionContext.getCurrentNodePointer().equals(nodeSetA.get(1)));

        unionContext.next();
        assertTrue(unionContext.getCurrentNodePointer().equals(nodeSetB.get(0)));

        unionContext.next();
        assertTrue(unionContext.getCurrentNodePointer().equals(nodeSetB.get(1)));

        unionContext.next();
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_invalidPosition() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();

        BasicNodeSet nodeSetA = new BasicNodeSet();
        BasicNodeSet nodeSetB = new BasicNodeSet();

        nodeSetA.add(new NodePointer(1));
        nodeSetA.add(new NodePointer(2));

        nodeSetB.add(new NodePointer(2));
        nodeSetB.add(new NodePointer(3));

        EvalContext[] contexts = new EvalContext[]{contextA, contextB};
        UnionContext unionContext = new UnionContext(null, contexts);

        unionContext.setPosition(3);
        assertFalse(unionContext.hasNext());
    }
}

class MockEvalContext extends EvalContext {
    @Override
    public boolean nextSet() {
        return true;
    }

    @Override
    public boolean nextNode() {
        return true;
    }
}