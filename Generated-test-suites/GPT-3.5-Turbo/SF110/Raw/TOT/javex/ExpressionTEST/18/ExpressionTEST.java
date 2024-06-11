package org.javex;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new ConcreteExpression("5 + 3");
        List externalFunctions = expression.getExternalFunctions();
        
        assertNotNull(externalFunctions);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new ConcreteExpression("3 * 2");

        assertNotNull(expression);
        assertEquals("3 * 2", expression.expression);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new ConcreteExpression("3 + 2");
        expression.prepare();
        
        assertEquals(5, expression.stack.size());
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new ConcreteExpression("and(4, 5)");
        boolean isReservedFunction = expression.isReservedFunction("and");
        
        assertTrue(isReservedFunction);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new ConcreteExpression("or(4, 5)");
        boolean isLogicalFunction = expression.isLogicalFunction("or");

        assertTrue(isLogicalFunction);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new ConcreteExpression("3 + 2");
        boolean isMathFunction = expression.isMathFunction("+");

        assertTrue(isMathFunction);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new ConcreteExpression("date_add(2021-01-01, 1)");
        boolean isDateFunction = expression.isDateFunction("date_add");

        assertTrue(isDateFunction);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new ConcreteExpression("substring('hello', 0, 2)");
        boolean isStringFunction = expression.isStringFunction("substring");

        assertTrue(isStringFunction);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new ConcreteExpression("4 + add(3, 5)");
        FunctionExpression externalFunction = expression.getExternalFunction("add");

        assertNotNull(externalFunction);
        assertEquals("add", externalFunction.getSimbol());
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new ConcreteExpression("3 * multiply(4, 2)");
        boolean isExternalFunction = expression.isExternalFunction("multiply");

        assertTrue(isExternalFunction);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new ConcreteExpression("3 - minus(5)");
        FunctionExpression function = expression.getFunction("minus");

        assertNotNull(function);
        assertEquals("minus", function.getSimbol());
    }

    @Test
    public void testIsFunction() {
        Expression expression = new ConcreteExpression("isnull(value)");
        boolean isFunction = expression.isFunction("isnull");

        assertTrue(isFunction);
    }

    @Test
    public void testPrepare() {
        Expression expression = new ConcreteExpression("2 - 1");
        expression.prepare();
        ExpressionStack stackCopy = expression.getStackCopy();

        assertEquals(3, stackCopy.size());
        assertEquals("2", stackCopy.get(0));
        assertEquals("1", stackCopy.get(1));
        assertEquals("-", stackCopy.get(2));
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new ConcreteExpression("2 * 3");
        Object result = expression.evaluate();

        assertEquals(6, result);
    }

    @Test
    public void testTokenize() {
        Expression expression = new ConcreteExpression("4 + 2");
        expression.prepare();
        ExpressionStack stackCopy = expression.getStackCopy();

        assertEquals(3, stackCopy.size());
        assertEquals("4", stackCopy.get(0));
        assertEquals("2", stackCopy.get(1));
        assertEquals("+", stackCopy.get(2));
    }

    private static class ConcreteExpression extends Expression {
        public ConcreteExpression(String s) {
            super(s);
        }

        @Override
        public Object evaluate() throws Exception {
            return null;
        }
    }

}