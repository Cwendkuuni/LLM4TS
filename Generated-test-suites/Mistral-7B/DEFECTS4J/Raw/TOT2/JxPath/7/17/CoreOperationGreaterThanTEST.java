// CoreOperationGreaterThanTest.java
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;
import static org.junit.experimental.categories.Categories.IntegrationTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;

public class CoreOperationGreaterThanTest {

    @Test
    @Category(IntegrationTest.class)
    public void testComputeValue() {
        Expression arg1 = new Constant(5.5);
        Expression arg2 = new Constant(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    // Edge case for non-numeric values
    @Test
    @Category(IntegrationTest.class)
    public void testNonNumericValues() {
        Expression arg1 = new Constant("3");
        Expression arg2 = new Constant("5");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        try {
            Object result = coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Edge case for negative numbers
    @Test
    @Category(IntegrationTest.class)
    public void testNegativeNumbers() {
        Expression arg1 = new Constant(-5.5);
        Expression arg2 = new Constant(-3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    // Exception handling for null EvalContext
    @Test(expected = IllegalArgumentException.class)
    @Category(IntegrationTest.class)
    public void testNullEvalContext() {
        Expression arg1 = new Constant(5.5);
        Expression arg2 = new Constant(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = null;
        coreOperationGreaterThan.computeValue(context);
    }
}

//