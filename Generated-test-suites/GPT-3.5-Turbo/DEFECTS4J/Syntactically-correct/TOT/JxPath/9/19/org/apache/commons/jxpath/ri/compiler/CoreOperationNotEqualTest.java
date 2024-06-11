import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: Two different objects
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));

        // Typical use case: Two equal objects
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(new Expression(), new Expression());
        Assert.assertEquals(Boolean.FALSE, operation2.computeValue(new EvalContext()));

        // Edge case: Two null objects
        CoreOperationNotEqual operation3 = new CoreOperationNotEqual(null, null);
        Assert.assertEquals(Boolean.FALSE, operation3.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        // Edge case: Get the symbol
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        Assert.assertEquals("!=", operation.getSymbol());
    }
}