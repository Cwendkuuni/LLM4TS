import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private UnionContext unionContext;

    @Before
    public void setUp() {
        contextA = new EvalContext(new NodeSetContext(new BasicNodeSet())) {
            {
                addNode("A1");
                addNode("A2");
            }
        };

        contextB = new EvalContext(new NodeSetContext(new BasicNodeSet())) {
            {
                addNode("B1");
                addNode("B2");
                addNode("A3"); // Both contexts should contain "A3"
            }
        };

        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
    }

    // Test basic functionality of UnionContext constructor
    @Test
    public void testUnionContextConstructor() {
        Assert.assertNotNull(unionContext);
    }

    // Test getDocumentOrder method
    @Test
    public void testGetDocumentOrder() {
        Assert.assertEquals(1, unionContext.getDocumentOrder());
    }

    // Test setPosition method with valid position
    @Test
    public void testSetPositionValidPosition() {
        Assert.assertTrue(unionContext.setPosition(0));
    }

    // Test setPosition method with invalid position
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionInvalidPosition() {
        unionContext.setPosition(3);
    }

    // Test edge case: both contexts have the same nodes
    @Test
    public void testUnionContextSameNodes() {
        EvalContext contextC = new EvalContext(new NodeSetContext(new BasicNodeSet())) {
            {
                addNode("A3"); // Both contexts should contain "A3"
            }
        };

        UnionContext unionContextSameNodes = new UnionContext(null, new EvalContext[]{contextA, contextC});
        List<NodePointer> nodes = new ArrayList<>();
        while (unionContextSameNodes.nextSet()) {
            while (unionContextSameNodes.nextNode()) {
                nodes.add(unionContextSameNodes.getCurrentNodePointer());
                unionContextSameNodes.nextNode(); // Consume current node to move to next node
            }
        }

        List<NodePointer> expectedNodes = new ArrayList<>(Arrays.asList(contextA.getNodeSet().getNodes().toArray(new NodePointer[0])));
        expectedNodes.addAll(contextC.getNodeSet().getNodes().toArray(new NodePointer[0]));

        Assert.assertEquals(expectedNodes, nodes);
    }

    // Test edge case: one context has no nodes
    @Test
    public void testUnionContextNoNodesContext() {
        EvalContext contextC = new EvalContext(new NodeSetContext(new BasicNodeSet()));

        UnionContext unionContextNoNodesContext = new UnionContext(null, new EvalContext[]{contextA, contextC});
        List<NodePointer> nodes = new ArrayList<>();
        while (unionContextNoNodesContext.nextSet()) {
            while (unionContextNoNodesContext.nextNode()) {
                nodes.add(unionContextNoNodesContext.getCurrentNodePointer());
                unionContextNoNodesContext.nextNode(); // Consume current node to move to next node
            }
        }

        List<NodePointer> expectedNodes = new ArrayList<>(contextA.getNodeSet().getNodes().toArray(new NodePointer[0]));

        Assert.assertEquals(expectedNodes, nodes);
    }

    // Test exception handling: null contexts
    @Test(expected = NullPointerException.class)
    public void testUnionContextNullContexts() {
        new UnionContext(null, null);
    }
}