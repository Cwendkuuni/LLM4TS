import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithNumbers() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(5.0),
                ExpressionUtil.newLiteral(5.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Boolean result = (Boolean) expression.computeValue(context);
        Assert.assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(5.0),
                ExpressionUtil.newLiteral(6.0)
        );

        context = new EvalContext(new InfoSet());
        result = (Boolean) expression.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testLessThanOrEqualWithNegativeNumbers() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(-5.0),
                ExpressionUtil.newLiteral(-5.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Boolean result = (Boolean) expression.computeValue(context);
        Assert.assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(-5.0),
                ExpressionUtil.newLiteral(-4.0)
        );

        context = new EvalContext(new InfoSet());
        result = (Boolean) expression.computeValue(context);
        Assert.assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(-5.0),
                ExpressionUtil.newLiteral(-3.0)
        );

        context = new EvalContext(new InfoSet());
        result = (Boolean) expression.computeValue(context);
        Assert.assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(-5.0),
                ExpressionUtil.newLiteral(-1.0)
        );

        context = new EvalContext(new InfoSet());
        result = (Boolean) expression.computeValue(context);
        Assert.assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(-5.0),
                ExpressionUtil.newLiteral(0.0)
        );

        context = new EvalContext(new InfoSet());
        result = (Boolean) expression.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testLessThanOrEqualWithStrings() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral("abc"),
                ExpressionUtil.newLiteral("abc")
        );

        EvalContext context = new EvalContext(new InfoSet());
        Boolean result = (Boolean) expression.computeValue(context);
        Assert.assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral("abc"),
                ExpressionUtil.newLiteral("def")
        );

        context = new EvalContext(new InfoSet());
        result = (Boolean) expression.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithMismatchedTypes() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                ExpressionUtil.newLiteral(5.0),
                ExpressionUtil.newLiteral("abc")
        );

        EvalContext context = new EvalContext(new InfoSet());
        expression.computeValue(context);
    }
}