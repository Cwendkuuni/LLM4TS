package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.AbstractJxPathTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.test.TestHelper;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest extends AbstractJxpathTest {

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualConstructor() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(2.0);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(coreOperation);
        assertEquals(CoreOperationGreaterThanOrEqual.class, coreOperation.getClass());
        assertEquals(2, coreOperation.getArgs().length);
    }

    /**
     * Test computeValue method with double values when the condition is true.
     */
    @Test
    public void testComputeValueTrue() {
        Expression arg1 = TestHelper.createNumberExpression(5.0);
        Expression arg2 = TestHelper.createNumberExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = TestHelper.createTestInfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with double values when the condition is false.
     */
    @Test
    public void testComputeValueFalse() {
        Expression arg1 = TestHelper.createNumberExpression(3.0);
        Expression arg2 = TestHelper.createNumberExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = TestHelper.createTestInfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with different data types.
     */
    @Test(expected = ClassCastException.class)
    public void testComputeValueDifferentDataTypes() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createStringExpression("2");
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = TestHelper.createTestInfoSet();
        context.setInfoSet(infoSet);
        coreOperation.computeValue(context);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(2.0);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String result = coreOperation.getSymbol();
        assertEquals(">=", result);
    }
}