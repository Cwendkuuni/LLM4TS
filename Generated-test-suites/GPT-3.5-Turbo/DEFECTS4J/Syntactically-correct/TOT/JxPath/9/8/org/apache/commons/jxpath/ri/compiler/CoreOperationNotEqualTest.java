import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_typicalCase() {
        // Typical case where two arguments are not equal
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        context.setVariable("arg1", "Hello");
        context.setVariable("arg2", "World");
        Object result = operation.computeValue(context);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_typicalCase_equalInputs() {
        // Typical case where two arguments are equal
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        context.setVariable("arg1", "Hello");
        context.setVariable("arg2", "Hello");
        Object result = operation.computeValue(context);
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testComputeValue_edgeCase_nullArgument() {
        // Edge case where one argument is null
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        context.setVariable("arg1", null);
        context.setVariable("arg2", "Hello");
        Object result = operation.computeValue(context);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_errorCase_invalidArguments() {
        // Error case where both arguments are invalid
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        context.setVariable("arg1", new Object());
        context.setVariable("arg2", new Object());
        Object result = operation.computeValue(context);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}