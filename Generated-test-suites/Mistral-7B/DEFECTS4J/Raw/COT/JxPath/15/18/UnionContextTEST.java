import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * UnionContextTest.java
 */
public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new Object(), new BasicNodeSet());
        EvalContext context1 = new EvalContext(new Object(), new BasicNodeSet());
        EvalContext context2 = new EvalContext(new Object(), new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

        Assert.assertArrayNotNull(unionContext.contexts);
        Assert.assertEquals(2, unionContext.contexts.length);
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object(), new BasicNodeSet()), new EvalContext[] {new EvalContext(new Object(), new BasicNodeSet()), new EvalContext(new Object(), new BasicNodeSet())});
        int documentOrder = unionContext.getDocumentOrder();
        Assert.assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPosition_whenContextsAreEmpty_shouldReturnFalse() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object(), new BasicNodeSet()), new EvalContext[0]);
        Assert.assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_whenPositionIsOutOfRange_shouldReturnFalse() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object(), new BasicNodeSet()), new EvalContext[] {new EvalContext(new Object(), new BasicNodeSet()), new EvalContext(new Object(), new BasicNodeSet())});
        Assert.assertFalse(unionContext.setPosition(3));
    }

    @Test
    public void testSetPosition_whenPositionIsValid_shouldReturnTrue() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object(), new BasicNodeSet()), new EvalContext[] {new EvalContext(new Object(), new BasicNodeSet()), new EvalContext(new Object(), new BasicNodeSet())});
        List<NodePointer> initialPointers = new ArrayList<>();
        for (EvalContext ctx : unionContext.contexts) {
            for (int i = 0; ctx.nextSet(); i++) {
                for (int j = 0; ctx.nextNode(); j++) {
                    initialPointers.add(ctx.getCurrentNodePointer());
                }
            }
        }
        int position = initialPointers.size() - 1;
        Assert.assertTrue(unionContext.setPosition(position));
    }
}