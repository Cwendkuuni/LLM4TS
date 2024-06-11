import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.javex.Expression;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.NotFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.utils.FunctionExpression;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        String s = "2 + 2 * 3";
        expression = new Expression(s);
    }

    @Test
    public void testGetExternalFunctions() {
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new AbsFunction());
        externalFunctions.add(new DateAddFunction());
        externalFunctions.add(new DivideFunction());

        expression.externalFunctions = externalFunctions;

        List<FunctionExpression> result = expression.getExternalFunctions();
        assertEquals(externalFunctions, result);
    }

    @Test
    public void testExpressionInitialization() {
        String s = "2 + 2 * 3";
        expression = new Expression(s);

        assertEquals(s, expression.expression);
        assertNull(expression.stack);
        assertNull(expression.externalFunctions);
        assertFalse(expression.prepared);
    }

    @Test
    public void testDoEvaluationStack() {
        expression.stack = new ExpressionStack();
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add("*");
        expression.stack.add("3");

        expression.doEvaluationStack();

        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("2");
        expectedStack.add("2");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("+");

        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression andFunction = new AndFunction();
        FunctionExpression absFunction = new AbsFunction();
        FunctionExpression dateAddFunction = new DateAddFunction();
        FunctionExpression divideFunction = new DivideFunction();

        assertEquals(andFunction, Expression.getReservedFunctions("and"));
        assertEquals(absFunction, Expression.getReservedFunctions("abs"));
        assertEquals(dateAddFunction, Expression.getReservedFunctions("dateadd"));
        assertEquals(divideFunction, Expression.getReservedFunctions("divide"));
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("abs"));
        assertTrue(expression.isReservedFunction("dateadd"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertFalse(expression.isLogicalFunction("abs"));
        assertFalse(expression.isLogicalFunction("dateadd"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("abs"));
        assertTrue(expression.isMathFunction("divide"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateadd"));
    }

    @Test
    public void testIsStringFunction() {
        assertFalse(expression.isStringFunction("abs"));
    }

    @Test
    public void testGetExternalFunction() {
        FunctionExpression absFunction = new AbsFunction();
        FunctionExpression divideFunction = new DivideFunction();

        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(absFunction);
        expression.externalFunctions.add(divideFunction);

        assertEquals(absFunction, expression.getExternalFunction("abs"));
        assertEquals(divideFunction, expression.getExternalFunction("divide"));
        assertNull(expression.getExternalFunction("and"));
    }

    @Test
    public void testIsExternalFunction() {
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new AbsFunction());
        expression.externalFunctions.add(new DateAddFunction());

        assertTrue(expression.isExternalFunction("abs"));
        assertTrue(expression.isExternalFunction("dateadd"));
        assertFalse(expression.isExternalFunction("and"));
    }

    @Test
    public void testGetFunction() {
        FunctionExpression absFunction = new AbsFunction();
        FunctionExpression divideFunction = new DivideFunction();

        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(divideFunction);

        assertEquals(absFunction, expression.getFunction("abs"));
        assertEquals(divideFunction, expression.getFunction("divide"));
        assertNull(expression.getFunction("and"));
    }

    @Test
    public void testIsFunction() {
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new AndFunction());
        expression.externalFunctions.add(new AbsFunction());

        assertTrue(expression.isFunction("and"));
        assertTrue(expression.isFunction("abs"));
        assertFalse(expression.isFunction("dateadd"));
    }

    // Add more test cases for the remaining methods
    
}