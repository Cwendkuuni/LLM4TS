import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private UnionContext unionContext;
    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext contextC;
    private int position;

    @Parameterized.Parameters(name = "{index}: UnionContext with contexts A={0}, B={1}, C={2}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new BasicNodeSet(), new BasicNodeSet(), new BasicNodeSet()},
                {new BasicNodeSet(), new BasicNodeSet(), new BasicNodeSet().add(new NodePointer(1))},
                {new BasicNodeSet().add(new NodePointer(1)), new BasicNodeSet(), new BasicNodeSet()},
        });
    }

    @Before
    public void setUp() {
        contextA = new EvalContext(new BasicNodeSet(), null);
        contextB = new EvalContext(new BasicNodeSet(), null);
        contextC = new EvalContext(new BasicNodeSet(), null);
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB, contextC});
        position = -1;
    }

    @Test
    public void testGetDocumentOrder() {
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_whenPositionIsLessThanTotalNumberOfNodes_thenShouldReturnTrue() {
        List<NodePointer> nodesA = new ArrayList<>();
        nodesA.add(new NodePointer(0));
        contextA.setNodeSet(new BasicNodeSet(nodesA));

        List<NodePointer> nodesB = new ArrayList<>();
        nodesB.add(new NodePointer(1));
        contextB.setNodeSet(new BasicNodeSet(nodesB));

        List<NodePointer> nodesC = new ArrayList<>();
        nodesC.add(new NodePointer(2));
        contextC.setNodeSet(new BasicNodeSet(nodesC));

        for (int i = 0; i < unionContext.getDocumentOrder(); i++) {
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
        }

        position = 0;
        assertTrue(unionContext.setPosition(position));
    }

    @Test
    public void testSetPosition_whenPositionIsGreaterThanTotalNumberOfNodes_thenShouldReturnFalse() {
        List<NodePointer> nodesA = new ArrayList<>();
        nodesA.add(new NodePointer(0));
        contextA.setNodeSet(new BasicNodeSet(nodesA));

        List<NodePointer> nodesB = new ArrayList<>();
        nodesB.add(new NodePointer(1));
        contextB.setNodeSet(new BasicNodeSet(nodesB));

        List<NodePointer> nodesC = new ArrayList<>();
        nodesC.add(new NodePointer(2));
        contextC.setNodeSet(new BasicNodeSet(nodesC));

        for (int i = 0; i < unionContext.getDocumentOrder(); i++) {
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
        }

        position = unionContext.getDocumentOrder() + 1;
        assertFalse(unionContext.setPosition(position));
    }

    @Test
    public void testSetPosition_whenPositionIsEqualToCurrentNode_thenShouldReturnTrue() {
        List<NodePointer> nodesA = new ArrayList<>();
        nodesA.add(new NodePointer(0));
        contextA.setNodeSet(new BasicNodeSet(nodesA));

        List<NodePointer> nodesB = new ArrayList<>();
        nodesB.add(new NodePointer(1));
        contextB.setNodeSet(new BasicNodeSet(nodesB));

        List<NodePointer> nodesC = new ArrayList<>();
        nodesC.add(new NodePointer(2));
        contextC.setNodeSet(new BasicNodeSet(nodesC));

        for (int i = 0; i < unionContext.getDocumentOrder(); i++) {
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
        }

        position = unionContext.getDocumentOrder() - 1;
        assertTrue(unionContext.setPosition(position));
        assertTrue(unionContext.getCurrentNodePointer().equals(unionContext.getNodeSet().get(position)));
    }

    @Test
    public void testSetPosition_whenPositionIsEqualToFirstNodeInFirstContext_thenShouldReturnTrue() {
        List<NodePointer> nodesA = new ArrayList<>();
        nodesA.add(new NodePointer(0));
        contextA.setNodeSet(new BasicNodeSet(nodesA));

        List<NodePointer> nodesB = new ArrayList<>();
        nodesB.add(new NodePointer(1));
        contextB.setNodeSet(new BasicNodeSet(nodesB));

        List<NodePointer> nodesC = new ArrayList<>();
        nodesC.add(new NodePointer(2));
        contextC.setNodeSet(new BasicNodeSet(nodesC));

        for (int i = 0; i < unionContext.getDocumentOrder(); i++) {
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
        }

        position = 0;
        assertTrue(unionContext.setPosition(position));
        assertTrue(unionContext.getCurrentNodePointer().equals(contextA.getCurrentNodePointer()));
    }

    @Test
    public void testSetPosition_whenPositionIsEqualToFirstNodeInSecondContext_thenShouldReturnTrue() {
        List<NodePointer> nodesA = new ArrayList<>();
        nodesA.add(new NodePointer(0));
        contextA.setNodeSet(new BasicNodeSet(nodesA));

        List<NodePointer> nodesB = new ArrayList<>();
        nodesB.add(new NodePointer(1));
        contextB.setNodeSet(new BasicNodeSet(nodesB));

        List<NodePointer> nodesC = new ArrayList<>();
        nodesC.add(new NodePointer(2));
        contextC.setNodeSet(new BasicNodeSet(nodesC));

        for (int i = 0; i < unionContext.getDocumentOrder(); i++) {
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
        }

        position = unionContext.getDocumentOrder();
        contextB.nextSet();
        contextB.nextNode();

        assertTrue(unionContext.setPosition(position));
        assertTrue(unionContext.getCurrentNodePointer().equals(contextB.getCurrentNodePointer()));
    }

    @Test
    public void testSetPosition_whenPositionIsEqualToFirstNodeInThirdContext_thenShouldReturnTrue() {
        List<NodePointer> nodesA = new ArrayList<>();
        nodesA.add(new NodePointer(0));
        contextA.setNodeSet(new BasicNodeSet(nodesA));

        List<NodePointer> nodesB = new ArrayList<>();
        nodesB.add(new NodePointer(1));
        contextB.setNodeSet(new BasicNodeSet(nodesB));

        List<NodePointer> nodesC = new ArrayList<>();
        nodesC.add(new NodePointer(2));
        contextC.setNodeSet(new BasicNodeSet(nodesC));

        for (int i = 0; i < unionContext.getDocumentOrder(); i++) {
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
        }

        position = unionContext.getDocumentOrder() + 1;
        contextC.nextSet();
        contextC.nextNode();

        assertTrue(unionContext.setPosition(position));
        assertTrue(unionContext.getCurrentNodePointer().equals(contextC.getCurrentNodePointer()));
    }
}