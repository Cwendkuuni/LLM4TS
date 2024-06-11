package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.datatypes.XSDTypeLong;
import org.apache.commons.jxpath.ri.datatypes.XSDTypeDouble;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.apache.commons.jxpath.ri.compiler.CoreArguments;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with double values
        Expression arg1 = new CoreConstantExpression(new NodeInfo(1.0), new XSDTypeDouble());
        Expression arg2 = new CoreConstantExpression(new NodeInfo(2.0), new XSDTypeDouble());
        CoreArguments arguments = new CoreArguments(new Expression[] { arg1, arg2 });
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arguments.getExpressionArray());
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with long values
        arg1 = new CoreConstantExpression(new NodeInfo(3L), new XSDTypeLong());
        arg2 = new CoreConstantExpression(new NodeInfo(2L), new XSDTypeLong());
        arguments = new CoreArguments(new Expression[] { arg1, arg2 });
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arguments.getExpressionArray());
        context = new EvalContext();
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with boolean values
        arg1 = new CoreConstantExpression(new NodeInfo(true), new XSDTypeBoolean());
        arg2 = new CoreConstantExpression(new NodeInfo(false), new XSDTypeBoolean());
        arguments = new CoreArguments(new Expression[] { arg1, arg2 });
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arguments.getExpressionArray());
        context = new EvalContext();
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with null values
        arg1 = new CoreConstantExpression(null, null);
        arg2 = new CoreConstantExpression(new NodeInfo(2.0), new XSDTypeDouble());
        arguments = new CoreArguments(new Expression[] { arg1, arg2 });
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arguments.getExpressionArray());
        context = new EvalContext();
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test with different datatypes
        arg1 = new CoreConstantExpression(new NodeInfo(1.0), new XSDTypeDouble());
        arg2 = new CoreConstantExpression(new NodeInfo(2), new XSDTypeInt());
        arguments = new CoreArguments(new Expression[] { arg1, arg2 });
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arguments.getExpressionArray());
        context = new EvalContext();
        try {
            coreOperationGreaterThanOrEqual.computeValue(context);
            fail("Should throw an exception, as types are not comparable.");
        } catch (ClassCastException e) {
            // Expected
        }

        // Test with expressions that throw an exception
        arg1 = new CoreThrowingExpression();
        arg2 = new CoreConstantExpression(new NodeInfo(2.0), new XSDTypeDouble());
        arguments = new CoreArguments(new Expression[] { arg1, arg2 });
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arguments.getExpressionArray());
        context = new EvalContext();
        try {
            coreOperationGreaterThanOrEqual.computeValue(context);
            fail("Should throw an exception if one of the expressions throws an exception.");
        } catch (RuntimeException e) {
            // Expected
        }

        // Test with expressions with side-effects
        Expression sideEffectArg1 = new CoreSideEffectExpression();
        Expression sideEffectArg2 = new CoreConstantExpression(new NodeInfo(2.0), new XSDTypeDouble());
        arguments = new CoreArguments(new Expression[] { sideEffectArg1, sideEffectArg2 });
        CoreOperationGreaterThanOrEqual sideEffectCoreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arguments.getExpressionArray());
        context = new EvalContext();
        // Compute value should not have side-effects
        Object sideEffectResult = sideEffectCoreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(sideEffectResult instanceof Boolean);
        assertFalse((Boolean) sideEffectResult);
        // Side-effect should have been executed
        assertTrue(sideEffectArg1.getSideEffectCount() > 0);
    }

    /**
     * Expression that throws an exception.
     */
    private static class CoreThrowingExpression extends CoreExpression {
        public Object computeValue(EvalContext context) {
            throw new RuntimeException();
        }
    }

    /**
     * Expression with a side-effect.
     */
    private static class CoreSideEffectExpression extends CoreExpression {
        private int sideEffectCount = 0;

        public Object computeValue(EvalContext context) {
            sideEffectCount++;
            return null;
        }

        public int getSideEffectCount() {
            return sideEffectCount;
        }
    }

    /**
     * Create a test InfoSet.
     *
     * @return InfoSet with two NodeInfos.
     */
    private static InfoSet createTestInfoSet() {
        NodeInfo nodeInfo1 = new NodeInfo(new Object());
        NodeInfo nodeInfo2 = new NodeInfo(new Object());
        return new InfoSet(new Object[] { nodeInfo1, nodeInfo2 });
    }

    /**
     * Create a Test EvalContext with a Test InfoSet.
     *
     * @return EvalContext with Test InfoSet.
     */
    private static EvalContext createTestEvalContext() {
        return new EvalContext(createTestInfoSet());
    }
}