import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import org.junit.Assert;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(null, null);
        EvalContext context = null;

        // Test basic functionality
        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test edge cases and exception handling scenarios
        // TODO: Add additional test cases to cover edge cases and exception handling scenarios

    }

}