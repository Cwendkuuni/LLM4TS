package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.CoreExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;

import java.math.BigDecimal;

public class CoreOperationLessThanTest {

    @org.junit.Test
    public void testLessThanDoublePositive() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Double(5.5));
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanDoubleNegative() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Double(-3.0));
        Expression arg2 = new CoreConstantExpression(new Double(5.5));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanDoubleZero() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Double(0.0));
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanFloatPositive() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Float(5.5f));
        Expression arg2 = new CoreConstantExpression(new Float(3.0f));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanFloatNegative() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Float(-3.0f));
        Expression arg2 = new CoreConstantExpression(new Float(5.5f));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanFloatZero() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Float(0.0f));
        Expression arg2 = new CoreConstantExpression(new Float(3.0f));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanBigDecimalPositive() {
        // Given
        Expression arg1 = new CoreConstantExpression(new BigDecimal("5.5"));
        Expression arg2 = new CoreConstantExpression(new BigDecimal("3.0"));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanBigDecimalNegative() {
        // Given
        Expression arg1 = new CoreConstantExpression(new BigDecimal("-3.0"));
        Expression arg2 = new CoreConstantExpression(new BigDecimal("5.5"));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanBigDecimalZero() {
        // Given
        Expression arg1 = new CoreConstantExpression(new BigDecimal("0.0"));
        Expression arg2 = new CoreConstantExpression(new BigDecimal("3.0"));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testLessThanString() {
        // Given
        Expression arg1 = new CoreConstantExpression(new String("5.5"));
        Expression arg2 = new CoreConstantExpression(new String("3.0"));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        op.computeValue(context);

        // Then (Exception should be thrown)
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testLessThanNodeInfo() {
        // Given
        Expression arg1 = new CoreConstantExpression(new NodeInfo(new Object(), new String("node1"), 1, 1, null));
        Expression arg2 = new CoreConstantExpression(new NodeInfo(new Object(), new String("node2"), 2, 2, null));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        op.computeValue(context);

        // Then (Exception should be thrown)
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLessThanNullExpression() {
        // Given
        Expression arg1 = null;
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());

        // Then (Exception should be thrown)
        op.computeValue(context);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLessThanNullArg1() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Object());
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());

        // Then (Exception should be thrown)
        op.computeValue(context);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLessThanNullArg2() {
        // Given
        Expression arg1 = new CoreConstantExpression(new Double(3.0));
        Expression arg2 = null;
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet(), new Path());

        // Then (Exception should be thrown)
        op.computeValue(context);
    }
}