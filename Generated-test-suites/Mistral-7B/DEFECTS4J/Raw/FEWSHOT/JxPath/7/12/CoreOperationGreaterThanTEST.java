package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new ConstantExpression(5.0),
                new ConstantExpression(3.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeNumbers() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new ConstantExpression(-3.0),
                new ConstantExpression(-2.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanWithZero() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new ConstantExpression(0.0),
                new ConstantExpression(5.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanWithEqualValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new ConstantExpression(5.0),
                new ConstantExpression(5.0)
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNull() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new ConstantExpression(5.0),
                new NullExpression()
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}