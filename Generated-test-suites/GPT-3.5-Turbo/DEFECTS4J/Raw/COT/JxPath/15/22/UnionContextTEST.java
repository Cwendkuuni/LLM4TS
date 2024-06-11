import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[]{new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        int result = unionContext.getDocumentOrder();
        assertEquals(1, result);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[]{new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean result = unionContext.setPosition(3);
        assertEquals(true, result);
    }

    @Test
    public void testSetPositionWithPreparedFalse() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[]{new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean result = unionContext.setPosition(1);
        assertEquals(true, result);
    }
}