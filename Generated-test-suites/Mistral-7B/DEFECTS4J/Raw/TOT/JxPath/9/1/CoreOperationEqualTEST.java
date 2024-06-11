import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual_TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationEqualTest extends CoreOperationEqual_TestCase {

    @Test
    public void testEqualTrue() {
        Expression arg1 = createNumberExpression(1);
        Expression arg2 = createNumberExpression(1);
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualFalse() {
        Expression arg1 = createNumberExpression(1);
        Expression arg2 = createNumberExpression(2);
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualStringTrue() {
        Expression arg1 = createStringExpression("abc");
        Expression arg2 = createStringExpression("abc");
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualStringFalse() {
        Expression arg1 = createStringExpression("abc");
        Expression arg2 = createStringExpression("def");
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualBooleanTrue() {
        Expression arg1 = createBooleanExpression(true);
        Expression arg2 = createBooleanExpression(true);
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualBooleanFalse() {
        Expression arg1 = createBooleanExpression(true);
        Expression arg2 = createBooleanExpression(false);
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgNull() {
        CoreOperationEqual op = new CoreOperationEqual(null, createNumberExpression(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArg1Null() {
        Expression arg1 = null;
        Expression arg2 = createNumberExpression(1);
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArg2Null() {
        Expression arg1 = createNumberExpression(1);
        Expression arg2 = null;
        CoreOperationEqual op = new CoreOperationEqual(arg1, arg2);
    }
}