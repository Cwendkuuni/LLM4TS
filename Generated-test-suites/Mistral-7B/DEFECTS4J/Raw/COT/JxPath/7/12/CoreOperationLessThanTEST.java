package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expression.Expression;

public class CoreOperationLessThanTest {

    @SuppressWarnings("unchecked")
    private static <T> Expression<T> createExpression(Class<T> clazz, Object... args) {
        return new CoreOperationLessThan(new FakeExpression<T>(args[0]),
                new FakeExpression<T>(args[1]));
    }

    @org.junit.Test
    public void testConstructor() {
        Expression<Object> exp = new CoreOperationLessThan(null, null);
        assertNotNull(exp);
    }

    @org.junit.Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object l = 3.5;
        Object r = 4.2;

        CoreOperationLessThan exp = new CoreOperationLessThan(new FakeExpression<Object>(l),
                new FakeExpression<Object>(r));

        Boolean result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;

        exp = new CoreOperationLessThan(new FakeExpression<Object>(l), new FakeExpression<Object>(r));

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>(r));

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$a"));

        result = (Boolean) exp.computeValue(context);
        assertTrue(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>(r));

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r);

        result = (Boolean) exp.computeValue(context);
        assertTrue(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r);

        result = (Boolean) exp.computeValue(context);
        assertTrue(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r + 0.1);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r + 0.1);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r + 0.1);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r + 0.1);

        result = (Boolean) exp.computeValue(context);
        assertTrue(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r + 0.1);

        result = (Boolean) exp.computeValue(context);
        assertFalse(result);

        l = 3.5;
        r = 3.5;
        context.setVariable("a", l);

        exp = new CoreOperationLessThan(new FakeExpression<Object>("$a"),
                new FakeExpression<Object>("$r"));

        context.setVariable("r", r + 0.1);

        result = (Boolean) exp.computeValue(context);
        assertTrue(result);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testComputeValueNullArgs() {
        EvalContext context = new EvalContext();
        CoreOperationLessThan exp = new CoreOperationLessThan(null, null);
        exp.computeValue(context);
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testComputeValueTypeMismatch() {
        EvalContext context = new EvalContext();
        Object l = 3.5;
        Object r = new String("test");

        CoreOperationLessThan exp = new CoreOperationLessThan(new FakeExpression<Object>(l),
                new FakeExpression<Object>(r));

        exp.computeValue(context);
    }

    @org.junit.Test
    public void testGetSymbol() {
        CoreOperationLessThan exp = new CoreOperationLessThan(null, null);
        assertEquals("<", exp.getSymbol());
    }
}

class FakeExpression<T> extends CoreOperationRelationalExpression<T> {
    private Object value;

    public FakeExpression(Object value) {
        super(new Expression[0]);
        this.value = value;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return value;
    }
}