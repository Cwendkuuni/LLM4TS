import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContextWithEmptyContexts() {
        EvalContext parentContext = new StubEvalContext();
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        List<NodePointer> expectedNodePointers = new ArrayList<>();

        Assert.assertEquals(0, unionContext.getNodeSet().size());

        unionContext.setPosition(0);

        Assert.assertFalse(unionContext.nextNode());

        unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        contextA.setNextSet(true);
        contextA.setNextNode(new StubNodePointer(1));
        contextA.nextNode();

        contextB.setNextSet(true);
        contextB.setNextNode(new StubNodePointer(2));
        contextB.nextNode();

        unionContext.setPosition(0);

        Assert.assertTrue(unionContext.nextNode());
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        expectedNodePointers.add(nodePointer);

        Assert.assertEquals(nodePointer.getNode(), contextA.getCurrentNode().getNode());

        Assert.assertTrue(unionContext.nextNode());
        nodePointer = unionContext.getCurrentNodePointer();
        expectedNodePointers.add(nodePointer);

        Assert.assertEquals(nodePointer.getNode(), contextB.getCurrentNode().getNode());

        Assert.assertFalse(unionContext.nextNode());

        unionContext.setPosition(1);

        Assert.assertTrue(unionContext.nextNode());
        nodePointer = unionContext.getCurrentNodePointer();
        Assert.assertEquals(nodePointer, expectedNodePointers.get(1));

        Assert.assertFalse(unionContext.nextNode());

        unionContext = new UnionContext(parentContext, new EvalContext[]{contextB, contextA});

        unionContext.setPosition(0);

        Assert.assertTrue(unionContext.nextNode());
        nodePointer = unionContext.getCurrentNodePointer();
        Assert.assertEquals(nodePointer, expectedNodePointers.get(1));

        Assert.assertTrue(unionContext.nextNode());
        nodePointer = unionContext.getCurrentNodePointer();
        Assert.assertEquals(nodePointer, expectedNodePointers.get(0));

        Assert.assertFalse(unionContext.nextNode());
    }

    @Test
    public void testUnionContextWithSingleContext() {
        EvalContext parentContext = new StubEvalContext();
        EvalContext context = new StubEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        unionContext.setPosition(0);

        context.setNextSet(true);
        context.setNextNode(new StubNodePointer(1));
        context.nextNode();

        unionContext.nextNode();

        Assert.assertEquals(1, unionContext.getNodeSet().size());
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(unionContext.getNodeSet().size(), 1);
    }

    @Test(expected = AssertionError.class)
    public void testUnionContextWithNullContexts() {
        EvalContext parentContext = new StubEvalContext();
        EvalContext contextA = null;
        EvalContext contextB = new StubEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
    }

    @Test(expected = AssertionError.class)
    public void testUnionContextWithNullParentContext() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        EvalContext parentContext = null;

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
    }

    private static class StubEvalContext extends EvalContext {
        @Override
        public boolean nextSet() {
            return true;
        }

        @Override
        public boolean nextNode() {
            return true;
        }

        @Override
        public NodePointer getCurrentNodePointer() {
            return new StubNodePointer(1);
        }
    }

    private static class StubNodePointer implements NodePointer {
        private final int nodeId;

        public StubNodePointer(int nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public int getNodeId() {
            return nodeId;
        }

        @Override
        public JXPathContext getContext() {
            return null;
        }
    }
}