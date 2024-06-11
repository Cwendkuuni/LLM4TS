import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testEmptyContextsUnion() {
        EvalContext contextA = new EmptyContext();
        EvalContext contextB = new EmptyContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {contextA, contextB});

        List<NodePointer> expectedNodePointers = new ArrayList<>();
        Assert.assertEquals(expectedNodePointers.size(), unionContext.getNodeSet().size());
        Assert.assertFalse(unionContext.hasNextSet());
    }

    @Test
    public void testSingleNodeInFirstContext() {
        EvalContext contextA = new SingleNodeContext(new NodePointer(1, "root", "nodeA"));
        EvalContext contextB = new EmptyContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {contextA, contextB});

        List<NodePointer> expectedNodePointers = new ArrayList<>();
        expectedNodePointers.add(new NodePointer(1, "root", "nodeA"));

        for (int i = 0; i < unionContext.getNodeSet().size(); i++) {
            NodePointer nodePointer = unionContext.getNodeSet().getNodePointer(i);
            expectedNodePointers.contains(nodePointer);
            unionContext.nextSet();
        }

        Assert.assertEquals(expectedNodePointers.size(), unionContext.getNodeSet().size());
        Assert.assertFalse(unionContext.hasNextSet());
    }

    @Test
    public void testSingleNodeInSecondContext() {
        EvalContext contextA = new EmptyContext();
        EvalContext contextB = new SingleNodeContext(new NodePointer(1, "root", "nodeB"));
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {contextA, contextB});

        List<NodePointer> expectedNodePointers = new ArrayList<>();
        expectedNodePointers.add(new NodePointer(1, "root", "nodeB"));

        for (int i = 0; i < unionContext.getNodeSet().size(); i++) {
            NodePointer nodePointer = unionContext.getNodeSet().getNodePointer(i);
            expectedNodePointers.contains(nodePointer);
            unionContext.nextSet();
        }

        Assert.assertEquals(expectedNodePointers.size(), unionContext.getNodeSet().size());
        Assert.assertFalse(unionContext.hasNextSet());
    }

    @Test
    public void testMultipleNodesInBothContexts() {
        EvalContext contextA = new MultipleNodesContext(new NodePointer[] {
                new NodePointer(1, "root", "nodeA1"),
                new NodePointer(1, "root", "nodeA2")
        });
        EvalContext contextB = new MultipleNodesContext(new NodePointer[] {
                new NodePointer(1, "root", "nodeB1"),
                new NodePointer(1, "root", "nodeB2"),
                new NodePointer(1, "root", "nodeB3")
        });
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {contextA, contextB});

        List<NodePointer> expectedNodePointers = new ArrayList<>();
        expectedNodePointers.addAll(List.of(
                new NodePointer(1, "root", "nodeA1"),
                new NodePointer(1, "root", "nodeA2"),
                new NodePointer(1, "root", "nodeB1"),
                new NodePointer(1, "root", "nodeB2"),
                new NodePointer(1, "root", "nodeB3")
        ));

        for (int i = 0; i < unionContext.getNodeSet().size(); i++) {
            NodePointer nodePointer = unionContext.getNodeSet().getNodePointer(i);
            expectedNodePointers.contains(nodePointer);
            unionContext.nextSet();
        }

        Assert.assertEquals(expectedNodePointers.size(), unionContext.getNodeSet().size());
        Assert.assertFalse(unionContext.hasNextSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidPositionWhenNoSets() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
        unionContext.nextSet();
        unionContext.nextNode();
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidPositionWhenNoMoreNodesInCurrentSet() {
        EvalContext context = new SingleNodeContext(new NodePointer(1, "root", "node"));
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {context});
        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
    }
}

class EmptyContext extends EvalContext {
    @Override
    public boolean nextSet() {
        return false;
    }

    @Override
    public boolean nextNode() {
        return false;
    }
}

class SingleNodeContext extends EvalContext {
    private NodePointer nodePointer;

    public SingleNodeContext(NodePointer nodePointer) {
        this.nodePointer = nodePointer;
    }

    @Override
    public boolean nextSet() {
        return true;
    }

    @Override
    public NodePointer getCurrentNodePointer() {
        return nodePointer;
    }
}

class MultipleNodesContext extends EvalContext {
    private NodePointer[] nodePointers;
    private int index = 0;

    public MultipleNodesContext(NodePointer[] nodePointers) {
        this.nodePointers = nodePointers;
    }

    @Override
    public boolean nextSet() {
        return true;
    }

    @Override
    public NodePointer getCurrentNodePointer() {
        return nodePointers[index];
    }

    @Override
    public boolean nextNode() {
        if (index < nodePointers.length - 1) {
            index++;
            return true;
        }
        return false;
    }
}