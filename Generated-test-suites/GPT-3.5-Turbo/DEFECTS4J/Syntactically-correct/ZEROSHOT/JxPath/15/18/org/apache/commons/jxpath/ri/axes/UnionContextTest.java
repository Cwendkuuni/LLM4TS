import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class UnionContextTest {

    private UnionContext unionContext;

    @Before
    public void setUp() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void getDocumentOrder_shouldReturn1_whenContextsLengthIsGreaterThan1() {
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(1, documentOrder);
    }

    @Test
    public void getDocumentOrder_shouldReturnSuperGetDocumentOrder_whenContextsLengthIsNotGreaterThan1() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        contexts[0] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(parentContext.getDocumentOrder(), documentOrder);
    }

    @Test
    public void setPosition_shouldReturnTrue() {
        boolean result = unionContext.setPosition(3);
        assertTrue(result);
    }

    @Test
    public void setPosition_shouldReturnFalse_whenNotPrepared() {
        unionContext = new UnionContext(new EvalContext(), new EvalContext[]{});
        boolean result = unionContext.setPosition(1);
        assertFalse(result);
    }

    @Test
    public void setPosition_shouldReturnSuperSetPosition_whenNotPrepared() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        contexts[0] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        boolean result = unionContext.setPosition(2);
        assertFalse(result);
    }

}