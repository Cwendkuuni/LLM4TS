import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        Assert.assertNotNull(unionContext.getContexts);
        Assert.assertEquals(2, unionContext.getContexts.length);
        Assert.assertFalse(unionContext.isPrepared());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        Assert.assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_emptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        Assert.assertFalse(unionContext.isPrepared());
        Assert.assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_singleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        context.add(new NodePointer(1));
        context.add(new NodePointer(2));

        unionContext.setPosition(1);

        Assert.assertTrue(unionContext.isPrepared());
        Assert.assertEquals(2, unionContext.size());
        Assert.assertEquals(new NodePointer(2), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_multipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));

        context2.add(new NodePointer(2));
        context2.add(new NodePointer(3));

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.setPosition(1);

        Assert.assertTrue(unionContext.isPrepared());
        Assert.assertEquals(3, unionContext.size());
        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new NodePointer(2));
        expectedNodes.add(new NodePointer(1));
        expectedNodes.add(new NodePointer(3));
        Assert.assertArrayEquals(expectedNodes.toArray(), unionContext.getNodes().toArray());
        Assert.assertEquals(new NodePointer(2), unionContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextConstructor_emptyParentContext() {
        EvalContext parentContext = null;
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        new UnionContext(parentContext, new EvalContext[]{context1, context2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextConstructor_nullContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext[] contexts = new EvalContext[0];

        new UnionContext(parentContext, contexts);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_alreadyPrepared() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});
        unionContext.setPosition(0);

        Assert.assertTrue(unionContext.isPrepared());

        unionContext.setPosition(1);
    }

    @Test
    public void testUnionContext_multipleContextsWithDuplicateNodes() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context1.add(new NodePointer(3));

        context2.add(new NodePointer(2));
        context2.add(new NodePointer(3));
        context2.add(new NodePointer(4));

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.setPosition(1);

        Assert.assertTrue(unionContext.isPrepared());
        Assert.assertEquals(4, unionContext.size());
        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new NodePointer(2));
        expectedNodes.add(new NodePointer(1));
        expectedNodes.add(new NodePointer(3));
        expectedNodes.add(new NodePointer(4));
        Assert.assertArrayEquals(expectedNodes.toArray(), unionContext.getNodes().toArray());
        Assert.assertEquals(new NodePointer(2), unionContext.getCurrentNodePointer());
    }
}