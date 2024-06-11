import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new ExpressionStub();
        Expression right = new ExpressionStub();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareStub(left, right);
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        // ...
    }
    
    // Additional test cases for other methods
    
    // ...
    
}