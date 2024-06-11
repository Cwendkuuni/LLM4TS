import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(3.0)));

        EvalContext context = new EvalContext();
        assertTrue(lessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testEqual() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext();
        assertFalse(lessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testSameType() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext();
        assertFalse(lessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testNaN() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                new ConstantExpression(new Double(Double.NaN)),
                new ConstantExpression(new Double(3.0)));

        EvalContext context = new EvalContext();
        assertFalse(lessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArg1() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                null,
                new ConstantExpression(new Double(3.0)));

        EvalContext context = new EvalContext();
    }

    @Test(expected = NullPointerException.class)
    public void testNullArg2() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                new ConstantExpression(new Double(3.0)),
                null);

        EvalContext context = new EvalContext();
    }
}