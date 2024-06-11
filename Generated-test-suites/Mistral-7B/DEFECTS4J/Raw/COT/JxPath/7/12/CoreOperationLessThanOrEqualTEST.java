package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression(10.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(""), infoSet);
        assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(context));

        context = new EvalContext(new Path(""), infoSet);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(5.0));
        assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(context));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression(5.0));
        assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(context));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression("5.0"), new ConstantExpression("10.0"));
        assertFalse(coreOperationLessThanOrEqual.computeValue(context) instanceof Boolean);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqualWithMixedTypes() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression("10.0"));
        coreOperationLessThanOrEqual.computeValue(new EvalContext(new Path(""), new InfoSet()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithNullArgument() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                null, new ConstantExpression(5.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithNullSecondArgument() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(5.0), null);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression(10.0));
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }
}