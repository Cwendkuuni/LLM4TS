import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetPosition_singleContext() {
        // Create a single context with one node pointer
        EvalContext singleContext = new EvalContext();
        NodePointer pointer = new NodePointer();
        singleContext.setCurrentNodePointer(pointer);

        // Create a UnionContext with a single context
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{singleContext});

        // Set the position to 1 and verify that it returns true
        assertTrue(unionContext.setPosition(1));
        // Verify that the position is still 1
        assertEquals(1, unionContext.getPosition());
    }

    @Test
    public void testGetPosition_multipleContexts() {
        // Create two contexts, each with two node pointers
        EvalContext context1 = new EvalContext();
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        context1.setCurrentNodePointer(pointer1);
        context1.nextSet();
        context1.setCurrentNodePointer(pointer2);

        EvalContext context2 = new EvalContext();
        NodePointer pointer3 = new NodePointer();
        NodePointer pointer4 = new NodePointer();
        context2.setCurrentNodePointer(pointer3);
        context2.nextSet();
        context2.setCurrentNodePointer(pointer4);

        // Create a UnionContext with the two contexts
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});

        // Set the position to 2 and verify that it returns true
        assertTrue(unionContext.setPosition(2));
        // Verify that the position is still 2
        assertEquals(2, unionContext.getPosition());
    }

    @Test
    public void testGetPosition_noContexts() {
        // Create a UnionContext with no contexts
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);

        // Set the position to 0 and verify that it returns false
        assertFalse(unionContext.setPosition(0));
        // Verify that the position is still 0
        assertEquals(0, unionContext.getPosition());
    }
}