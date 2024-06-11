package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri. expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.core.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.expressions.core.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expressions.literals.NumberLiteral;
import org.junit.Test;

/**
 * CoreOperationGreaterThanOrEqualTest class for testing CoreOperationGreaterThanOrEqual class
 */
public class CoreOperationGreaterThanOrEqualTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        //create test expressions
        Expression arg1 = new NumberLiteral(2.5);
        Expression arg2 = new NumberLiteral(3.5);
        //create test object
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        //check object is instance of CoreOperationGreaterThanOrEqual
        assertTrue("Object is not instance of CoreOperationGreaterThanOrEqual",
                coreOperation instanceof CoreOperationGreaterThanOrEqual);
        //check object is instance of CoreOperationRelationalExpression
        assertTrue("Object is not instance of CoreOperationRelationalExpression",
                coreOperation instanceof CoreOperationRelationalExpression);
        //check object has two arguments
        assertEquals("Number of arguments is not correct", 2, coreOperation.getArgCount());
        //check arguments are set correctly
        assertEquals("Argument 1 is not set correctly", arg1, coreOperation.getArg(0));
        assertEquals("Argument 2 is not set correctly", arg2, coreOperation.getArg(1));
    }

    /**
     * Test method computeValue.
     */
    @Test
    public void testComputeValue() {
        //create test expressions
        Expression arg1 = new NumberLiteral(2.5);
        Expression arg2 = new NumberLiteral(3.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        //test with valid inputs
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(1, "root", null));
        Object result = coreOperation.computeValue(context);
        assertTrue("Result is not correct", result instanceof Boolean);
        assertEquals("Result is not correct", Boolean.TRUE, result);

        //test with invalid inputs
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(1, "root", null));
        arg1 = new NumberLiteral("invalid");
        arg2 = new NumberLiteral(3.5);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertFalse("Result is not correct", result instanceof Boolean);
    }

    /**
     * Test method getSymbol.
     */
    @Test
    public void testGetSymbol() {
        //create test expression
        Expression arg1 = new NumberLiteral(2.5);
        Expression arg2 = new NumberLiteral(3.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        //test symbol is set correctly
        assertEquals("Symbol is not set correctly", ">=", coreOperation.getSymbol());
    }

    /**
     * Test method computeValue with negative numbers.
     */
    @Test
    public void testComputeValueWithNegativeNumbers() {
        //create test expressions
        Expression arg1 = new NumberLiteral(-2.5);
        Expression arg2 = new NumberLiteral(-3.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        //test with valid inputs
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(1, "root", null));
        Object result = coreOperation.computeValue(context);
        assertTrue("Result is not correct", result instanceof Boolean);
        assertEquals("Result is not correct", Boolean.FALSE, result);

        arg1 = new NumberLiteral(-2.5);
        arg2 = new NumberLiteral(-3.5);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context.setCurrentNode(new NodeInfo(1, "root", null));
        result = coreOperation.computeValue(context);
        assertTrue("Result is not correct", result instanceof Boolean);
        assertEquals("Result is not correct", Boolean.TRUE, result);
    }

    /**
     * Test method computeValue with zero as left operand.
     */
    @Test
    public void testComputeValueWithZeroAsLeftOperand() {
        //create test expressions
        Expression arg1 = new NumberLiteral(0.0);
        Expression arg2 = new NumberLiteral(3.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        //test with valid inputs
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(1, "root", null));
        Object result = coreOperation.computeValue(context);
        assertFalse("Result is not correct", result instanceof Boolean);

        arg1 = new NumberLiteral(0.0);
        arg2 = new NumberLiteral(0.0);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context.setCurrentNode(new NodeInfo(1, "root", null));
        result = coreOperation.computeValue(context);
        assertTrue("Result is not correct", result instanceof Boolean);
        assertEquals("Result is not correct", Boolean.TRUE, result);
    }
}