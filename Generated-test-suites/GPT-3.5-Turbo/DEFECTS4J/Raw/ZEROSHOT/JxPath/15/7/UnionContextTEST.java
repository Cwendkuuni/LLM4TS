import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        int expected = 1;
        int result = unionContext.getDocumentOrder();
        assertEquals(expected, result);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        int position = 1;
        boolean expected = super.setPosition(position);
        boolean result = unionContext.setPosition(position);
        assertEquals(expected, result);
    }
}