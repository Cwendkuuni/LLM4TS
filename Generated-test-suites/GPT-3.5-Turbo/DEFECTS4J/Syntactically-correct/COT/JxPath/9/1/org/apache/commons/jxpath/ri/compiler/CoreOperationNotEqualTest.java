
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {
  
    @Test
    public void testComputeValue() {
        // Create test data
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual obj = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContextNode(arg1);

        // Invoke the tested method
        Object result = obj.computeValue(context);

        // Verify the result
        Assert.assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetSymbol() {
        // Create test data
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual obj = new CoreOperationNotEqual(arg1, arg2);

        // Invoke the tested method
        String result = obj.getSymbol();

        // Verify the result
        Assert.assertEquals("!=", result);
    }
}
