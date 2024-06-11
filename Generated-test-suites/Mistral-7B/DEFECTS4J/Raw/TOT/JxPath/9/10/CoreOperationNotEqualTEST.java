import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual.EvalContext;
import org.apache.commons.jxpath.ri.expressions.Constant;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqualBoolean() {
        Expression arg1 = new Constant(Boolean.TRUE);
        Expression arg2 = new Constant(Boolean.FALSE);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotEqualNumbers() {
        Expression arg1 = new Constant(1);
        Expression arg2 = new Constant(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNotEqualStrings() {
        Expression arg1 = new Constant("apple");
        Expression arg2 = new Constant("banana");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotEqualNullAndNotNull() {
        Expression arg1 = new Constant(null);
        Expression arg2 = new Constant("not null");
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotEqualNullAndNull() {
        Expression arg1 = new Constant(null);
        Expression arg2 = new Constant(null);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqualWithSameExpression() {
        Expression arg1 = new Constant(1);
        Expression arg2 = arg1;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertFalse(notEqual.isInitialized());
    }
}