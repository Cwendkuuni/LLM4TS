import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Create UnionContext with two contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext contexts[] = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);
        
        // Test if getDocumentOrder returns 1 for multiple contexts
        assertEquals(1, unionContext.getDocumentOrder());
    }
    
    @Test
    public void testSetPosition() {
        // Create UnionContext with two contexts
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext contexts[] = {context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);
        
        // Set position and check if it was set successfully
        assertTrue(unionContext.setPosition(1));
    }

}