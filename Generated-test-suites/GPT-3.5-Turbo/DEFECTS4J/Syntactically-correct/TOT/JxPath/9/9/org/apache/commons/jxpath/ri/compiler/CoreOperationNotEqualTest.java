import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    // Method: computeValue(EvalContext)
    
    // Test case 1: Test when both arguments are equal
    @Test
    public void testComputeValueEqualArguments() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        // Set equal values for arguments
        arg1.setValue(5);
        arg2.setValue(5);
        
        // Compute the value using the context
        Object result = operation.computeValue(context);
        
        // Verify that the result is Boolean.FALSE
        assertEquals(Boolean.FALSE, result);
    }
    
    // Test case 2: Test when both arguments are not equal
    @Test
    public void testComputeValueNotEqualArguments() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        // Set different values for arguments
        arg1.setValue("abc");
        arg2.setValue("def");
        
        // Compute the value using the context
        Object result = operation.computeValue(context);
        
        // Verify that the result is Boolean.TRUE
        assertEquals(Boolean.TRUE, result);
    }
    
    
    // Method: getSymbol()
    
    // Test case 3: Test the getSymbol() method
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        // Verify that the symbol is "!="
        assertEquals("!=", operation.getSymbol());
    }
    
}