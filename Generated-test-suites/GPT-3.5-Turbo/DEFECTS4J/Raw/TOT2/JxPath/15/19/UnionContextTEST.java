import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
        assertArrayEquals(contexts, unionContext.getContexts());
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
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        EvalContext context1 = new EvalContext();
        context1.setCurrentNodePointer(pointer1);
        EvalContext context2 = new EvalContext();
        context2.setCurrentNodePointer(pointer2);
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionUnprepared() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertFalse(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionWithDuplicateNodePointers() {
        EvalContext parentContext = new EvalContext();
        NodePointer pointer1 = new NodePointer();
        EvalContext context1 = new EvalContext();
        context1.setCurrentNodePointer(pointer1);
        EvalContext context2 = new EvalContext();
        context2.setCurrentNodePointer(pointer1);
        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.setPosition(1));
    }
}