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
    public void testUnionContext_constructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        assertEquals(parentContext, unionContext.getParentContext());
        assertEquals(2, unionContext.contexts.length);
        assertFalse(unionContext.isPrepared());
        assertSame(new BasicNodeSet(), unionContext.getNodeSet());
    }

    @Test
    public void testUnionContext_getDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testUnionContext_setPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        List<NodePointer> nodeSet = new ArrayList<>();
        nodeSet.add(new NodePointer(new Object(), 0));
        nodeSet.add(new NodePointer(new Object(), 1));
        context1.setNodeSet(new BasicNodeSet(nodeSet));
        List<NodePointer> nodeSet2 = new ArrayList<>();
        nodeSet2.add(new NodePointer(new Object(), 2));
        context2.setNodeSet(new BasicNodeSet(nodeSet2));

        unionContext.setPosition(0);
        assertTrue(unionContext.isPrepared());

        List<NodePointer> unionNodeSet = ((BasicNodeSet) unionContext.getNodeSet()).getNodes();
        assertEquals(3, unionNodeSet.size());
        assertSame(nodeSet.get(0), unionNodeSet.get(0));
        assertSame(nodeSet.get(0), unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        assertSame(nodeSet.get(1), unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        assertSame(nodeSet2.get(0), unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        assertNull(unionContext.getCurrentNodePointer());

        unionContext.nextSet();
        assertTrue(unionContext.isPrepared());
        unionNodeSet = ((BasicNodeSet) unionContext.getNodeSet()).getNodes();
        assertEquals(3, unionNodeSet.size());
        assertSame(nodeSet2.get(0), unionNodeSet.get(2));
    }

    @Test
    public void testUnionContext_nextSet() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        List<NodePointer> nodeSet1 = new ArrayList<>();
        nodeSet1.add(new NodePointer(new Object(), 0));
        nodeSet1.add(new NodePointer(new Object(), 1));
        context1.setNodeSet(new BasicNodeSet(nodeSet1));

        List<NodePointer> nodeSet2 = new ArrayList<>();
        nodeSet2.add(new NodePointer(new Object(), 2));
        nodeSet2.add(new NodePointer(new Object(), 3));
        context2.setNodeSet(new BasicNodeSet(nodeSet2));

        unionContext.nextSet();
        List<NodePointer> unionNodeSet1 = ((BasicNodeSet) unionContext.getNodeSet()).getNodes();
        assertEquals(2, unionNodeSet1.size());
        assertSame(nodeSet1.get(0), unionNodeSet1.get(0));
        assertSame(nodeSet1.get(1), unionNodeSet1.get(1));

        unionContext.nextSet();
        List<NodePointer> unionNodeSet2 = ((BasicNodeSet) unionContext.getNodeSet()).getNodes();
        assertEquals(2, unionNodeSet2.size());
        assertSame(nodeSet2.get(0), unionNodeSet2.get(0));
        assertSame(nodeSet2.get(1), unionNodeSet2.get(1));
    }

    @Test
    public void testUnionContext_nextSet_afterLastSet() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        List<NodePointer> nodeSet1 = new ArrayList<>();
        nodeSet1.add(new NodePointer(new Object(), 0));
        nodeSet1.add(new NodePointer(new Object(), 1));
        context1.setNodeSet(new BasicNodeSet(nodeSet1));

        List<NodePointer> nodeSet2 = new ArrayList<>();
        nodeSet2.add(new NodePointer(new Object(), 2));
        nodeSet2.add(new NodePointer(new Object(), 3));
        context2.setNodeSet(new BasicNodeSet(nodeSet2));

        unionContext.nextSet();
        List<NodePointer> unionNodeSet1 = ((BasicNodeSet) unionContext.getNodeSet()).getNodes();
        assertEquals(2, unionNodeSet1.size());
        assertSame(nodeSet1.get(0), unionNodeSet1.get(0));
        assertSame(nodeSet1.get(1), unionNodeSet1.get(1));

        unionContext.nextSet();
        List<NodePointer> unionNodeSet2 = ((BasicNodeSet) unionContext.getNodeSet()).getNodes();
        assertEquals(2, unionNodeSet2.size());
        assertSame(nodeSet2.get(0), unionNodeSet2.get(0));
        assertSame(nodeSet2.get(1), unionNodeSet2.get(1));

        unionContext.nextSet();
        assertNull(unionContext.getNodeSet());
    }
}