import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_SmallerTrue() {
        // Test when the left operand is smaller than the right operand
        Expression[] args = new Expression[] {new Constant(2), new Constant(3)};
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        assertTrue((Boolean)coreOperation.computeValue(null));
    }

    // Add more test cases for other public methods and edge cases

}