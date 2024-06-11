import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Create an array of contexts
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        
        // Create a UnionContext object
        UnionContext unionContext = new UnionContext(null, contexts);
        
        // Test when the number of contexts is greater than 1
        int expectedOrder = 1;
        int actualOrder = unionContext.getDocumentOrder();
        assertEquals(expectedOrder, actualOrder);
        
        // Test when the number of contexts is 1
        contexts = new EvalContext[1];
        contexts[0] = new EvalContext();
        unionContext = new UnionContext(null, contexts);
        expectedOrder = 0;
        actualOrder = unionContext.getDocumentOrder();
        assertEquals(expectedOrder, actualOrder);
    }
    
    @Test
    public void testSetPosition() {
        // Create an array of contexts
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        
        // Create a UnionContext object
        UnionContext unionContext = new UnionContext(null, contexts);
        
        // Test when position is valid
        int position = 5;
        boolean result = unionContext.setPosition(position);
        assertTrue(result);
        
        // Test when position is not valid
        position = -1;
        result = unionContext.setPosition(position);
        assertFalse(result);
    }
}