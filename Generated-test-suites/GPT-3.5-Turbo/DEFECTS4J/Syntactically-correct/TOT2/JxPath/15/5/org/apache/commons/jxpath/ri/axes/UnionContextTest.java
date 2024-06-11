import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Test case for typical scenario
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        EvalContext[] contexts = {ctx1, ctx2};
        UnionContext unionContext = new UnionContext(null, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        // Test case for typical scenario
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        EvalContext[] contexts = {ctx1, ctx2};
        UnionContext unionContext = new UnionContext(null, contexts);
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionEdgeCase() {
        // Test case for edge case when position is invalid
        EvalContext ctx1 = new EvalContext();
        EvalContext ctx2 = new EvalContext();
        EvalContext[] contexts = {ctx1, ctx2};
        UnionContext unionContext = new UnionContext(null, contexts);
        assertFalse(unionContext.setPosition(5));
    }

}