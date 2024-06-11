import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1: Both arguments are null
        Object result = operation.equal(context, null, null);
        assertTrue(result);
        
        // Test case 2: Both arguments are equal integers
        result = operation.equal(context, 10, 10);
        assertTrue(result);

        // Test case 3: Both arguments are equal strings
        result = operation.equal(context, "abc", "abc");
        assertTrue(result);
        
        // Add more test cases here for argument comparisons
        
    }
    
    // Add test cases for other public methods in the class
    
    // Test for potential edge cases and exception handling scenarios
    
}