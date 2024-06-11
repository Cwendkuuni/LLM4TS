import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertFalse(unionContext.setPosition(0));
    }
}