package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContextCreation() {
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(new MockEvalContext(), new EvalContext[]{context1, context2});

        assertNotNull(unionContext);
        assertEquals(2, unionContext.contexts.length);
        assertFalse(unionContext.prepared);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(new MockEvalContext(), new EvalContext[]{context1, context2});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_success() {
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();
        UnionContext unionContext = new UnionContext(new MockEvalContext(), new EvalContext[]{context1, context2});

        List<NodePointer> nodes1 = Arrays.asList(new NodePointer(1), new NodePointer(2));
        List<NodePointer> nodes2 = Arrays.asList(new NodePointer(3), new NodePointer(4));

        for (int i = 0; i < 4; i++) {
            if (context1.nextSet()) {
                context1.nextNode();
                context1.setCurrentNodePointer(nodes1.get(i % 2));
            }
            if (context2.nextSet()) {
                context2.nextNode();
                context2.setCurrentNodePointer(nodes2.get(i % 2));
            }

            unionContext.setPosition(i);
            assertTrue(unionContext.getCurrentNodePointer().isPresent());
        }
    }

    @Test
    public void testSetPosition_outOfBounds() {
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();
        UnionContext unionContext = new UnionContext(new MockEvalContext(), new EvalContext[]{context1, context2});

        List<NodePointer> nodes1 = Arrays.asList(new NodePointer(1), new NodePointer(2));
        List<NodePointer> nodes2 = Arrays.asList(new NodePointer(3), new NodePointer(4));

        for (int i = 0; i < 4; i++) {
            if (context1.nextSet()) {
                context1.nextNode();
                context1.setCurrentNodePointer(nodes1.get(i % 2));
            }
            if (context2.nextSet()) {
                context2.nextNode();
                context2.setCurrentNodePointer(nodes2.get(i % 2));
            }

            unionContext.setPosition(5);
            assertNull(unionContext.getCurrentNodePointer());
        }
    }

    @Test
    public void testSetPosition_prepared() {
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();
        UnionContext unionContext = new UnionContext(new MockEvalContext(), new EvalContext[]{context1, context2});

        List<NodePointer> nodes1 = Arrays.asList(new NodePointer(1), new NodePointer(2));
        List<NodePointer> nodes2 = Arrays.asList(new NodePointer(3), new NodePointer(4));

        for (int i = 0; i < 4; i++) {
            if (context1.nextSet()) {
                context1.nextNode();
                context1.setCurrentNodePointer(nodes1.get(i % 2));
            }
            if (context2.nextSet()) {
                context2.nextNode();
                context2.setCurrentNodePointer(nodes2.get(i % 2));
            }

            unionContext.setPosition(i);
            assertTrue(unionContext.getCurrentNodePointer().isPresent());
        }

        unionContext.setPosition(5);
        assertNull(unionContext.getCurrentNodePointer());

        // Prepare the UnionContext
        unionContext.setPosition(0);

        unionContext.setPosition(5);
        assertNull(unionContext.getCurrentNodePointer());
    }
}

class MockEvalContext implements EvalContext {
    private List<NodePointer> currentNodes = new ArrayList<>();

    @Override
    public boolean nextSet() {
        return currentNodes.isEmpty() ? false : true;
    }

    @Override
    public boolean nextNode() {
        return !currentNodes.isEmpty();
    }

    @Override
    public NodePointer getCurrentNodePointer() {
        return currentNodes.isEmpty() ? null : currentNodes.remove(0);
    }
}