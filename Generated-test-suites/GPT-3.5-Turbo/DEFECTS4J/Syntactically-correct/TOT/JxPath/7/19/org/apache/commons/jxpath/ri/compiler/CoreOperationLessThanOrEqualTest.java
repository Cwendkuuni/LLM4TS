import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void computeValue_typicalCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setExpression("2", 2);
        context.setExpression("3", 3);

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void computeValue_equalOperands() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setExpression("4", 4);
        context.setExpression("4", 4);

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void computeValue_leftOperandGreater() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setExpression("5", 5);
        context.setExpression("4", 4);

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void computeValue_rightOperandGreater() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setExpression("4", 4);
        context.setExpression("5", 5);

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void computeValue_nonNumericOperands() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setExpression("a", "a");
        context.setExpression("b", "b");

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void getSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());

        String symbol = operation.getSymbol();

        Assert.assertEquals("<=", symbol);
    }

}