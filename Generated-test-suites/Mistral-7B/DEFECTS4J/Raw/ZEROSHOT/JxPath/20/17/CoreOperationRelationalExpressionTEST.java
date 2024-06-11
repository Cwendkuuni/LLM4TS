package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThanOrEqual;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test for subclass {@link CoreOperationGreaterThan}.
     */
    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(createExpression("5"), createExpression("3"));
        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5)));
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5, 6)));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new InitialContext(Collections.singletonList(new Object[] { new Integer(5), new Integer(5) })));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(5.1), new Double(3.1) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(5.1), new Double(5.1) })));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for subclass {@link CoreOperationGreaterThanOrEqual}.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(createExpression("5"), createExpression("3"));
        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5)));
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5, 6)));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Collections.singletonList(new Object[] { new Integer(5), new Integer(5) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(5.1), new Double(3.1) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(3.1), new Double(5.1) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for subclass {@link CoreOperationLessThan}.
     */
    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan op = new CoreOperationLessThan(createExpression("3"), createExpression("5"));
        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5)));
        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5, 6)));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new InitialContext(Collections.singletonList(new Object[] { new Integer(5), new Integer(5) })));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(3.1), new Double(5.1) })));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(5.1), new Double(3.1) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for subclass {@link CoreOperationLessThanOrEqual}.
     */
    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(createExpression("3"), createExpression("5"));
        EvalContext context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5)));
        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new InitialContext(Arrays.asList(1, 2, 3, 5, 6)));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Collections.singletonList(new Object[] { new Integer(5), new Integer(5) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(3.1), new Double(5.1) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InitialContext(Arrays.asList(new Object[] { new Double(5.1), new Double(3.1) })));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Creates a {@link Expression} based on the given expression string.
     * @param expression string representation of an expression
     * @return the corresponding {@link Expression} object
     */
    private Expression createExpression(String expression) {
        return new CoreFunctionCallExpression("number", Arrays.asList(new Expression[] { new StringExpression(expression) }));
    }

}