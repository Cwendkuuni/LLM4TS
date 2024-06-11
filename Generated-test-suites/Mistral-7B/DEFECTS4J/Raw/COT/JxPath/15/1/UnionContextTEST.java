import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;

    @Before
    public void setUp() {
        contextA = new EvalContext(new BasicNodeSet());
        contextB = new EvalContext(new BasicNodeSet());
    }

    @Test
    public void testUnionContextConstructor() {
        EvalContext[] contexts = new EvalContext[]{contextA, contextB};
        unionContext = new UnionContext(null, contexts);
        Assert.assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        int documentOrder = unionContext.getDocumentOrder();
        Assert.assertEquals(1, documentOrder);

        unionContext = new NodeSetContext(null, new BasicNodeSet());
        documentOrder = unionContext.getDocumentOrder();
        Assert.assertEquals(0, documentOrder);
    }

    @Test
    public void testSetPosition_EmptyContexts() {
        unionContext = new UnionContext(null, new EvalContext[0]);
        Assert.assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_SingleContext() {
        unionContext = new UnionContext(null, contextA);
        NodePointer node = new NodePointer(1);
        contextA.append(node);
        unionContext.setPosition(0);
        Assert.assertEquals(node, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_MultipleContexts() {
        NodePointer nodeA1 = new NodePointer(1), nodeA2 = new NodePointer(2), nodeB1 = new NodePointer(3);
        contextA.append(nodeA1);
        contextA.append(nodeA2);
        contextB.append(nodeB1);

        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        unionContext.setPosition(1);
        Assert.assertEquals(nodeA2, unionContext.getCurrentNodePointer());

        unionContext.setPosition(3);
        Assert.assertEquals(nodeB1, unionContext.getCurrentNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_InvalidPosition() {
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        unionContext.setPosition(3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNextSet_UnionContext() {
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        unionContext.nextSet();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNextNode_UnionContext() {
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        unionContext.nextNode();
    }
}