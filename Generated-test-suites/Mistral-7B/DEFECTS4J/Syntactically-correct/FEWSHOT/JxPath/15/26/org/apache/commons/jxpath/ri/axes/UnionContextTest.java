package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    private EvalContext context1, context2, parentContext;
    private UnionContext unionContext;

    @org.junit.Before
    public void setUp() {
        context1 = new TestEvalContext();
        context2 = new TestEvalContext();
        parentContext = new TestEvalContext();
    }

    @org.junit.Test
    public void testUnionContextEmptyContexts() {
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        assertTrue(unionContext.isEmpty());
    }

    @org.junit.Test
    public void testUnionContextOneContext() {
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1});
        assertFalse(unionContext.isEmpty());
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @org.junit.Test
    public void testUnionContextMultipleContexts() {
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertFalse(unionContext.isEmpty());
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @org.junit.Test
    public void testUnionContextGetCurrentNodePointer() {
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context2.setCurrentNodePointer(new NodePointer(2, 1));

        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertTrue(unionContext.nextSet());
        NodePointer nodePointer1 = unionContext.getCurrentNodePointer();
        assertSame(context1.getCurrentNodePointer(), nodePointer1);
        assertTrue(unionContext.nextSet());
        NodePointer nodePointer2 = unionContext.getCurrentNodePointer();
        assertSame(context2.getCurrentNodePointer(), nodePointer2);
        assertFalse(unionContext.nextSet());
    }

    @org.junit.Test
    public void testUnionContextSetPosition() {
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context2.setCurrentNodePointer(new NodePointer(2, 1));

        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.setPosition(0);
        NodePointer nodePointer1 = unionContext.getCurrentNodePointer();
        assertSame(context1.getCurrentNodePointer(), nodePointer1);

        unionContext.setPosition(1);
        NodePointer nodePointer2 = unionContext.getCurrentNodePointer();
        assertSame(context2.getCurrentNodePointer(), nodePointer2);

        unionContext.setPosition(2);
        assertNull(unionContext.getCurrentNodePointer());
    }

    @org.junit.Test
    public void testUnionContextSetPositionOutOfBounds() {
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context2.setCurrentNodePointer(new NodePointer(2, 1));

        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.setPosition(3);
        assertNull(unionContext.getCurrentNodePointer());
    }

    @org.junit.Test
    public void testUnionContextReset() {
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context2.setCurrentNodePointer(new NodePointer(2, 1));

        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.setPosition(0);
        NodePointer nodePointer1 = unionContext.getCurrentNodePointer();
        assertSame(context1.getCurrentNodePointer(), nodePointer1);

        unionContext.reset();
        assertNull(unionContext.getCurrentNodePointer());
    }

    @org.junit.Test
    public void testUnionContextNextSet() {
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context2.setCurrentNodePointer(new NodePointer(2, 1));

        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextSet());
        assertFalse(unionContext.nextSet());
    }

    static class TestEvalContext extends EvalContext {
        @Override
        public boolean nextNode() {
            return false;
        }

        @Override
        public boolean nextSet() {
            return true;
        }

        @Override
        public NodePointer getCurrentNodePointer() {
            return new NodePointer(0, 0);
        }

        @Override
        public NodePointer getRootNodePointer() {
            return new NodePointer(0, 0);
        }

        @Override
        public boolean isInRootScope() {
            return true;
        }

        @Override
        public void setCurrentNodePointer(NodePointer nodePointer) {
            super.setCurrentNodePointer(nodePointer);
        }
    }
}