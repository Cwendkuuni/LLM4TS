package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationLessThan lessThan;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = ExpressionFactory.createNumber(1.0);
        arg2 = ExpressionFactory.createNumber(2.0);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        context = TestUtils.newContext();
    }

    @Test
    public void testCoreOperationLessThanConstructor() {
        assertNotNull(lessThan);
    }

    @Test
    public void testComputeValueWhenArg1IsLessThanArg2() {
        InfoSet arg1InfoSet = TestUtils.newInfoSet();
        arg1InfoSet.setDoubleValue("x", arg1.computeValue(context).doubleValue());
        context.setInfoSet(arg1InfoSet);

        boolean result = lessThan.computeValue(context).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testComputeValueWhenArg1IsGreaterThanOrEqualArg2() {
        InfoSet arg1InfoSet = TestUtils.newInfoSet();
        arg1InfoSet.setDoubleValue("x", arg1.computeValue(context).doubleValue());
        context.setInfoSet(arg1InfoSet);

        boolean result = lessThan.computeValue(context).booleanValue();
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWhenArgumentsIsNull() {
        context.setInfoSet(null);
        lessThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArgumentsIsNotNumber() {
        context.setInfoSet(TestUtils.newInfoSet());
        Expression arg1 = ExpressionFactory.createVariable("x");
        Expression arg2 = ExpressionFactory.createString("y");
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        lessThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArgumentsHaveDifferentCardinality() {
        InfoSet arg1InfoSet = TestUtils.newInfoSet();
        arg1InfoSet.setDoubleValue("x", arg1.computeValue(context).doubleValue());
        context.setInfoSet(arg1InfoSet);

        InfoSet arg2InfoSet = TestUtils.newInfoSet();
        arg2InfoSet.setDoubleValue("y", arg2.computeValue(context).doubleValue());
        arg2InfoSet.setDoubleValue("z", 1.5);
        context.setInfoSet(arg2InfoSet);

        CoreOperationRelationalExpression lessThan = new CoreOperationLessThan(arg1, arg2);
        lessThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArgumentsHaveMismatchedTypes() {
        InfoSet arg1InfoSet = TestUtils.newInfoSet();
        arg1InfoSet.setDoubleValue("x", arg1.computeValue(context).doubleValue());
        context.setInfoSet(arg1InfoSet);

        InfoSet arg2InfoSet = TestUtils.newInfoSet();
        arg2InfoSet.setStringValue("y", arg2.computeValue(context).stringValue());
        context.setInfoSet(arg2InfoSet);

        CoreOperationRelationalExpression lessThan = new CoreOperationLessThan(arg1, arg2);
        lessThan.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        String symbol = lessThan.getSymbol();
        assertEquals("<", symbol);
    }
}