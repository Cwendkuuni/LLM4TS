package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationEqual}.
 *
 * @author Dmitri Plotnikov
 */
public class CoreOperationEqualTest extends CoreOperationTest {

    /**
     * Constructor for CoreOperationEqualTest.
     */
    public CoreOperationEqualTest() {
        super();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("Expected CoreOperationEqual, but got: " + coreOperationEqual,
                CoreOperationEqual.class, coreOperationEqual.getClass());
        assertEquals("Expected arg1, but got: " + coreOperationEqual.getArgs()[0], arg1, coreOperationEqual.getArgs()[0]);
        assertEquals("Expected arg2, but got: " + coreOperationEqual.getArgs()[1], arg2, coreOperationEqual.getArgs()[1]);
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("1");
        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(arg1, arg2);
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(arg1, arg2);
        assertTrue("Expected equals, but got: " + coreOperationEqual1.equals(coreOperationEqual2), coreOperationEqual1.equals(coreOperationEqual2));
        assertFalse("Expected not equals, but got: " + coreOperationEqual1.equals(createExpression("1")), coreOperationEqual1.equals(createExpression("1")));
    }

    /**
     * Test getSymbol.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("Expected '=', but got: " + coreOperationEqual.getSymbol(), "=", coreOperationEqual.getSymbol());
    }

    /**
     * Test computeValue.
     */
    @Test
    public void testComputeValueTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertEquals("Expected true, but got: " + result, Boolean.TRUE, result);
    }

    /**
     * Test computeValue False.
     */
    @Test
    public void testComputeValueFalse() {
        EvalContext context = new EvalContext();
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected false, but got: " + result, result instanceof Boolean);
        assertEquals("Expected false, but got: " + result, Boolean.FALSE, result);
    }

    /**
     * Test computeValue null.
     */
    @Test
    public void testComputeValueNull() {
        EvalContext context = new EvalContext();
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("null");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertNull("Expected null, but got: " + result, result);
    }

    /**
     * Test computeValue different types.
     */
    @Test
    public void testComputeValueDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("true");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
        assertNull("Expected null, but got: " + result, result);
    }

    /**
     * Test computeValue same types.
     */
    @Test
    public void testComputeValueSameTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("1.0");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertEquals("Expected true, but got: " + result, Boolean.TRUE, result);
    }
}