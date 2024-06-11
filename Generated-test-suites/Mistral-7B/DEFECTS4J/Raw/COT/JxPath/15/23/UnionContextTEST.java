import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UnionContextTest class contains JUnit tests for UnionContext class
 */
public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);

        Assert.assertNotNull(unionContext);
        Assert.assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);

        unionContext.setPosition(0);
        Assert.assertEquals(1, unionContext.getDocumentOrder());

        unionContext.setPosition(1);
        Assert.assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPositionWithPreparedContext() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);

        unionContext.setPosition(0);
        unionContext.setPosition(1);
        unionContext.setPosition(2); // out of bounds

        // Test with prepared context
        unionContext.prepareContext();
        unionContext.setPosition(contextsLength(unionContext) + 1); // out of bounds

        // Test with not prepared context
        unionContext.setPosition(contextsLength(unionContext) + 1); // out of bounds
        Assert.assertFalse(unionContext.isPrepared());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithEmptyContext() {
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(new EvalContext[0], context);
        unionContext.setPosition(0);
    }

    @Test
    public void testSetPositionWithSingleContext() {
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(new EvalContext[]{context}, null);

        unionContext.setPosition(0);
        unionContext.setPosition(1);
        unionContext.setPosition(contextsLength(unionContext) + 1); // out of bounds
    }

    @Test(expected = AssertionError.class)
    public void testSetPositionWithNullContext() {
        EvalContext context = null;
        UnionContext unionContext = new UnionContext(new EvalContext[]{new EvalContext(new BasicNodeSet())}, context);

        unionContext.setPosition(0);
    }

    private int contextsLength(UnionContext unionContext) {
        int length = 0;
        for (EvalContext context : unionContext.getContexts()) {
            length += context.size();
        }
        return length;
    }

    @Test
    public void testUnionContextNextSet() {
        EvalContext context1 = new EvalContext(new ArrayList<NodePointer>() {{
            add(new NodePointer(1));
            add(new NodePointer(2));
        }});
        EvalContext context2 = new EvalContext(new ArrayList<NodePointer>() {{
            add(new NodePointer(3));
            add(new NodePointer(4));
        }});

        UnionContext unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);

        unionContext.prepareContext();

        Assert.assertTrue(unionContext.nextSet());
        Assert.assertEquals(4, unionContext.size());
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(1, unionContext.getCurrentNodePointer().getNodeID());
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(2, unionContext.getCurrentNodePointer().getNodeID());
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(3, unionContext.getCurrentNodePointer().getNodeID());
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(4, unionContext.getCurrentNodePointer().getNodeID());
        Assert.assertFalse(unionContext.nextNode());

        Assert.assertTrue(unionContext.nextSet());
        Assert.assertEquals(0, unionContext.size());
    }

    @Test
    public void testUnionContextNextSetWithEmptyContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext[0], null);
        Assert.assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextNextSetWithSingleContext() {
        EvalContext context = new EvalContext(new ArrayList<NodePointer>() {{
            add(new NodePointer(1));
        }});
        UnionContext unionContext = new UnionContext(new EvalContext[]{context}, null);

        unionContext.prepareContext();

        Assert.assertTrue(unionContext.nextSet());
        Assert.assertEquals(1, unionContext.size());
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(1, unionContext.getCurrentNodePointer().getNodeID());
        Assert.assertFalse(unionContext.nextNode());

        Assert.assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextNextNodeWithPreparedContext() {
        EvalContext context1 = new EvalContext(new ArrayList<NodePointer>() {{
            add(new NodePointer(1));
            add(new NodePointer(2));
        }});
        EvalContext context2 = new EvalContext(new ArrayList<NodePointer>() {{
            add(new NodePointer(3));
            add(new NodePointer(4));
        }});

        UnionContext unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);

        unionContext.prepareContext();

        unionContext.setPosition(3);
        Assert.assertFalse(unionContext.nextNode());

        unionContext.setPosition(contextsLength(unionContext) - 1); // last node
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(4, unionContext.getCurrentNodePointer().getNodeID());

        unionContext.setPosition(contextsLength(unionContext) + 1); // out of bounds
        Assert.assertFalse(unionContext.nextNode());
    }

    @Test
    public void testUnionContextNextNodeWithNotPreparedContext() {
        EvalContext context1 = new EvalContext(new ArrayList<NodePointer>() {{
            add(new NodePointer(1));
            add(new NodePointer(2));
        }});
        EvalContext context2 = new EvalContext(new ArrayList<NodePointer>() {{
            add(new NodePointer(3));
            add(new NodePointer(4));
        }});

        UnionContext unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);

        unionContext.setPosition(3);
        Assert.assertFalse(unionContext.nextNode());

        unionContext.setPosition(contextsLength(unionContext) - 1); // last node
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(4, unionContext.getCurrentNodePointer().getNodeID());

        unionContext.setPosition(contextsLength(unionContext) + 1); // out of bounds
        Assert.assertFalse(unionContext.nextNode());

        unionContext.prepareContext();

        unionContext.setPosition(3);
        Assert.assertFalse(unionContext.nextNode());

        unionContext.setPosition(contextsLength(unionContext) - 1); // last node
        Assert.assertTrue(unionContext.nextNode());
        Assert.assertEquals(4, unionContext.getCurrentNodePointer().getNodeID());

        unionContext.setPosition(contextsLength(unionContext) + 1); // out of bounds
        Assert.assertFalse(unionContext.nextNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUnionContextNextNodeWithEmptyContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext[0], null);
        unionContext.setPosition(0);
        unionContext.nextNode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextNextNodeWithNullContexts() {
        UnionContext unionContext = new UnionContext(null, null);
        unionContext.setPosition(0);
        unionContext.nextNode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextNextNodeWithNullContext() {
        EvalContext context = null;
        UnionContext unionContext = new UnionContext(new EvalContext[]{new EvalContext(new BasicNodeSet())}, context);
        unionContext.setPosition(0);
        unionContext.nextNode();
    }
}