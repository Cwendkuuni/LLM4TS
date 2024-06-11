import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(context1, new EvalContext[]{context1, context2});

        assertEquals(1, unionContext.getDocumentOrder());

        unionContext = new UnionContext(context1, new EvalContext[]{context1});
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(context1, new EvalContext[]{context1, context2});

        unionContext.setPosition(1);
        // Add your test logic here
    }
}