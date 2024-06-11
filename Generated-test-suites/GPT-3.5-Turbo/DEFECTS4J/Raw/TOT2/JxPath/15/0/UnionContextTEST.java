import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext[] contexts = new EvalContext[2];
        // Create EvalContext objects and add them to the contexts array
        UnionContext unionContext = new UnionContext(null, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext[] contexts = new EvalContext[2];
        // Create EvalContext objects and add them to the contexts array
        UnionContext unionContext = new UnionContext(null, contexts);
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionEdgeCase() {
        EvalContext[] contexts = new EvalContext[1]; // Edge case with only one context
        // Create EvalContext object and add it to the contexts array
        UnionContext unionContext = new UnionContext(null, contexts);
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionWithEmptyContextsArray() {
        EvalContext[] contexts = new EvalContext[0]; // Zero contexts
        UnionContext unionContext = new UnionContext(null, contexts);
        assertFalse(unionContext.setPosition(1));
    }
}