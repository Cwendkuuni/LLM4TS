package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestHelper;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                TestHelper.createLiteralExpression(2.0),
                TestHelper.createLiteralExpression(3.0));

        EvalContext context = TestHelper.createContext();

        Object result = op.computeValue(context);

        assertFalse("Expected false", result instanceof Boolean ? ((Boolean) result).booleanValue() : false);

        op = new CoreOperationLessThanOrEqual(
                TestHelper.createLiteralExpression(1.0),
                TestHelper.createLiteralExpression(1.0));

        result = op.computeValue(context);

        assertTrue("Expected true", result instanceof Boolean ? ((Boolean) result).booleanValue() : false);

        op = new CoreOperationLessThanOrEqual(
                TestHelper.createLiteralExpression(2.0),
                TestHelper.createLiteralExpression(2.0));

        result = op.computeValue(context);

        assertTrue("Expected true", result instanceof Boolean ? ((Boolean) result).booleanValue() : false);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithMixedTypes() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                TestHelper.createLiteralExpression(2.0),
                TestHelper.createLiteralExpression("3"));

        EvalContext context = TestHelper.createContext();

        Object result = op.computeValue(context);

        assertFalse("Expected false", result instanceof Boolean ? ((Boolean) result).booleanValue() : false);

        op = new CoreOperationLessThanOrEqual(
                TestHelper.createLiteralExpression("1"),
                TestHelper.createLiteralExpression(1.0));

        result = op.computeValue(context);

        assertTrue("Expected true", result instanceof Boolean ? ((Boolean) result).booleanValue() : false);

        op = new CoreOperationLessThanOrEqual(
                TestHelper.createLiteralExpression(1.0),
                TestHelper.createLiteralExpression("2"));

        result = op.computeValue(context);

        assertFalse("Expected false", result instanceof Boolean ? ((Boolean) result).booleanValue() : false);
    }
}