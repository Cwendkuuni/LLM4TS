import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder_singleContext() {
        EvalContext context = new EvalContext();
        UnionContext unionContext = new UnionContext(context, new EvalContext[]{context});
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetDocumentOrder_multipleContexts() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(context1, new EvalContext[]{context1, context2});
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(context1, new EvalContext[]{context1, context2});
        assertTrue(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_invalidPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(context1, new EvalContext[]{context1, context2});
        assertFalse(unionContext.setPosition(-1));
    }
}