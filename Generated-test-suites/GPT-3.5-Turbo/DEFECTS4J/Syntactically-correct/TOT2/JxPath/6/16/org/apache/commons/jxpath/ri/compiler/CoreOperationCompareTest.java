import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test for equality of two equal values
        assertTrue(operation.equal(context, new Expression(), new Expression()));
        
        // Test for inequality of two different values
        assertFalse(operation.equal(context, new Expression(), new Expression()));

        // Add more test cases for different scenarios
        // Edge cases, error scenarios, etc.
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        
        // Add test cases for the contains method
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        
        // Add test cases for the findMatch method
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        
        // Add test cases for comparing values
    }

}