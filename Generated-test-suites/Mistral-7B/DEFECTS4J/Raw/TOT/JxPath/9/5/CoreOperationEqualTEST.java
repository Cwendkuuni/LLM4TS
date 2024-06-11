import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Literal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test for CoreOperationEqual class.
 */
public class CoreOperationEqualTest {

    @Test
    public void testEqualTrue() {
        Expression arg1 = new Literal(1);
        Expression arg2 = new Literal(1);
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = opEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualFalse() {
        Expression arg1 = new Literal(1);
        Expression arg2 = new Literal(2);
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = opEqual.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualNull1() {
        Expression arg1 = new Literal(null);
        Expression arg2 = new Literal(1);
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = opEqual.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualNull2() {
        Expression arg1 = new Literal(1);
        Expression arg2 = new Literal(null);
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = opEqual.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualNullBoth() {
        Expression arg1 = new Literal(null);
        Expression arg2 = new Literal(null);
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = opEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnequalTypes() {
        Expression arg1 = new Literal(1);
        Expression arg2 = new Literal("str");
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
    }
}