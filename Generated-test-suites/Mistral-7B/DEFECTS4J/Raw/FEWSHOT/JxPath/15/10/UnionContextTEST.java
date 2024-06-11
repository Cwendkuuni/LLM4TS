package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testUnionContext_EmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        EvalContext contexts[] = new EvalContext[0];

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertFalse(unionContext.isSet());
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testUnionContext_SingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        EvalContext context = new EvalContext(new BasicNodeSet(), null);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        context.nextSet();
        context.nextNode();
        NodePointer nodePointer = context.getCurrentNodePointer();

        unionContext.nextSet();
        assertTrue(unionContext.isSet());
        assertEquals(1, unionContext.getDocumentOrder());
        assertEquals(nodePointer, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContext_MultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        EvalContext context1 = new EvalContext(new BasicNodeSet(), null);
        EvalContext context2 = new EvalContext(new BasicNodeSet(), null);

        unionContexts:
        {
            UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

            context1.nextSet();
            context1.nextNode();
            NodePointer nodePointer1 = context1.getCurrentNodePointer();

            context2.nextSet();
            context2.nextNode();
            NodePointer nodePointer2 = context2.getCurrentNodePointer();

            unionContext.nextSet();
            assertTrue(unionContext.isSet());
            assertEquals(1, unionContext.getDocumentOrder());

            int position = 0;
            while (unionContext.nextNode()) {
                NodePointer currentNodePointer = unionContext.getCurrentNodePointer();
                if (currentNodePointer.equals(nodePointer1)) {
                    position++;
                }
                if (currentNodePointer.equals(nodePointer2)) {
                    position++;
                }
            }
            assertEquals(2, position);
        }
    }
}