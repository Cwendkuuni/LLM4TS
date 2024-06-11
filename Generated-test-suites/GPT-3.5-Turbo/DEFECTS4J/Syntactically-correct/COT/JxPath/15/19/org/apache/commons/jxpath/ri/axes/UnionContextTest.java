
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Test case to check the document order of the UnionContext
        // when the context has more than 1 sub-contexts
        // Expected: 1
        EvalContext subContext1 = new EvalContext();
        EvalContext subContext2 = new EvalContext();
        EvalContext[] subContexts = {subContext1, subContext2};
        UnionContext context = new UnionContext(null, subContexts);
        
        int documentOrder = context.getDocumentOrder();
        
        assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPosition() {
        // Test case to check the setPosition method of UnionContext
        // when the context is not prepared
        // Expected: True
        EvalContext subContext1 = new EvalContext();
        evalContext1.setNextSet(true);
        evalContext1.setNextNode(true);
        EvalContext[] subContexts = {subContext1};
        UnionContext context = new UnionContext(null, subContexts);
        
        boolean setPositionResult = context.setPosition(1);

        assertTrue(setPositionResult);
    }
}
