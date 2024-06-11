import org.apache.commons.jxpath.Axis;
import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContextConstruction() {
        EvaluationContext parentContext = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context1 = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context2 = new EvaluationContext(new BasicNodeSet(), null);

        UnionContext unionContext = new UnionContext(parentContext, new EvaluationContext[] {context1, context2});

        Assert.assertArrayEquals(new int[] {Axis.CHILD, Axis.DESCENDANT, Axis.ANCESTOR, Axis.ANCESTOR_OR_SELF, Axis.ATTRIBUTE, Axis.PARENT, Axis.SELF, Axis.FOLLOWING, Axis.FOLLOWING_SIBLING, Axis.PRECEDING, Axis.PRECEDING_SIBLING}, unionContext.getSupportedAxes());
        Assert.assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetDocumentOrder() {
        EvaluationContext parentContext = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context1 = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context2 = new EvaluationContext(new BasicNodeSet(), null);

        UnionContext unionContext = new UnionContext(parentContext, new EvaluationContext[] {context1, context2});

        unionContext.setPosition(0);
        unionContext.nextSet();

        Assert.assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testUnionContextPositioning() {
        EvaluationContext parentContext = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context1 = new EvaluationContext(new BasicNodeSet(), Arrays.asList(new NodePointer[] {new NodePointer("n1"), new NodePointer("n2"), new NodePointer("n3")}));
        EvaluationContext context2 = new EvaluationContext(new BasicNodeSet(), Arrays.asList(new NodePointer[] {new NodePointer("n4"), new NodePointer("n5"), new NodePointer("n3")}));

        UnionContext unionContext = new UnionContext(parentContext, new EvaluationContext[] {context1, context2});

        unionContext.setPosition(0);
        unionContext.nextSet();

        List<NodePointer> expectedOrder = new ArrayList<>();
        expectedOrder.add(context1.getCurrentNodePointer());
        expectedOrder.add(context2.getCurrentNodePointer());

        List<NodePointer> actualOrder = new ArrayList<>();
        while (unionContext.nextNode()) {
            actualOrder.add(unionContext.getCurrentNodePointer());
        }

        Assert.assertEquals(expectedOrder.size(), actualOrder.size());
        for (int i = 0; i < expectedOrder.size(); i++) {
            Assert.assertEquals(expectedOrder.get(i), actualOrder.get(i));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testUnionContextPositioningAfterEndOfSet() {
        EvaluationContext parentContext = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context1 = new EvaluationContext(new BasicNodeSet(), Arrays.asList(new NodePointer[] {new NodePointer("n1"), new NodePointer("n2"), new NodePointer("n3")}));
        EvaluationContext context2 = new EvaluationContext(new BasicNodeSet(), Arrays.asList(new NodePointer[] {new NodePointer("n4"), new NodePointer("n5"), new NodePointer("n3")}));

        UnionContext unionContext = new UnionContext(parentContext, new EvaluationContext[] {context1, context2});

        unionContext.setPosition(Integer.MAX_VALUE);
        unionContext.nextSet();

        unionContext.nextNode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextNullContexts() {
        EvaluationContext parentContext = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context1 = null;
        EvaluationContext context2 = new EvaluationContext(new BasicNodeSet(), null);

        try {
            UnionContext unionContext = new UnionContext(parentContext, new EvaluationContext[] {context1, context2});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("contexts cannot be null.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextEmptyContexts() {
        EvaluationContext parentContext = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context1 = new EvaluationContext(new BasicNodeSet(), null);
        EvaluationContext context2 = new EvaluationContext(new BasicNodeSet(), null);

        try {
            UnionContext unionContext = new UnionContext(parentContext, new EvaluationContext[] {context1, context2});
            unionContext.nextSet();
            unionContext.nextNode();

            unionContext = new UnionContext(parentContext, new EvaluationContext[]{});
            unionContext.nextSet();
            unionContext.nextNode();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("contexts cannot be empty.", e.getMessage());
            throw e;
        }
    }
}