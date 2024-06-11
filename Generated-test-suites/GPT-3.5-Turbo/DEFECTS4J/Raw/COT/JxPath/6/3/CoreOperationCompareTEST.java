import org.junit.Test;

public class CoreOperationCompareTest {

    // Test equal method
    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = coreOperationCompare.equal(context, left, right);
        // Assert the result here
    }

    // TODO: Add test cases for other methods

}