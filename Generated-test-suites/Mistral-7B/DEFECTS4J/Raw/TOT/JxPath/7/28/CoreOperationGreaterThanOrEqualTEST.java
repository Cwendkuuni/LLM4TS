import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;
import org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testGreaterThanOrEqualDouble() {
        // Given
        Expression arg1 = new SimpleNode(new Double(5.0));
        Expression arg2 = new SimpleNode(new Double(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanDouble() {
        // Given
        Expression arg1 = new SimpleNode(new Double(5.0));
        Expression arg2 = new SimpleNode(new Double(6.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testEqualDouble() {
        // Given
        Expression arg1 = new SimpleNode(new Double(5.0));
        Expression arg2 = new SimpleNode(new Double(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNaNAndNaN() {
        // Given
        Expression arg1 = new SimpleNode(Double.NaN);
        Expression arg2 = new SimpleNode(Double.NaN);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test(expected = ClassCastException.class)
    public void testArg1NotANumber() {
        // Given
        Expression arg1 = new SimpleNode(new String("5"));
        Expression arg2 = new SimpleNode(new Double(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        expression.computeValue(context);

        // Then
        fail("Expected ClassCastException but it didn't occur.");
    }

    @Test(expected = ClassCastException.class)
    public void testArg2NotANumber() {
        // Given
        Expression arg1 = new SimpleNode(new Double(5.0));
        Expression arg2 = new SimpleNode(new String("5"));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        expression.computeValue(context);

        // Then
        fail("Expected ClassCastException but it didn't occur.");
    }

    @Test
    public void testGreaterThanOrEqualBigDecimal() {
        // Given
        Expression arg1 = new SimpleNode(new BigDecimal("5.0"));
        Expression arg2 = new SimpleNode(new BigDecimal("5.0"));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanBigDecimal() {
        // Given
        Expression arg1 = new SimpleNode(new BigDecimal("5.0"));
        Expression arg2 = new SimpleNode(new BigDecimal("6.0"));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testEqualBigDecimal() {
        // Given
        Expression arg1 = new SimpleNode(new BigDecimal("5.0"));
        Expression arg2 = new SimpleNode(new BigDecimal("5.0"));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanOrEqualNullAndDouble() {
        // Given
        Expression arg1 = new SimpleNode(null);
        Expression arg2 = new SimpleNode(new Double(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testGreaterThanOrEqualDoubleAndNull() {
        // Given
        Expression arg1 = new SimpleNode(new Double(5.0));
        Expression arg2 = new SimpleNode(null);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    @Test
    public void testGreaterThanOrEqualNullAndNull() {
        // Given
        Expression arg1 = new SimpleNode(null);
        Expression arg2 = new SimpleNode(null);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }
}