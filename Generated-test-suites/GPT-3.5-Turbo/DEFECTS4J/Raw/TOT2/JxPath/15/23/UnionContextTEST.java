import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Create UnionContext with multiple contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        // Test getDocumentOrder when there are multiple contexts
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        // Create UnionContext with multiple contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        // Test setPosition after preparing the nodeSet
        assertTrue(unionContext.setPosition(1));
    }
}