package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ExpType;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.ContextFactory;
import org.apache.commons.jxpath.ri.core.SimpleNode;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    private Context context;
    private ContextFactory contextFactory;

    @Before
    public void setUp() {
        contextFactory = new ContextFactory();
        context = contextFactory.createContext();
    }

    @Test
    public void testComputeValue_whenBothExpressionsHaveBooleanTrueValues_thenReturnBooleanTrue() {
        Expression arg1 = new SimpleNode("true", ExpType.BOOLEAN, null);
        Expression arg2 = new SimpleNode("true", ExpType.BOOLEAN, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothExpressionsHaveBooleanFalseValues_thenReturnBooleanFalse() {
        Expression arg1 = new SimpleNode("false", ExpType.BOOLEAN, null);
        Expression arg2 = new SimpleNode("false", ExpType.BOOLEAN, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstExpressionHasBooleanTrueValueAndSecondExpressionHasIntegerValue_thenReturnBooleanFalse() {
        Expression arg1 = new SimpleNode("true", ExpType.BOOLEAN, null);
        Expression arg2 = new SimpleNode("42", ExpType.NUMBER, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstExpressionHasIntegerValueAndSecondExpressionHasBooleanTrueValue_thenReturnBooleanFalse() {
        Expression arg1 = new SimpleNode("42", ExpType.NUMBER, null);
        Expression arg2 = new SimpleNode("true", ExpType.BOOLEAN, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstExpressionHasIntegerValueAndSecondExpressionHasIntegerValueWithEqualValues_thenReturnBooleanTrue() {
        Expression arg1 = new SimpleNode("42", ExpType.NUMBER, null);
        Expression arg2 = new SimpleNode("42", ExpType.NUMBER, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstExpressionHasIntegerValueAndSecondExpressionHasIntegerValueWithDifferentValues_thenReturnBooleanFalse() {
        Expression arg1 = new SimpleNode("42", ExpType.NUMBER, null);
        Expression arg2 = new SimpleNode("43", ExpType.NUMBER, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}