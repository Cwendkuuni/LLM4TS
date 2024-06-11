
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // typical use case where arg1 is greater than arg2
        context.setContextNode(arg1);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // typical use case where arg1 is less than arg2
        context.setContextNode(arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // typical use case where arg1 is equal to arg2
        context.setContextNode(arg3);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // edge case where arg1 and arg2 are both null
        context.setContextNode(null);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // edge case where arg1 is null and arg2 is not null
        context.setContextNode(arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // edge case where arg1 is not null and arg2 is null
        context.setContextNode(arg1);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // error scenario where context node is not set
        context.setContextNode(null);
        Exception exception = assertThrows(Exception.class, () -> {
            operation.computeValue(context);
        });
        assertEquals("Context node not set", exception.getMessage());
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        assertEquals(">", operation.getSymbol());
    }
    
}
