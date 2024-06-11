import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        /**
         * Typical Use Cases:
         * 1. Both arguments are null
         * 2. Both arguments are non-null and equal
         * 3. Both arguments are non-null but not equal
         * 4. One argument is null and the other is non-null
         */

        // Test Case 1: Both arguments are null
        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(null, null);
        EvalContext context1 = new EvalContext();
        Assert.assertEquals("ComputeValue should return true when both arguments are null.",
                Boolean.TRUE, coreOperationEqual1.computeValue(context1));

        // Test Case 2: Both arguments are non-null and equal
        Expression arg1 = new Constant(true);
        Expression arg2 = new Constant(true);
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(arg1, arg2);
        EvalContext context2 = new EvalContext();
        Assert.assertEquals("ComputeValue should return true when both arguments are non-null and equal.",
                Boolean.TRUE, coreOperationEqual2.computeValue(context2));

        // Test Case 3: Both arguments are non-null but not equal
        Expression arg3 = new Constant(1);
        Expression arg4 = new Constant(2);
        CoreOperationEqual coreOperationEqual3 = new CoreOperationEqual(arg3, arg4);
        EvalContext context3 = new EvalContext();
        Assert.assertEquals("ComputeValue should return false when both arguments are non-null but not equal.",
                Boolean.FALSE, coreOperationEqual3.computeValue(context3));

        // Test Case 4: One argument is null and the other is non-null
        CoreOperationEqual coreOperationEqual4 = new CoreOperationEqual(null, arg1);
        EvalContext context4 = new EvalContext();
        Assert.assertEquals("ComputeValue should return false when one argument is null and the other is non-null.",
                Boolean.FALSE, coreOperationEqual4.computeValue(context4));
    }

    @Test
    public void testGetSymbol() {
        // Test case for getSymbol
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        Assert.assertEquals("getSymbol should return '='", "=", coreOperationEqual.getSymbol());
    }

}