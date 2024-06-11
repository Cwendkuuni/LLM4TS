package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    private InfoSet infoSet;
    private EvalContext context;
    private Expression arg1, arg2;
    private CoreOperationGreaterThan coreOperationGreaterThan;

    @Before
    public void setUp() {
        infoSet = InfoSetUtil.createInfoSet();
        context = new EvalContext(infoSet);
        arg1 = new ConstantExpression(1.0);
        arg2 = new ConstantExpression(2.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testCoreOperationGreaterThanFunctionality() {
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithEqualValues() {
        arg1 = new ConstantExpression(2.0);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeValues() {
        arg1 = new ConstantExpression(-1.0);
        arg2 = new ConstantExpression(-2.0);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanWithNonNumericValues() {
        arg1 = new ConstantExpression("string1");
        arg2 = new ConstantExpression("string2");
        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanWithNullValues() {
        arg1 = new ConstantExpression(null);
        arg2 = new ConstantExpression(2.0);
        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanWithMixedTypes() {
        arg1 = new ConstantExpression(1.0);
        arg2 = new ConstantExpression("string2");
        coreOperationGreaterThan.computeValue(context);
    }
}