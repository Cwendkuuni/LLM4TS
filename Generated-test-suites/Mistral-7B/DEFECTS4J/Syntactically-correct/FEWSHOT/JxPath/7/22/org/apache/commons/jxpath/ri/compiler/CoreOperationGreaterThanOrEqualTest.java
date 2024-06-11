package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with double values
        assertTrue(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createConstantExpression(1.5),
                TestUtil.createConstantExpression(1.5)
        ).computeValue(new EvalContext()));

        assertTrue(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createConstantExpression(2.5),
                TestUtil.createConstantExpression(1.5)
        ).computeValue(new EvalContext()));

        assertFalse(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createConstantExpression(1.5),
                TestUtil.createConstantExpression(2.5)
        ).computeValue(new EvalContext()));

        assertFalse(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createConstantExpression(-1.5),
                TestUtil.createConstantExpression(1.5)
        ).computeValue(new EvalContext()));

        // Test with expressions
        assertTrue(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createFieldExpression("num", 1),
                TestUtil.createConstantExpression(1.5)
        ).computeValue(new EvalContext()));

        assertTrue(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createMultiplicativeExpression(
                        TestUtil.createFieldExpression("num", 1),
                        TestUtil.createConstantExpression(2.0)
                ),
                TestUtil.createConstantExpression(3.0)
        ).computeValue(new EvalContext()));

        assertFalse(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createFieldExpression("num", 1),
                TestUtil.createMultiplicativeExpression(
                        TestUtil.createFieldExpression("num", 1),
                        TestUtil.createConstantExpression(1.5)
                )
        ).computeValue(new EvalContext()));

        assertFalse(CoreOperationGreaterThanOrEqual.class.newInstance(
                TestUtil.createConstantExpression(1.5),
                TestUtil.createFieldExpression("num", 1)
        ).computeValue(new EvalContext()));
    }
}