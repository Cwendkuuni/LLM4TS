import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertNotNull(unionContext);
        assertEquals(parentContext, unionContext.getParentContext());
        assertArrayEquals(contexts, unionContext.getContexts());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext[] contexts = {context1};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertTrue(unionContext.setPosition(1)); // Assuming valid position
        // Add more detailed edge cases and error scenarios testing here
    }

    // Add more test cases for other public methods

}