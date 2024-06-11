import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
        assertEquals(parentContext, unionContext.getParentContext());
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
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionUnprepared() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.prepared = false;
        assertFalse(unionContext.setPosition(1));
    }
}