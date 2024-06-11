package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContextWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(null, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return false;
            }

            @Override
            public boolean nextNode() {
                return false;
            }
        };

        EvalContext contexts[] = new EvalContext[0];

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNull(unionContext.getNodeSet());
        assertFalse(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testUnionContextWithSingleContext() {
        EvalContext parentContext = new EvalContext(null, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext context = new EvalContext(parentContext, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext contexts[] = new EvalContext[]{context};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        List<NodePointer> pointers = new ArrayList<>();
        while (unionContext.nextSet()) {
            while (unionContext.nextNode()) {
                NodePointer ptr = unionContext.getCurrentNodePointer();
                pointers.add(ptr);
            }
        }

        assertEquals(1, pointers.size());
        assertNotNull(pointers.get(0));
    }

    @Test
    public void testUnionContextWithMultipleContexts() {
        EvalContext parentContext = new EvalContext(null, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext context1 = new EvalContext(parentContext, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext context2 = new EvalContext(parentContext, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        List<NodePointer> pointers = new ArrayList<>();
        while (unionContext.nextSet()) {
            while (unionContext.nextNode()) {
                NodePointer ptr = unionContext.getCurrentNodePointer();
                pointers.add(ptr);
            }
        }

        assertEquals(2, pointers.size());
        List<NodePointer> expectedPointers = Arrays.asList(
                context1.getCurrentNodePointer(), context2.getCurrentNodePointer()
        );
        assertTrue(pointers.containsAll(expectedPointers));
    }

    @Test
    public void testUnionContextWithMultipleContextsAndPosition() {
        EvalContext parentContext = new EvalContext(null, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext context1 = new EvalContext(parentContext, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext context2 = new EvalContext(parentContext, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        List<NodePointer> pointers = new ArrayList<>();
        unionContext.nextSet();
        unionContext.nextNode(); // skip first node from context1
        pointers.add(unionContext.getCurrentNodePointer());

        assertEquals(1, pointers.size());
        List<NodePointer> expectedPointers = Arrays.asList(context2.getCurrentNodePointer());
        assertTrue(pointers.containsAll(expectedPointers));

        unionContext.nextSet();
        unionContext.nextNode(); // skip first node from context1
        pointers.clear();

        while (unionContext.nextNode()) {
            NodePointer ptr = unionContext.getCurrentNodePointer();
            pointers.add(ptr);
        }

        assertEquals(2, pointers.size());
        List<NodePointer> expectedPointers2 = Arrays.asList(
                context1.getCurrentNodePointer(), context2.getCurrentNodePointer()
        );
        assertTrue(pointers.containsAll(expectedPointers2));
    }

    @Test
    public void testUnionContextWithMultipleContextsAndMixedPositions() {
        EvalContext parentContext = new EvalContext(null, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext context1 = new EvalContext(parentContext, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext context2 = new EvalContext(parentContext, new BasicNodeSet()) {
            @Override
            public boolean nextSet() {
                return true;
            }

            @Override
            public boolean nextNode() {
                return true;
            }
        };

        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        List<NodePointer> pointers1 = new ArrayList<>();
        unionContext.nextSet();
        unionContext.nextNode(); // skip first node from context1
        pointers1.add(unionContext.getCurrentNodePointer());

        List<NodePointer> pointers2 = new ArrayList<>();
        unionContext.nextSet();
        pointers2.addAll(Arrays.asList(unionContext.getCurrentNodePointer(), unionContext.getCurrentNodePointer()));

        assertEquals(3, pointers1.size() + pointers2.size());
        List<NodePointer> expectedPointers = Arrays.asList(
                context1.getCurrentNodePointer(), context2.getCurrentNodePointer(), context1.getCurrentNodePointer(), context2.getCurrentNodePointer()
        );
        assertTrue(pointers1.containsAll(Arrays.asList(context2.getCurrentNodePointer())) && pointers2.containsAll(expectedPointers.subList(1, expectedPointers.size())));
    }
}