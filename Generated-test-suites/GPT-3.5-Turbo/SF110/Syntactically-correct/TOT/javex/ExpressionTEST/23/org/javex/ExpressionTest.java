import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        // Test case 1: check if the returned externalFunctions list is not null
        Expression expression = new Expression();
        assertNotNull(expression.getExternalFunctions());
        
        // Test case 2: check if the returned externalFunctions list is empty when no external functions are added
        Expression expression2 = new Expression();
        assertEquals(0, expression2.getExternalFunctions().size());
    }

    @Test
    public void testExpressionConstructor() {
        // Test case 1: check if the expression field is set correctly in the constructor
        String expected = "5 + 3";
        Expression expression = new Expression(expected);
        assertEquals(expected, expression.expression);

        // Test case 2: check if the stack field is initialized properly in the constructor
        Expression expression2 = new Expression();
        assertNotNull(expression2.stack);
    }

    @Test
    public void testDoEvaluationStack() {
        // Test case 1: check if the stack is transformed correctly when there are no parentheses
        Expression expression = new Expression("5 + 3");
        expression.stack.add("5");
        expression.stack.add("+");
        expression.stack.add("3");
        expression.doEvaluationStack();
        assertEquals("5 3 +", expression.stack.toString());

        // Test case 2: check if the stack is transformed correctly when there are parentheses
        Expression expression2 = new Expression("(5 + 3) * 2");
        expression2.stack.add("(");
        expression2.stack.add("5");
        expression2.stack.add("+");
        expression2.stack.add("3");
        expression2.stack.add(")");
        expression2.stack.add("*");
        expression2.stack.add("2");
        expression2.doEvaluationStack();
        assertEquals("5 3 + 2 *", expression2.stack.toString());

        // Test case 3: check the stack is transformed correctly when there are logical and math functions
        Expression expression3 = new Expression("5 + (3 / 2) and 2 > 1");
        expression3.stack.add("5");
        expression3.stack.add("+");
        expression3.stack.add("(");
        expression3.stack.add("3");
        expression3.stack.add("/");
        expression3.stack.add("2");
        expression3.stack.add(")");
        expression3.stack.add("and");
        expression3.stack.add("2");
        expression3.stack.add(">");
        expression3.stack.add("1");
        expression3.doEvaluationStack();
        assertEquals("5 3 2 / + 2 1 > and", expression3.stack.toString());
    }

    @Test
    public void testGetReservedFunctions() {
        // Test case 1: check if the map reservedFunctions is populated correctly
        Expression expression = new Expression();
        assertNotNull(expression.reservedFunctions);
        assertTrue(expression.reservedFunctions.size() > 0);
        assertTrue(expression.reservedFunctions.containsKey(new AndFunction()));
        assertTrue(expression.reservedFunctions.containsKey(new BetweenFunction()));
        // ... add more checks for other reserved functions

        // Test case 2: check if the correct FunctionExpression is returned for the given symbol
        Expression expression2 = new Expression();
        assertEquals(new AndFunction(), expression2.getReservedFunctions("and"));
        assertEquals(new BetweenFunction(), expression2.getReservedFunctions("between"));
        // ... add more checks for other reserved functions
    }

    @Test
    public void testIsReservedFunction() {
        // Test case 1: check if the given symbol is a reserved function
        Expression expression = new Expression();
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("between"));
        // ... add more checks for other reserved functions

        // Test case 2: check if the given symbol is not a reserved function
        Expression expression2 = new Expression();
        assertFalse(expression2.isReservedFunction("foo"));
    }

    // ... add more test methods for the remaining methods in the Expression class

}