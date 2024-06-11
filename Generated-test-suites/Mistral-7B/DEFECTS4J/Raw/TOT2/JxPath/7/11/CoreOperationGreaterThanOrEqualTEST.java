package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        InfoSet is = TestUtils.newDoubleInfoSet(new double[]{1.5, 2.0});
        EvalContext context = TestUtils.newContext(is);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(1.5), new CoreNumberExpression(2.0));
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        is = TestUtils.newDoubleInfoSet(new double[]{2.5, 2.0});
        context = TestUtils.newContext(is);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(2.5), new CoreNumberExpression(2.0));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        is = TestUtils.newDoubleInfoSet(new double[]{Double.NaN, Double.NaN});
        context = TestUtils.newContext(is);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(Double.NaN), new CoreNumberExpression(Double.NaN));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result); // NaN is not greater than or equal to NaN

        is = TestUtils.newDoubleInfoSet(new double[]{Double.NaN, 2.0});
        context = TestUtils.newContext(is);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(Double.NaN), new CoreNumberExpression(2.0));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertNull(result); // NaN is not comparable with a number

        is = TestUtils.newDoubleInfoSet(new double[]{-1.5, 2.0});
        context = TestUtils.newContext(is);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(-1.5), new CoreNumberExpression(2.0));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}