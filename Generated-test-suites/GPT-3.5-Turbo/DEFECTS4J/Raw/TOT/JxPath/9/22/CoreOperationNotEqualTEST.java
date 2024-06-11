import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_typicalCase() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        
        // Typical case where arg1 and arg2 are not equal
        context.setArg("arg1", "value1");
        context.setArg("arg2", "value2");
        assertEquals(true, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_typicalCase_equalArguments() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        
        // Typical case where arg1 and arg2 are equal
        context.setArg("arg1", "value1");
        context.setArg("arg2", "value1");
        assertEquals(false, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_edgeCase_nullArguments() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        
        // Edge case where arg1 and/or arg2 is null
        context.setArg("arg1", null);
        context.setArg("arg2", "value2");
        assertEquals(true, operation.computeValue(context));
        context.setArg("arg1", "value1");
        context.setArg("arg2", null);
        assertEquals(true, operation.computeValue(context));
        context.setArg("arg1", null);
        context.setArg("arg2", null);
        assertEquals(false, operation.computeValue(context));
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValue_errorCase_nullContext() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        
        // Error case where context is null
        operation.computeValue(null);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        
        // Symbol should always be "!="
        assertEquals("!=", operation.getSymbol());
    }
}