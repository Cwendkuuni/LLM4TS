
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqualWithPrimitives() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        // Test case 1
        assertTrue(operation.equal(context, new ConstantExpression(1), new ConstantExpression(1)));
        
        // Test case 2
        assertFalse(operation.equal(context, new ConstantExpression(1), new ConstantExpression(2)));
    }

    // Rest of the test cases...
}