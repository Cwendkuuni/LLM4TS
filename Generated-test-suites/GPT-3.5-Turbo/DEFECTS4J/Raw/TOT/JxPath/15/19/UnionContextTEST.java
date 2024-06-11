import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.BasicNodeSet;

public class UnionContextTest {
    
    private UnionContext unionContext;

    @Before
    public void setUp() {
        // Creating a UnionContext with two contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = { context1, context2 };
        unionContext = new UnionContext(null, contexts);
    }

    @Test
    public void testGetDocumentOrderMultipleContexts() {
        // Testing the scenario where there are more than one contexts
        int expected = 1;
        int actual = unionContext.getDocumentOrder();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDocumentOrderSingleContext() {
        // Testing the scenario where there is only one context
        EvalContext[] contexts = { new EvalContext() };
        UnionContext singleContextUnion = new UnionContext(null, contexts);

        int expected = 0;
        int actual = singleContextUnion.getDocumentOrder();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition() {
        // Adding nodes to context1 and context2
        BasicNodeSet nodeSet1 = new BasicNodeSet();
        BasicNodeSet nodeSet2 = new BasicNodeSet();

        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();

        nodeSet1.add(pointer1);
        nodeSet2.add(pointer2);

        ((EvalContext) unionContext.getContexts()[0]).setNodeSet(nodeSet1);
        ((EvalContext) unionContext.getContexts()[1]).setNodeSet(nodeSet2);

        // Setting the position and validating the result
        boolean result = unionContext.setPosition(1);
        assertTrue(result);
    }

    @Test
    public void testSetPositionUnprepared() {
        // Testing the scenario where setPosition is called before preparation
        boolean result = unionContext.setPosition(1);
        assertFalse(result);
    }

    @Test
    public void testSetPositionOutOfBounds() {
        // Testing the scenario where an invalid position is passed
        boolean result = unionContext.setPosition(0);
        assertFalse(result);
    }

    @Test
    public void testSetPositionMultipleContexts() {
        // Adding nodes to context1 and context2
        BasicNodeSet nodeSet1 = new BasicNodeSet();
        BasicNodeSet nodeSet2 = new BasicNodeSet();

        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();

        nodeSet1.add(pointer1);
        nodeSet2.add(pointer2);

        ((EvalContext) unionContext.getContexts()[0]).setNodeSet(nodeSet1);
        ((EvalContext) unionContext.getContexts()[1]).setNodeSet(nodeSet2);

        // Preparing the UnionContext
        unionContext.setPosition(1);

        // Calling setPosition again
        boolean result = unionContext.setPosition(1);
        assertTrue(result);
    }
}