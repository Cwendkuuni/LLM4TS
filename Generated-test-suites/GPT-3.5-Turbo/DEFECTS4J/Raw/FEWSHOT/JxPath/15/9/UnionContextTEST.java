import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class UnionContextTest {
    @Test
    public void testGetDocumentOrderWithSingleContext() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetDocumentOrderWithMultipleContexts() {
         EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPositionBeforePreparation() {
         EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionAfterPreparation() {
         EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(1);
        assertTrue(unionContext.setPosition(2));
    }

    @Test
    public void testSetPositionInvalidPosition() {
         EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertFalse(unionContext.setPosition(0));
    }
}