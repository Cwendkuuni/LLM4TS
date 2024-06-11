import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {
    
    private UnionContext unionContext;

    @Before
    public void setUp() {
        EvalContext evalContext1 = new EvalContext();
        EvalContext evalContext2 = new EvalContext();
        EvalContext evalContext3 = new EvalContext();
        EvalContext[] evalContexts = {evalContext1, evalContext2, evalContext3};
        unionContext = new UnionContext(null, evalContexts);
    }

    @Test
    public void testGetDocumentOrder() {
        // Test when there is only one context
        EvalContext evalContext = new EvalContext();
        EvalContext[] evalContexts = {evalContext};
        UnionContext singleContextUnion = new UnionContext(null, evalContexts);
        assertEquals(0, singleContextUnion.getDocumentOrder());

        // Test when there are multiple contexts
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        // Test when the context is not prepared
        assertFalse(unionContext.setPosition(0)); // The return value must be false
        
        // Test when the context is prepared
        unionContext.setPosition(1);
        assertTrue(unionContext.setPosition(1)); // The return value must be true
    }

}