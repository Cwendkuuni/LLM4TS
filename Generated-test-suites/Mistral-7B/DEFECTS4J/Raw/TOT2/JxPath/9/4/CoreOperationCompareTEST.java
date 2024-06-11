import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_sameValues() {
        CoreOperation op = new CoreOperationCompare(
                new ExpressionTestHelper.ConstantExpression(1),
                new ExpressionTestHelper.ConstantExpression(1));
        EvalContext context = new EvalContext();
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testEqual_differentTypes_sameValues() {
        CoreOperation op = new CoreOperationCompare(
                new ExpressionTestHelper.ConstantExpression(1),
                new ExpressionTestHelper.ConstantExpression("1"));
        EvalContext context = new EvalContext();
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testEqual_sameObjects() {
        Object a = new Object();
        CoreOperation op = new CoreOperationCompare(
                new ExpressionTestHelper.ConstantExpression(a),
                new ExpressionTestHelper.ConstantExpression(a));
        EvalContext context = new EvalContext();
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testEqual_differentObjects_equal() {
        Object a = new Object();
        Object b = new Object();
        if (a.equals(b)) {
            CoreOperation op = new CoreOperationCompare(
                    new ExpressionTestHelper.ConstantExpression(a),
                    new ExpressionTestHelper.ConstantExpression(b));
            EvalContext context = new EvalContext();
            assertTrue(op.evaluate(context).booleanValue());
        } else {
            assertFalse(op.evaluate(context).booleanValue());
        }
    }

    @Test
    public void testEqual_differentObjects_notEqual() {
        Object a = new Object();
        Object b = new Object();
        CoreOperation op = new CoreOperationCompare(
                new ExpressionTestHelper.ConstantExpression(a),
                new ExpressionTestHelper.ConstantExpression(b));
        EvalContext context = new EvalContext();
        assertFalse(op.evaluate(context).booleanValue());
    }

    @Test(expected = RuntimeException.class)
    public void testEqual_invalidArgumentTypes() {
        CoreOperation op = new CoreOperationCompare(
                new ExpressionTestHelper.ConstantExpression(1),
                new ExpressionTestHelper.ConstantExpression(new RuntimeException("Test")));
        EvalContext context = new EvalContext();
        op.evaluate(context); // Should throw exception
    }
}