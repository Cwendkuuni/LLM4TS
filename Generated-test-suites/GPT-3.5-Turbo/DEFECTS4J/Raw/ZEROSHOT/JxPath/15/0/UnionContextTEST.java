import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertTrue(unionContext.setPosition(1));
    }
}