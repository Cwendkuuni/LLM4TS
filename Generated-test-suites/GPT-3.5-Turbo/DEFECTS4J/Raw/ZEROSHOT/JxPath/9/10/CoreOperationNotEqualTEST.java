import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are equal
        context.setContextNode(arg1);
        context.setPosition(1);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
        
        // Test when arg1 and arg2 are not equal
        context.setContextNode(arg2);
        context.setPosition(2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}