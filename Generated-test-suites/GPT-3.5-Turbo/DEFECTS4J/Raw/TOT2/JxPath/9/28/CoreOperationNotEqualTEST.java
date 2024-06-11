import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_EqualArguments() {
        CoreOperationNotEqual coreOp = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        context.setArg(0, "value");
        context.setArg(1, "value");
        
        Object result = coreOp.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_NotEqualArguments() {
        CoreOperationNotEqual coreOp = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        context.setArg(0, "value1");
        context.setArg(1, "value2");
        
        Object result = coreOp.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOp = new CoreOperationNotEqual(new Expression(), new Expression());
        
        assertEquals("!=", coreOp.getSymbol());
    }
    
    @Test
    public void testComputeValue_NullArguments() {
        CoreOperationNotEqual coreOp = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        context.setArg(0, null);
        context.setArg(1, "value");
        
        Object result = coreOp.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_EmptyStringArguments() {
        CoreOperationNotEqual coreOp = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        context.setArg(0, "");
        context.setArg(1, "");
        
        Object result = coreOp.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
}