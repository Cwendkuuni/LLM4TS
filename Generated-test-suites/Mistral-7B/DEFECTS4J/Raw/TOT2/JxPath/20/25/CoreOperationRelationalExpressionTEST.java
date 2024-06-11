
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ExpressionException;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValueNullInput() {
        Expression left = null;
        Expression right = new ConstantExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);

        EvalContext context = new EvalContext();

        try {
            expression.computeValue(context);
            fail();
        } catch (ExpressionException e) {
            //expected
        }
    }

    @Test
    public void testComputeValueEmptyCollections() {
        Iterator left = new HashSet<Object>().iterator();
        Expression right = new ConstantExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationIterators(left), right);

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        Assert.assertSame(Boolean.FALSE, result);

        Iterator rightIter = new HashSet<Object>().iterator();
        Expression left2 = new CoreOperationIterators(rightIter);
        CoreOperationRelationalExpression expression2 = new CoreOperationGreaterThan(left2, right);

        Object result2 = expression2.computeValue(context);

        Assert.assertSame(Boolean.FALSE, result2);
    }

    @Test
    public void testComputeValueDifferentDataTypes() {
        Expression left = new ConstantExpression(1.0);
        Expression right = new ConstantExpression("2");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);

        EvalContext context = new EvalContext();

        try {
            expression.computeValue(context);
            fail();
        } catch (ClassCastException e) {
            //expected
        }
    }
}
