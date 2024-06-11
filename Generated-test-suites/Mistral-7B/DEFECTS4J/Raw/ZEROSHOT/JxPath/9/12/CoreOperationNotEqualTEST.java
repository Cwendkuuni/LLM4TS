package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.expr.ExpressionVisitor;
import org.apache.commons.jxpath.ri.expr.ExpressionVisitorResult;
import org.junit.Test;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        // Test with null arguments
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, null);
        assertNull(coreOperationNotEqual.getSymbol());
        assertNull(coreOperationNotEqual.computeValue(null));

        // Test with valid arguments
        Expression arg1 = new Path("$x[1]");
        Expression arg2 = new Path("$x[2]");
        CoreOperationNotEqual coreOperationNotEqualInstance = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(coreOperationNotEqualInstance);
        assertFalse(coreOperationNotEqualInstance.equals(arg1));
        assertFalse(coreOperationNotEqualInstance.equals(arg2));
        assertNotNull(coreOperationNotEqualInstance.getSymbol());
        assertTrue(coreOperationNotEqualInstance.toString().contains("!="));

        // Test computeValue method with different types
        EvalContext context = new EvalContext();
        Object arg1Value = 10;
        Object arg2Value = "hello";
        context.set("$x", new Object[] { arg1Value, arg2Value });

        Object result = coreOperationNotEqualInstance.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue() == false);

        // Test computeValue method with same types but different values
        arg1Value = "hello";
        arg2Value = "world";
        result = coreOperationNotEqualInstance.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue() == true);

        // Test computeValue method with same types and same values
        arg1Value = "hello";
        arg2Value = "hello";
        result = coreOperationNotEqualInstance.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue() == false);

        // Test computeValue method with same types but equal values and different context
        context = new EvalContext();
        result = coreOperationNotEqualInstance.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue() == false);

        context.set("$x", new Object[] { arg1Value, arg2Value });
        result = coreOperationNotEqualInstance.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue() == false);

        // Test computeValue method with same types but equal values and different contexts
        context = new EvalContext();
        Object[] x = new Object[] { arg1Value, arg2Value };
        context.set("$x", x);

        ExpressionVisitor visitor = new ExpressionVisitor() {
            @Override
            public ExpressionVisitorResult visit(CoreOperationNotEqual node) {
                node.args = new Expression[] { node.args[0], new Path("$x[1]") };
                return super.visit(node);
            }
        };

        Expression coreOperationNotEqualInstance2 = visitor.visit(coreOperationNotEqualInstance);
        result = coreOperationNotEqualInstance2.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue() == false);

        context.set("$x", new Object[] { arg2Value, arg1Value });
        result = coreOperationNotEqualInstance2.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue() == true);
    }

}