package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.info.InfoSet;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThanWithNumbers() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));

        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanWithEqualNumbers() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreConstantExpression(1.0), new CoreConstantExpression(1.0));

        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testLessThanWithNegativeNumbers() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreConstantExpression(-1.0), new CoreConstantExpression(-2.0));

        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanWithMixedNumbers() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreConstantExpression(1.0), new CoreConstantExpression(-1.0));

        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanWithNullAndNumber() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreConstantExpression(1.0), new CoreNullExpression());

        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanWithNullAndNull() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreNullExpression(), new CoreNullExpression());

        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testLessThanWithStringValues() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreConstantExpression("1"), new CoreConstantExpression("2"));

        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}