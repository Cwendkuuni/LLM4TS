import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(operation);
        assertEquals("Operation symbol should be !=", "!=", operation.getSymbol());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new DummyEvalContext();
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        // Test when arguments are equal
        context.set(args[0], "value");
        context.set(args[1], "value");
        assertEquals(false, operation.computeValue(context));
        
        // Test when arguments are not equal
        context.set(args[0], "value1");
        context.set(args[1], "value2");
        assertEquals(true, operation.computeValue(context));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArguments() {
        EvalContext context = new DummyEvalContext();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        
        operation.computeValue(context);
        fail("NullPointerException should be thrown for null arguments");
    }
    
    // Additional test cases for edge cases and exceptions could be added here
    
}