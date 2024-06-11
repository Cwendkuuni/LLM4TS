package org.javex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
    }

    @Test
    public void testGetExternalFunctions() {
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new FunctionExpression("max"));
        expression.externalFunctions.add(new FunctionExpression("min"));

        assertEquals(2, expression.getExternalFunctions().size());
    }

    @Test
    public void testConstructor() {
        Expression e = new Expression("1 + 2");
        assertEquals("1 + 2", e.expression);
        assertFalse(e.prepared);
        assertEquals(new ExpressionStack(), e.stack);

        e = new Expression();
        assertTrue(e.stack.isEmpty());
    }

    @Test
    public void testGetReservedFunctions() {
        // Test for each reserved function
        assertEquals(AndFunction.class, Expression.getReservedFunctions("and").getClass());
        assertEquals(BetweenFunction.class, Expression.getReservedFunctions("between").getClass());
        assertEquals(ContainsFunction.class, Expression.getReservedFunctions("contains").getClass());
        assertEquals(EndsWithFunction.class, Expression.getReservedFunctions("endsWith").getClass());
        assertEquals(EqualsFunction.class, Expression.getReservedFunctions("equals").getClass());
        assertEquals(GreaterEqThanFunction.class, Expression.getReservedFunctions("greaterEqThan").getClass());
        assertEquals(GreaterThanFunction.class, Expression.getReservedFunctions("greaterThan").getClass());
        assertEquals(IsEmptyFunction.class, Expression.getReservedFunctions("isEmpty").getClass());
        assertEquals(IsFunction.class, Expression.getReservedFunctions("is").getClass());
        assertEquals(IsNullFunction.class, Expression.getReservedFunctions("isNull").getClass());
        assertEquals(LessEqThanFunction.class, Expression.getReservedFunctions("lessEqThan").getClass());
        assertEquals(LessThanFunction.class, Expression.getReservedFunctions("lessThan").getClass());
        assertEquals(NotEqualsFunction.class, Expression.getReservedFunctions("notEquals").getClass());
        assertEquals(NotFunction.class, Expression.getReservedFunctions("not").getClass());
        assertEquals(NotCharFunction.class, Expression.getReservedFunctions("notChar").getClass());
        assertEquals(OrFunction.class, Expression.getReservedFunctions("or").getClass());
        assertEquals(StartsWithFunction.class, Expression.getReservedFunctions("startsWith").getClass());
        assertEquals(MatchesFunction.class, Expression.getReservedFunctions("matches").getClass());
        assertEquals(InListFunction.class, Expression.getReservedFunctions("inList").getClass());
        assertEquals(AbsFunction.class, Expression.getReservedFunctions("abs").getClass());
        assertEquals(DivideFunction.class, Expression.getReservedFunctions("divide").getClass());
        assertEquals(MultiplyFunction.class, Expression.getReservedFunctions("multiply").getClass());
        assertEquals(PlusFunction.class, Expression.getReservedFunctions("plus").getClass());
        assertEquals(SqrtFunction.class, Expression.getReservedFunctions("sqrt").getClass());
        assertEquals(SubstractFunction.class, Expression.getReservedFunctions("subtract").getClass());
        assertEquals(PowerFunction.class, Expression.getReservedFunctions("power").getClass());
        assertEquals(PowerCharFunction.class, Expression.getReservedFunctions("powerChar").getClass());
        assertEquals(DateAddFunction.class, Expression.getReservedFunctions("dateAdd").getClass());
        assertEquals(DateDiffFunction.class, Expression.getReservedFunctions("dateDiff").getClass());
        assertEquals(LengthFunction.class, Expression.getReservedFunctions("length").getClass());
        assertEquals(SubStringFunction.class, Expression.getReservedFunctions("subString").getClass());
        assertEquals(ToLowerFunction.class, Expression.getReservedFunctions("toLower").getClass());
        assertEquals(ToUpperFunction.class, Expression.getReservedFunctions("toUpper").getClass());
        // Test for a function that doesn't exist
        assertEquals(null, Expression.getReservedFunctions("fakeFunction"));
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("max"));
        assertFalse(expression.isReservedFunction("fakeFunction"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("fakeFunction"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("sqrt"));
        assertFalse(expression.isMathFunction("fakeFunction"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("fakeFunction"));
    }

   @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("contains"));
        assertFalse(expression.isStringFunction("fakeFunction"));
    }

    @Test
    public void testGetExternalFunction() {
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new FunctionExpression("max"));
        expression.externalFunctions.add(new FunctionExpression("min"));

        assertEquals(FunctionExpression.class, expression.getExternalFunction("max").getClass());
        assertEquals(FunctionExpression.class, expression.getExternalFunction("min").getClass());
        assertEquals(null, expression.getExternalFunction("fakeFunction"));
    }

    @Test
    public void testIsExternalFunction() {
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new FunctionExpression("max"));

        assertTrue(expression.isExternalFunction("max"));
        assertFalse(expression.isExternalFunction("fakeFunction"));
    }

    @Test
    public void testGetFunction() {
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new FunctionExpression("max"));

        assertEquals(FunctionExpression.class, expression.getFunction("max").getClass());
        assertEquals(null, expression.getFunction("fakeFunction"));
    }

    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("and"));
        assertFalse(expression.isFunction("fakeFunction"));
    }

    @Test
    public void testStack() {
        Stack<String> stack = new Stack<>();
        expression.stack("+", stack);
        assertEquals("+", stack.peek());

        stack = new Stack<>();
        stack.push("1");
        expression.stack("2", stack);
        assertEquals("12", stack.peek());
    }

    @Test
    public void testPrepare() {
        expression.expression = "1 + 2";
        expression.prepare();
        assertTrue(expression.prepared);

        expression.stack = new ExpressionStack();
        expression.expression = "";
        expression.prepare();
        assertTrue(expression.prepared);
        assertTrue(expression.stack.isEmpty());
    }

    @Test
    public void testGetStackCopy() {
        ExpressionStack stack = new ExpressionStack();
        stack.add("1");
        stack.add("2");
        expression.stack = stack;

        ExpressionStack copy = expression.getStackCopy();
        assertEquals(stack.size(), copy.size());
        assertEquals(stack.get(0), copy.get(0));
        assertEquals(stack.get(1), copy.get(1));
    }

    // ... continue with tests for other methods and scenarios ...

}