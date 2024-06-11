
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_Equal() {
        ExpressionMock expressionMock = new ExpressionMock();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expressionMock, expressionMock);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        String symbol = coreOperationNotEqual.getSymbol();
        Assert.assertEquals("!=", symbol);
    }

    /*
    Additional test cases:
    - Edge cases:
        - Two null arguments
    - Exception handling:
        - When evaluating the arguments throws an exception
    */

    @Test
    public void testComputeValue_NullArguments() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, null);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test(expected = Exception.class)
    public void testComputeValue_ExceptionEvaluation() {
        ExpressionMock expressionMock = new ExpressionMock();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expressionMock, expressionMock);
        EvalContext context = new EvalContext();
        expressionMock.setThrowException(true);
        coreOperationNotEqual.computeValue(context);
    }

}
