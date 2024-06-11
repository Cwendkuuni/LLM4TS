import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;

    @Before
    public void setUp() {
        // Initialize contexts A and B with some nodes
        contextA = new EvalContext(new BasicNodeSet());
        contextB = new EvalContext(new BasicNodeSet());

        // Add some nodes to context A
        contextA.addNode("nodeA1", new NodePointer("document1", 1));
        contextA.addNode("nodeA2", new NodePointer("document1", 2));

        // Add some nodes to context B
        contextB.addNode("nodeB1", new NodePointer("document1", 3));
        contextB.addNode("nodeB2", new NodePointer("document1", 4));

        // Create a UnionContext with contexts A and B as arguments
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_whenPositionIsValid_shouldReturnTrue() {
        // Test setting position with valid index
        unionContext.setPosition(0);
        unionContext.nextSet();
        unionContext.nextNode();

        assertTrue(unionContext.getCurrentNode() instanceof NodePointer);
        assertEquals("nodeA1", unionContext.getCurrentNode().getName());

        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();

        assertTrue(unionContext.getCurrentNode() instanceof NodePointer);
        assertEquals("nodeB1", unionContext.getCurrentNode().getName());

        unionContext.nextSet(); // This should be the last set
        assertTrue(unionContext.nextNode() == null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_whenPositionIsInvalid_shouldThrowIllegalStateException() {
        // Test setting position with an invalid index
        unionContext.setPosition(5);
    }
}