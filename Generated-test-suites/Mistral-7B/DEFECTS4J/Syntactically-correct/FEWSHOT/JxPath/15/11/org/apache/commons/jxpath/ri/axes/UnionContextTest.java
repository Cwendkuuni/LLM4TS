package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;

import java.util.ArrayList;
import java.util.List;

public class UnionContextTest {

    private EvalContext createMockEvalContext() {
        class MockEvalContext extends EvalContext {
            private int nextSetCounter = 0;
            private int nextNodeCounter = 0;

            @Override
            public boolean nextSet() {
                nextSetCounter++;
                return true;
            }

            @Override
            public boolean nextNode() {
                nextNodeCounter++;
                return true;
            }

            @Override
            public NodePointer getCurrentNodePointer() {
                return new NodePointer(new Object(), 0);
            }

            public int getNextSetCounter() {
                return nextSetCounter;
            }

            public int getNextNodeCounter() {
                return nextNodeCounter;
            }
        }

        return new MockEvalContext();
    }

    @Test
    public void testUnionContextEmptyContexts() {
        EvalContext parentContext = createMockEvalContext();
        EvalContext contexts[] = new EvalContext[0];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(0, unionContext.contexts.length);
        assertTrue(unionContext.getDocumentOrder() == 0);
    }

    @Test
    public void testUnionContextOneContext() {
        EvalContext parentContext = createMockEvalContext();
        EvalContext context = createMockEvalContext();
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});
        assertEquals(1, unionContext.contexts.length);
        assertTrue(unionContext.getDocumentOrder() == 0);
    }

    @Test
    public void testUnionContextMultipleContexts() {
        EvalContext parentContext = createMockEvalContext();
        EvalContext context1 = createMockEvalContext();
        EvalContext context2 = createMockEvalContext();
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertEquals(2, unionContext.contexts.length);
        assertTrue(unionContext.getDocumentOrder() == 1);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = createMockEvalContext();
        EvalContext context1 = createMockEvalContext();
        EvalContext context2 = createMockEvalContext();
        NodeSetContext nodeSetContextMock = new NodeSetContext() {
            private int nextNodeCounter = 0;

            @Override
            public NodePointer getCurrentNodePointer() {
                return new NodePointer(new Object(), nextNodeCounter++);
            }

            @Override
            public int getSize() {
                return 5;
            }
        };

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        // Test that the first context is iterated first
        context1.nextSet();
        unionContext.setPosition(0);
        assertEquals(context1.getNextNodeCounter(), unionContext.getNodeSet().getCurrentNodePointer().getNodeIndex());

        // Test that the second context is iterated next
        context2.nextSet();
        unionContext.nextSet();
        unionContext.setPosition(1);
        assertEquals(context2.getNextNodeCounter(), unionContext.getNodeSet().getCurrentNodePointer().getNodeIndex());

        // Test that the first context is iterated again
        context1.nextSet();
        unionContext.nextSet();
        unionContext.setPosition(2);
        assertEquals(context1.getNextNodeCounter(), unionContext.getNodeSet().getCurrentNodePointer().getNodeIndex());

        // Test that the position is reset when the contexts array is empty
        unionContext.contexts = new EvalContext[0];
        unionContext.setPosition(3);
        assertNull(unionContext.getNodeSet().getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWhenFirstContextIsEmpty() {
        EvalContext parentContext = createMockEvalContext();
        EvalContext context1 = new EvalContext(new BasicNodeSet()) {
            @Override
            public boolean nextNode() {
                return false;
            }
        };
        EvalContext context2 = createMockEvalContext();
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        context2.nextSet();
        unionContext.nextSet();
        unionContext.setPosition(0);
        assertFalse(unionContext.getNodeSet().getCurrentNodePointer().isNull());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextNullContexts() {
        EvalContext parentContext = createMockEvalContext();
        UnionContext unionContext = new UnionContext(parentContext, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextEmptyParentContext() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
    }
}