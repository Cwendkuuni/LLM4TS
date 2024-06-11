package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testUnionContextEmpty() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(new Object[]{}), null);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertEquals(0, unionContext.size());
        assertSame(parentContext, unionContext.getParentContext());
    }

    @Test
    public void testUnionContextSingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(new Object[]{}), null);
        EvalContext contextA = new EvalContext(new BasicNodeSet(new Object[]{"a"}), null);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA});

        assertEquals(1, unionContext.size());
        assertSame(contextA, unionContext.getContexts()[0]);
        assertSame(parentContext, unionContext.getParentContext());
    }

    @Test
    public void testUnionContextMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(new Object[]{}), null);
        EvalContext contextA = new EvalContext(new BasicNodeSet(new Object[]{"a"}), null);
        EvalContext contextB = new EvalContext(new BasicNodeSet(new Object[]{"b"}), null);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        assertEquals(2, unionContext.size());
        assertSame(contextA, unionContext.getContexts()[0]);
        assertSame(contextB, unionContext.getContexts()[1]);
        assertSame(parentContext, unionContext.getParentContext());
    }

    @Test
    public void testUnionContextDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(new Object[]{}), null);
        EvalContext contextA = new EvalContext(new BasicNodeSet(new Object[]{"a"}), null);
        EvalContext contextB = new EvalContext(new BasicNodeSet(new Object[]{"b"}), null);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testUnionContextSetPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(new Object[]{}), null);
        EvalContext contextA = new EvalContext(new BasicNodeSet(new Object[]{"a", "b"}), null);
        EvalContext contextB = new EvalContext(new BasicNodeSet(new Object[]{"c", "d"}), null);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(contextA.getPointerFor("a"));
        expectedNodes.add(contextA.getPointerFor("b"));
        expectedNodes.add(contextB.getPointerFor("c"));
        expectedNodes.add(contextB.getPointerFor("d"));

        for (int i = 0; i < expectedNodes.size(); i++) {
            NodePointer nodePointer = unionContext.nextNode();
            assertSame(expectedNodes.get(i), nodePointer);
            assertFalse(unionContext.nextNodeHasNext());
        }

        assertNull(unionContext.nextNode());
    }
}