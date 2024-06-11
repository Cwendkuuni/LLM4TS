import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertNotNull(unionContext);
        assertEquals(parentContext, unionContext.getParentContext());
        assertEquals(0, unionContext.getNodeSet().size());
        assertArrayEquals(contexts, unionContext.getContexts());
        assertFalse(unionContext.isPrepared());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertEquals(1, unionContext.getDocumentOrder());

        EvalContext singleContext = new EvalContext();
        EvalContext[] singleContexts = {singleContext};
        UnionContext singleUnionContext = new UnionContext(parentContext, singleContexts);

        assertEquals(0, singleUnionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = {new EvalContext(), new EvalContext()};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertFalse(unionContext.isPrepared());
        assertTrue(unionContext.setPosition(0)); // No nodes added before preparing
        assertTrue(unionContext.isPrepared());
    }
}