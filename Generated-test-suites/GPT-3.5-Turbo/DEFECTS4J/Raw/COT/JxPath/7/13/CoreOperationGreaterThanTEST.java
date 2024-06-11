import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Add test cases here for the method's functionality
    }

    @Test
    public void testComputeValueEdgeCases() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Add test cases here for potential edge cases
    }

    @Test(expected = Exception.class)
    public void testComputeValueExceptions() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Add test cases here for potential exceptions
    }
}