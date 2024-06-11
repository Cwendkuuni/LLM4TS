import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    @Test
    public void testUnionContextBasicFunctionality() {
        EvalContext contextA = new EvalContext(new BasicNodeSet());
        EvalContext contextB = new EvalContext(new BasicNodeSet());
        NodePointer nodeA = new NodePointer("node1", 1);
        NodePointer nodeB = new NodePointer("node2", 1);
        contextA.pushNodeSet(new BasicNodeSet());
        contextA.pushNodeSet(new BasicNodeSet());
        contextA.add(nodeA);
        contextB.pushNodeSet(new BasicNodeSet());
        contextB.pushNodeSet(new BasicNodeSet());
        contextB.add(nodeB);

        UnionContext unionContext = new UnionContext(contextA, contextB);
        int position = 0;
        assertTrue(unionContext.setPosition(position));
        NodePointer node = unionContext.getCurrentNodePointer();
        assertEquals(nodeA, node);

        position = 1;
        assertTrue(unionContext.setPosition(position));
        node = unionContext.getCurrentNodePointer();
        assertEquals(nodeB, node);
    }

    @Test
    public void testUnionContextEdgeCases() {
        EvalContext contextA = new EvalContext(new BasicNodeSet());
        EvalContext contextB = new EvalContext(new BasicNodeSet());
        NodePointer nodeA1 = new NodePointer("node1", 1);
        NodePointer nodeA2 = new NodePointer("node2", 1);
        NodePointer nodeB1 = new NodePointer("node3", 1);
        NodePointer nodeB2 = new NodePointer("node4", 1);

        contextA.pushNodeSet(new BasicNodeSet());
        contextA.add(nodeA1);
        contextA.add(nodeA2);

        contextB.pushNodeSet(new BasicNodeSet());
        contextB.add(nodeB1);
        contextB.add(nodeB2);

        UnionContext unionContext = new UnionContext(contextA, contextB);

        int position = 0;
        assertTrue(unionContext.setPosition(position));
        NodePointer node = unionContext.getCurrentNodePointer();
        assertEquals(nodeA1, node);

        position = 1;
        assertTrue(unionContext.setPosition(position));
        node = unionContext.getCurrentNodePointer();
        assertEquals(nodeA2, node);

        position = 2;
        assertTrue(unionContext.setPosition(position));
        node = unionContext.getCurrentNodePointer();
        assertEquals(nodeB1, node);

        position = 3;
        assertTrue(unionContext.setPosition(position));
        node = unionContext.getCurrentNodePointer();
        assertEquals(nodeB2, node);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUnionContextExceptionScenario() {
        EvalContext contextA = new EvalContext(new BasicNodeSet());
        EvalContext contextB = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(contextA, contextB);

        int position = 10;
        assertFalse(unionContext.setPosition(position));
    }
}