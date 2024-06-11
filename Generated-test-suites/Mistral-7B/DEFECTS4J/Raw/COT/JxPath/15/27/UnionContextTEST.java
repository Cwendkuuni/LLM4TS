import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
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

        Assert.assertNotNull(unionContext.getNodeSet());
        Assert.assertEquals(3, unionContext.getNodeSet().size());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        Assert.assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_normalCase() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        NodePointer nodePointer1 = context1.getCurrentNodePointer();
        NodePointer nodePointer2 = context2.getCurrentNodePointer();

        unionContext.setPosition(0);
        Assert.assertEquals(nodePointer1, unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        Assert.assertEquals(nodePointer2, unionContext.getCurrentNodePointer());

        unionContext.nextSet();
        Assert.assertFalse(unionContext.hasNextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_notPrepared() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.setPosition(0);
    }

    @Test
    public void testSetPosition_pastEnd() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.nextSet();
        unionContext.nextNode();

        unionContext.setPosition(2);
        Assert.assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testSetPosition_negative() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        unionContext.nextSet();
        unionContext.nextNode();

        unionContext.setPosition(-1);
        Assert.assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testSetPosition_sameContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context, context});

        unionContext.nextSet();
        unionContext.nextNode();

        unionContext.setPosition(0);
        Assert.assertEquals(context.getCurrentNodePointer(), unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();

        unionContext.setPosition(1);
        Assert.assertEquals(context.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_differentContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        context1.nextSet();
        context1.nextNode();

        context2.nextSet();
        context2.nextNode();

        unionContext.nextSet();
        unionContext.nextNode();

        unionContext.setPosition(1);
        Assert.assertEquals(context2.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
    }
}