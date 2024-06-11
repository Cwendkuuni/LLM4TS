import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testEmptyContextsUnion() {
        EvalContext parentContext = new EvaluationContext(new BasicNodeSet());
        EvalContext context1 = new EvaluationContext(parentContext, new BasicNodeSet());
        EvalContext context2 = new EvaluationContext(parentContext, new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        List<NodePointer> expectedNodes = new ArrayList<>();
        // An empty union of two empty contexts should return an empty node set
        Assert.assertEquals(expectedNodes.size(), unionContext.getNodeSet().size());
    }

    @Test
    public void testSingleNodeInEachContext() {
        EvalContext parentContext = new EvaluationContext(new BasicNodeSet());
        EvalContext context1 = new EvaluationContext(parentContext, new BasicNodeSet());
        context1.add("//node1", new NodePointer(new Object(), 0));

        EvalContext context2 = new EvaluationContext(parentContext, new BasicNodeSet());
        context2.add("//node2", new NodePointer(new Object(), 0));

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        List<NodePointer> expectedNodes = new ArrayList<>(Arrays.asList(context1.getCurrentNodePointer(), context2.getCurrentNodePointer()));
        // A union of two contexts, each containing a single node, should return a node set containing both nodes
        Assert.assertEquals(expectedNodes.size(), unionContext.getNodeSet().size());
        for (int i = 0; i < expectedNodes.size(); i++) {
            Assert.assertEquals(expectedNodes.get(i), unionContext.getNodeSet().get(i));
        }
    }

    @Test
    public void testOverlappingNodes() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(parentContext, new BasicNodeSet());
        context1.add("//node1", new NodePointer(new Object(), 0));
        context1.add("//node2", new NodePointer(new Object(), 1));

        EvalContext context2 = new EvalContext(parentContext, new BasicNodeSet());
        context2.add("//node2", new NodePointer(new Object(), 2));
        context2.add("//node3", new NodePointer(new Object(), 3));

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        List<NodePointer> expectedNodes = new ArrayList<>(Arrays.asList(context1.getCurrentNodePointer(), context2.getCurrentNodePointer().get(1), context2.getCurrentNodePointer().get(2)));
        // A union of two contexts, one containing nodes that overlap with the other, should return a node set containing all unique nodes from both contexts
        Assert.assertEquals(expectedNodes.size(), unionContext.getNodeSet().size());
        for (int i = 0; i < expectedNodes.size(); i++) {
            Assert.assertEquals(expectedNodes.get(i), unionContext.getNodeSet().get(i));
        }
    }

    @Test(expected = EvaluationException.class)
    public void testInvalidPositionInEmptyContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvaluationContext(parentContext, new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1});
        unionContext.getNodeSet().nextSet(); // consume the empty node set

        unionContext.setPosition(-1); // invalid position
    }

    @Test(expected = EvaluationException.class)
    public void testInvalidPositionInNonEmptyContext() {
        EvalContext parentContext = new EvaluationContext(new BasicNodeSet());
        EvalContext context1 = new EvaluationContext(parentContext, new BasicNodeSet());
        context1.add("//node1", new NodePointer(new Object(), 0));

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1});

        unionContext.setPosition(unionContext.getNodeSet().size()); // invalid position
    }
}