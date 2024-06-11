import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UnionContextTest is to test the functionality of UnionContext class.
 */
public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        Assert.assertNotNull(unionContext);
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
    public void testSetPositionWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        Assert.assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithOneContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        unionContext.getNodeSet().add(new NodePointer(1, "testNode1"));
        context.getNodeSet().add(new NodePointer(2, "testNode2"));

        Assert.assertTrue(unionContext.setPosition(0));
        Assert.assertEquals(new NodePointer(1, "testNode1"), unionContext.getCurrentNodePointer());

        Assert.assertTrue(unionContext.setPosition(1));
        Assert.assertEquals(new NodePointer(2, "testNode2"), unionContext.getCurrentNodePointer());

        Assert.assertFalse(unionContext.setPosition(2));
    }

    @Test
    public void testSetPositionWithMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        context1.getNodeSet().add(new NodePointer(1, "testNode1"));
        context1.getNodeSet().add(new NodePointer(2, "testNode2"));
        context2.getNodeSet().add(new NodePointer(3, "testNode3"));
        context2.getNodeSet().add(new NodePointer(4, "testNode4"));

        Assert.assertTrue(unionContext.setPosition(0));
        Assert.assertEquals(new NodePointer(1, "testNode1"), unionContext.getCurrentNodePointer());

        Assert.assertTrue(unionContext.setPosition(1));
        Assert.assertEquals(new NodePointer(2, "testNode2"), unionContext.getCurrentNodePointer());

        Assert.assertTrue(unionContext.setPosition(2));
        Assert.assertEquals(new NodePointer(3, "testNode3"), unionContext.getCurrentNodePointer());

        Assert.assertTrue(unionContext.setPosition(3));
        Assert.assertEquals(new NodePointer(4, "testNode4"), unionContext.getCurrentNodePointer());

        Assert.assertFalse(unionContext.setPosition(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithNegativePosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        unionContext.getNodeSet().add(new NodePointer(1, "testNode1"));
        context.getNodeSet().add(new NodePointer(2, "testNode2"));

        unionContext.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithPositionGreaterThanSize() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        unionContext.getNodeSet().add(new NodePointer(1, "testNode1"));
        context.getNodeSet().add(new NodePointer(2, "testNode2"));

        unionContext.setPosition(3);
    }

    @Test
    public void testSetPositionWithSamePositionInMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        context1.getNodeSet().add(new NodePointer(1, "testNode1"));
        context1.getNodeSet().add(new NodePointer(2, "testNode2"));
        context2.getNodeSet().add(new NodePointer(1, "testNode1"));
        context2.getNodeSet().add(new NodePointer(3, "testNode3"));

        Assert.assertTrue(unionContext.setPosition(1));
        Assert.assertEquals(new NodePointer(1, "testNode1"), unionContext.getCurrentNodePointer());

        Assert.assertTrue(unionContext.setPosition(1));
        Assert.assertEquals(new NodePointer(1, "testNode1"), unionContext.getCurrentNodePointer());
    }
}