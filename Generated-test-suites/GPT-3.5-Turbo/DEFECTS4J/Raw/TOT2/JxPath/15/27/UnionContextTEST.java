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
        assertEquals(2, unionContext.contexts.length);
        assertTrue(unionContext.getNodeSet() instanceof BasicNodeSet);
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
        NodePointer pointer1 = new NodePointer();
        context1.setCurrentNodePointer(pointer1);
        
        EvalContext context2 = new EvalContext();
        NodePointer pointer2 = new NodePointer();
        context2.setCurrentNodePointer(pointer2);

        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.setPosition(1));
        assertFalse(unionContext.setPosition(2));
    }
}