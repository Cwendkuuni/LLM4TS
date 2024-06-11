package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @org.junit.Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with valid arguments
        Expression arg1 = new Literal(new Double(5.0));
        Expression arg2 = new Literal(new Double(10.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new ArrayList<>());
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);

        arg1 = new Literal(new Double(10.0));
        arg2 = new Literal(new Double(5.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new ArrayList<>());
        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result);

        arg1 = new Literal(new Double(5.0));
        arg2 = new Literal(new Double(5.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new ArrayList<>());
        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);

        arg1 = new Literal(new Double(NaN)); // Not-a-Number
        arg2 = new Literal(new Double(5.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new ArrayList<>());
        try {
            result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new Literal(new Double(5.0));
        arg2 = new Literal(new Double(NaN));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new ArrayList<>());
        try {
            result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with empty context
        context = new EvalContext(new ArrayList<>());
        arg1 = new Literal(new Double(5.0));
        arg2 = new Literal(new Double(10.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test inheritance
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(
                arg1, arg2);
        assertTrue(coreOperationLessThanOrEqual instanceof CoreOperationRelationalExpression);
        assertTrue(coreOperationRelationalExpression instanceof CoreOperationLessThanOrEqual);
    }
}