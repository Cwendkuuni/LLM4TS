package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodeSetContext;

import java.util.ArrayList;

public class UnionContextTest {
    @Test
    public void testUnionContextCreation() {
        EvalContext contextA = new EvalContext(new BasicNodeSet(), new Object()[0], new Object()[0]);
        EvalContext contextB = new EvalContext(new BasicNodeSet(), new Object()[0], new Object()[0]);
        UnionContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), null), new EvalContext[]{contextA, contextB});

        assertNotNull(unionContext.getContexts);
        assertEquals(2, unionContext.getContexts.length);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new EvalContext(new BasicNodeSet(), new Object()[0], new Object()[0]);
        EvalContext contextB = new EvalContext(new BasicNodeSet(), new Object()[0], new Object()[0]);
        UnionContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), null), new EvalContext[]{contextA, contextB});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_nextSet_noNextNode_firstContext() {
        EvalContext contextA = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]) {
            @Override
            public boolean nextSet() {
                return true;
            }
        };
        EvalContext contextB = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]);
        UnionContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), null), new EvalContext[]{contextA, contextB});

        unionContext.setPosition(0);
        assertTrue(contextA.nextSet());
        assertFalse(contextB.nextSet());
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testSetPosition_nextSet_noNextNode_secondContext() {
        EvalContext contextA = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]) {
            @Override
            public boolean nextSet() {
                return false;
            }
        };
        EvalContext contextB = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]) {
            @Override
            public boolean nextSet() {
                return true;
            }
        };
        UnionContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), null), new EvalContext[]{contextA, contextB});

        unionContext.setPosition(0);
        assertFalse(contextA.nextSet());
        assertTrue(contextB.nextSet());
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testSetPosition_nextSet_hasNextNode_firstContext() {
        EvalContext contextA = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };
        EvalContext contextB = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]);
        UnionContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), null), new EvalContext[]{contextA, contextB});

        unionContext.setPosition(0);
        assertTrue(contextA.nextSet());
        assertTrue(contextA.nextNode());
        assertFalse(contextB.nextSet());
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testSetPosition_nextSet_hasNextNode_secondContext() {
        EvalContext contextA = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]);
        EvalContext contextB = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };
        UnionContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), null), new EvalContext[]{contextA, contextB});

        unionContext.setPosition(0);
        assertFalse(contextA.nextSet());
        assertTrue(contextB.nextSet());
        assertTrue(contextB.nextNode());
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testSetPosition_noNextSet_null() {
        EvalContext contextA = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]);
        EvalContext contextB = new EvalContext(new ArrayList<>(), new Object()[0], new Object()[0]);
        UnionContext unionContext = new UnionContext(new NodeSetContext(new BasicNodeSet(), null), new EvalContext[]{contextA, contextB});

        unionContext.setPosition(0);
        assertNull(unionContext.getCurrentNodePointer());
    }
}