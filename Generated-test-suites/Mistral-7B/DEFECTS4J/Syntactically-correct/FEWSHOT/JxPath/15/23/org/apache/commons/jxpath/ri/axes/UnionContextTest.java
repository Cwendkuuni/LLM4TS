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

    @Test
    public void testUnionContext() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        assertEquals(2, unionContext.contexts.length);
        assertFalse(unionContext.prepared);
        assertEquals(new BasicNodeSet(), unionContext.getNodeSet());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        NodePointer nodeA1 = new NodePointer(new Object(), 1);
        NodePointer nodeA2 = new NodePointer(new Object(), 2);
        NodePointer nodeB1 = new NodePointer(new Object(), 3);

        contextA.setPosition(0);
        contextA.addNode(nodeA1);
        contextA.addNode(nodeA2);

        contextB.setPosition(0);
        contextB.addNode(nodeB1);

        unionContext.setPosition(0);

        ArrayList pointers = ((BasicNodeSet) unionContext.getNodeSet()).asList();
        assertEquals(3, pointers.size());
        assertEquals(nodeA1, pointers.get(0));
        assertEquals(nodeA2, pointers.get(1));
        assertEquals(nodeB1, pointers.get(2));
    }

    @Test
    public void testPrepare() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        unionContext.setPosition(0);

        assertTrue(unionContext.prepared);
    }

    @Test
    public void testSetPositionInvalidIndex() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        assertFalse(unionContext.setPosition(Integer.MAX_VALUE));
    }

    @Test
    public void testSetPositionNegativeIndex() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        assertFalse(unionContext.setPosition(-1));
    }

    @Test
    public void testNextSet() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        unionContext.setPosition(0);

        assertTrue(unionContext.nextSet());
        assertFalse(unionContext.nextSet());

        contextA.addNode(new NodePointer(new Object(), 1));
        contextB.addNode(new NodePointer(new Object(), 2));

        unionContext.setPosition(0);

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertEquals(contextA, unionContext.getCurrentContext());
        assertEquals(new NodePointer(new Object(), 1), unionContext.getCurrentNodePointer());

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertEquals(contextB, unionContext.getCurrentContext());
        assertEquals(new NodePointer(new Object(), 2), unionContext.getCurrentNodePointer());

        assertTrue(unionContext.nextSet());
        assertFalse(unionContext.nextNode());
        assertNull(unionContext.getCurrentContext());
        assertNull(unionContext.getCurrentNodePointer());

        unionContext.setPosition(0);

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertEquals(contextA, unionContext.getCurrentContext());
        assertEquals(new NodePointer(new Object(), 1), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testCurrentContext() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 0;
            }
        };

        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet()) {
            @Override
            public int getDocumentOrder() {
                return 1;
            }
        };

        EvalContext[] contexts = new EvalContext[] { contextA, contextB };
        UnionContext unionContext = new UnionContext(null, contexts);

        NodePointer nodeA1 = new NodePointer(new Object(), 1);
        NodePointer nodeA2 = new NodePointer(new Object(), 2);
        NodePointer nodeB1 = new NodePointer(new Object(), 3);

        contextA.setPosition(0);
        contextA.addNode(nodeA1);
        contextA.addNode(nodeA2);

        contextB.setPosition(0);
        contextB.addNode(nodeB1);

        unionContext.setPosition(0);

        assertSame(contextA, unionContext.getCurrentContext());
        assertNull(unionContext.getCurrentNodePointer());

        unionContext.nextSet();

        assertSame(contextB, unionContext.getCurrentContext());
        assertNull(unionContext.getCurrentNodePointer());

        unionContext.nextNode();

        assertSame(contextB, unionContext.getCurrentContext());
        assertEquals(nodeB1, unionContext.getCurrentNodePointer());
    }
}