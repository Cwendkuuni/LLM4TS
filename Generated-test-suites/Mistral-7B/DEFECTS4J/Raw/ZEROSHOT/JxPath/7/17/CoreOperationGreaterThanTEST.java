package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with double values
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new Path("/"), new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(new Double(3.0));
        arg2 = new LiteralExpression(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(new Double(3.0));
        arg2 = new LiteralExpression(new Double(3.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with CoreExpression subclasses
        Expression arg1Core = new CoreVariableExpression("x");
        Expression arg2Core = new CoreNumberExpression(new Double(5.0));
        CoreOperationGreaterThan coreOperationGreaterThanCore = new CoreOperationGreaterThan(arg1Core, arg2Core);
        context = new EvalContext(new Path("/"), new InfoSet());
        context.set("x", new Double(3.0));
        result = coreOperationGreaterThanCore.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("x", new Double(5.0));
        result = coreOperationGreaterThanCore.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1Core = new CoreVariableExpression("x");
        arg2Core = new CoreNumberExpression(new Double(3.0));
        coreOperationGreaterThanCore = new CoreOperationGreaterThan(arg1Core, arg2Core);
        context = new EvalContext(new Path("/"), new InfoSet());
        context.set("x", new Double(5.0));
        result = coreOperationGreaterThanCore.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.set("x", new Double(3.0));
        result = coreOperationGreaterThanCore.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with CoreExpression subclasses and different types
        Expression arg1TypeMismatch = new CoreVariableExpression("x");
        Expression arg2TypeMismatch = new CoreStringExpression("5");
        CoreOperationGreaterThan coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        context.set("x", new Double(3.0));
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreNumberExpression(new Double(3.0));
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreStringExpression("3");
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreNumberExpression(new Double(3.0));
        arg2TypeMismatch = new CoreStringExpression("5");
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreNumberExpression(new Double(3.0));
        arg2TypeMismatch = new CoreNumberExpression(new Double("5"));
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreStringExpression("3");
        arg2TypeMismatch = new CoreNumberExpression(new Double(5.0));
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreNumberExpression(new Double(3.0));
        arg2TypeMismatch = new CoreStringExpression("5");
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreStringExpression("3.0");
        arg2TypeMismatch = new CoreStringExpression("5.0");
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreNumberExpression(new Double(3.0));
        arg2TypeMismatch = new CoreStringExpression("5.0");
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1TypeMismatch = new CoreNumberExpression(new Double(3.0));
        arg2TypeMismatch = new CoreNumberExpression(new Double("5.0"));
        coreOperationGreaterThanTypeMismatch = new CoreOperationGreaterThan(arg1TypeMismatch, arg2TypeMismatch);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanTypeMismatch.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        // Test with single argument
        Expression argNull = new CoreNullExpression();
        CoreOperationGreaterThan coreOperationGreaterThanNull = new CoreOperationGreaterThan(argNull);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanNull.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        argNull = new CoreNumberExpression(new Double(null));
        coreOperationGreaterThanNull = new CoreOperationGreaterThan(argNull);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanNull.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        argNull = new CoreStringExpression(null);
        coreOperationGreaterThanNull = new CoreOperationGreaterThan(argNull);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanNull.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        argNull = new CoreBooleanExpression(false);
        coreOperationGreaterThanNull = new CoreOperationGreaterThan(argNull);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThanNull.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        argNull = new CoreExpression[] {new CoreNullExpression()};
        CoreOperationRelationalExpression coreOperationRelationalExpressionNull = new CoreOperationRelationalExpression(argNull);
        coreOperationGreaterThan = new CoreOperationGreaterThan(coreOperationRelationalExpressionNull);
        context = new EvalContext(new Path("/"), new InfoSet());
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}