package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.PathParser;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.Operator;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactoryImpl;
import org.apache.commons.jxpath.ri.compiler.StaticContext;

import junit.framework.TestCase;

/**
 * Test cases for CoreOperationLessThan class
 */
public class CoreOperationLessThanTest extends TestCase {

    // Test constructor
    public void testCoreOperationLessThanConstructor() {
        Expression arg1 = ExpressionFactory.createLiteral(new Double(5.0));
        Expression arg2 = ExpressionFactory.createLiteral(new Double(10.0));
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(operation);
        assertInstanceof(CoreOperationLessThan.class, operation);
        assertEquals(Operator.LT, operation.getOperator());
        assertEquals(2, operation.getArgs().length);
    }

    // Test computeValue method with valid input
    public void testComputeValueValidInput() {
        Expression arg1 = ExpressionFactory.createLiteral(new Double(5.0));
        Expression arg2 = ExpressionFactory.createLiteral(new Double(10.0));
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new StaticContext();
        InfoSet infoSet = new PathParser("1.0").parse("1.0");
        context.setInfoSet(infoSet);
        Object result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new StaticContext();
        infoSet = new PathParser("5.0, 10.0").parse("5.0, 10.0");
        context.setInfoSet(infoSet);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new StaticContext();
        infoSet = new PathParser("2.5, 5.0").parse("2.5, 5.0");
        context.setInfoSet(infoSet);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Test computeValue method with invalid input
    public void testComputeValueInvalidInput() {
        Expression arg1 = ExpressionFactory.createLiteral(new Double(5.0));
        Expression arg2 = ExpressionFactory.createLiteral(null);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new StaticContext();
        InfoSet infoSet = new PathParser("1.0").parse("1.0");
        context.setInfoSet(infoSet);
        try {
            operation.computeValue(context);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg2 = ExpressionFactory.createLiteral(new Double(5.0));
        operation = new CoreOperationLessThan(ExpressionFactory.createVariable("x"), arg2);
        context = new StaticContext();
        infoSet = new PathParser("1.0").parse("1.0");
        context.setInfoSet(infoSet);
        try {
            operation.computeValue(context);
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    // Test getSymbol method
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.createLiteral(new Double(5.0));
        Expression arg2 = ExpressionFactory.createLiteral(new Double(10.0));
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", operation.getSymbol());
    }
}