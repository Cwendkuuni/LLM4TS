import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValueTypicalCase() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Set values for arg1 and arg2 in the context
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);

        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testComputeValueTypicalCaseEqualValues() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Set values for arg1 and arg2 in the context
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 5);

        Assert.assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testComputeValueTypicalCaseNegativeValues() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Set values for arg1 and arg2 in the context
        context.setVariable("arg1", -3);
        context.setVariable("arg2", -5);

        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullContext() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());

        expression.computeValue(null);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());

        Assert.assertEquals(">", expression.getSymbol());
    }
}