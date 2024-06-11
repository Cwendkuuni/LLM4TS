import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("");
        List externalFunctions = expression.getExternalFunctions();
        assertEquals(null, externalFunctions);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("");
        assertEquals("", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("");
        expression.stack = new ExpressionStack();
        expression.stack.add("(");
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add(")");
        expression.doEvaluationStack();
        ExpressionStack expectedOutput = new ExpressionStack();
        expectedOutput.add("1");
        expectedOutput.add("2");
        expectedOutput.add("+");
        assertEquals(expectedOutput, expression.stack);
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression expectedOutput = new AndFunction();
        FunctionExpression result = Expression.getReservedFunctions("and");
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isReservedFunction("and");
        assertEquals(true, result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isLogicalFunction("and");
        assertEquals(true, result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isMathFunction("abs");
        assertEquals(true, result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isDateFunction("dateAdd");
        assertEquals(true, result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isStringFunction("startsWith");
        assertEquals(true, result);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("");
        expression.externalFunctions = new ArrayList();
        expression.externalFunctions.add(new CustomFunction());
        FunctionExpression expectedOutput = (FunctionExpression) expression.externalFunctions.get(0);
        FunctionExpression result = expression.getExternalFunction("custom");
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("");
        expression.externalFunctions = new ArrayList();
        expression.externalFunctions.add(new CustomFunction());
        boolean result = expression.isExternalFunction("custom");
        assertEquals(true, result);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("");
        FunctionExpression expectedOutput = new AndFunction();
        FunctionExpression result = expression.getFunction("and");
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isFunction("and");
        assertEquals(true, result);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("");
        expression.prepared = false;
        expression.expression = "(1+2)";
        expression.prepare();
        ExpressionStack expectedOutput = new ExpressionStack();
        expectedOutput.add("1");
        expectedOutput.add("2");
        expectedOutput.add("+");
        assertEquals(expectedOutput, expression.stack);
        assertEquals(true, expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("");
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("2");
        expression.stack.add("+");
        ExpressionStack result = expression.getStackCopy();
        assertNotSame(expression.stack, result);
        assertEquals(expression.stack, result);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return 3;
            }
        };
        Object result = expression.evaluate();
        assertEquals(3, result);
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("");
        expression.expression = "(1+2)";
        expression.stack = new ExpressionStack();
        expression.tokenize();
        ExpressionStack expectedOutput = new ExpressionStack();
        expectedOutput.add("(");
        expectedOutput.add("1");
        expectedOutput.add("+");
        expectedOutput.add("2");
        expectedOutput.add(")");
        assertEquals(expectedOutput, expression.stack);
    }
}