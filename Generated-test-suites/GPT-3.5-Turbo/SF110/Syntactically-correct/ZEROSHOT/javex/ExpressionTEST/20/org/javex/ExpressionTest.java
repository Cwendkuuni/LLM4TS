import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("1 + 2");
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2");
        assertNotNull(expression);
        assertFalse(expression.prepared);
        assertEquals(expression.stack.size(), 0);
        assertEquals(expression.expression, "1 + 2");
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2");
        expression.stack.push("1");
        expression.stack.push("+");
        expression.stack.push("2");
        expression.doEvaluationStack();
        assertEquals(expression.stack.pop(), "1");
        assertEquals(expression.stack.pop(), "2");
        assertEquals(expression.stack.pop(), "+");
    }

    @Test
    public void testPutFunction() {
        Expression expression = new Expression();
        Map reservedFunctions = expression.reservedFunctions;
        assertNotNull(reservedFunctions);
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression function = Expression.getReservedFunctions("and");
        assertNotNull(function);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("not"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("abs"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("and"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isStringFunction("toLowerCase"));
        assertFalse(expression.isStringFunction("and"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        FunctionExpression externalFunction = new ExternalFunctionExpression("testFunction");
        expression.externalFunctions = new List();
        expression.externalFunctions.add(externalFunction);
        FunctionExpression result = expression.getExternalFunction("testFunction");
        assertNotNull(result);
        assertEquals(result, externalFunction);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        FunctionExpression externalFunction = new ExternalFunctionExpression("testFunction");
        expression.externalFunctions = new List();
        expression.externalFunctions.add(externalFunction);
        assertTrue(expression.isExternalFunction("testFunction"));
        assertFalse(expression.isExternalFunction("and"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        FunctionExpression reservedFunction = new AndFunction();
        expression.reservedFunctions = new Map();
        expression.reservedFunctions.put(reservedFunction, reservedFunction);
        FunctionExpression externalFunction = new ExternalFunctionExpression("testFunction");
        expression.externalFunctions = new List();
        expression.externalFunctions.add(externalFunction);
        FunctionExpression result1 = expression.getFunction("and");
        FunctionExpression result2 = expression.getFunction("testFunction");
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1, reservedFunction);
        assertEquals(result2, externalFunction);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        FunctionExpression reservedFunction = new AndFunction();
        expression.reservedFunctions = new Map();
        expression.reservedFunctions.put(reservedFunction, reservedFunction);
        FunctionExpression externalFunction = new ExternalFunctionExpression("testFunction");
        expression.externalFunctions = new List();
        expression.externalFunctions.add(externalFunction);
        assertTrue(expression.isFunction("and"));
        assertTrue(expression.isFunction("testFunction"));
        assertFalse(expression.isFunction("not"));
    }

    @Test
    public void testExpressionConstructor2() {
        Expression expression = new Expression();
        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression();
        Stack stack = new Stack();
        expression.stack("1", stack);
        expression.stack("2", stack);
        assertEquals(stack.pop(), "12");
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression();
        expression.stack.push("1");
        expression.stack.push("+");
        expression.stack.push("2");
        ExpressionStack result = expression.getStackCopy();
        assertNotNull(result);
        assertEquals(result.size(), expression.stack.size());
        assertEquals(result.get(0), expression.stack.get(0));
        assertEquals(result.get(1), expression.stack.get(1));
        assertEquals(result.get(2), expression.stack.get(2));
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNull(expression.evaluate());
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression();
        expression.expression = "1 + 2";
        expression.tokenize();
        assertEquals(expression.stack.size(), 3);
        assertEquals(expression.stack.get(0), "1");
        assertEquals(expression.stack.get(1), "+");
        assertEquals(expression.stack.get(2), "2");
    }
}