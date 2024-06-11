package org.javex;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        List externalFunctions = expression.getExternalFunctions();

        assertNull(externalFunctions);
    }

    @Test
    public void testExpressionConstructor() {
        String s = "test";
        Expression expression = new Expression(s) {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        assertEquals(s, expression.expression);
        assertNotNull(expression.stack);
        assertNull(expression.externalFunctions);
        assertFalse(expression.prepared);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.stack = new ExpressionStack();
        expression.stack.add("(");
        expression.stack.add(")");
        expression.stack.add("+");
        expression.stack.add("-");
        expression.stack.add("and");
        expression.stack.add("or");
        expression.stack.add("test");

        expression.doEvaluationStack();

        // TODO: add assertions
    }

    @Test
    public void testPutFunction() {
        FunctionExpression f = new AndFunction();
        Map map = new TreeMap();

        Expression.putFunction(f, map);

        assertEquals(1, map.size());
        assertTrue(map.containsKey(f));
        assertEquals(f, map.get(f));
    }

    @Test
    public void testGetReservedFunctions() {
        String simbol = "and";

        FunctionExpression function = Expression.getReservedFunctions(simbol);

        assertNotNull(function);
        assertEquals(AndFunction.class, function.getClass());
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        String simbol = "and";

        boolean isReservedFunction = expression.isReservedFunction(simbol);

        assertTrue(isReservedFunction);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        String simbol = "and";

        boolean isLogicalFunction = expression.isLogicalFunction(simbol);

        assertTrue(isLogicalFunction);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        String simbol = "abs";

        boolean isMathFunction = expression.isMathFunction(simbol);

        assertTrue(isMathFunction);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        String simbol = "date";

        boolean isDateFunction = expression.isDateFunction(simbol);

        assertTrue(isDateFunction);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        String simbol = "substring";

        boolean isStringFunction = expression.isStringFunction(simbol);

        assertTrue(isStringFunction);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        FunctionExpression externalFunction = new CustomFunctionExpression("test");

        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(externalFunction);

        FunctionExpression result = expression.getExternalFunction("test");

        assertEquals(externalFunction, result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        FunctionExpression externalFunction = new CustomFunctionExpression("test");

        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(externalFunction);

        boolean isExternalFunction = expression.isExternalFunction("test");

        assertTrue(isExternalFunction);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        FunctionExpression externalFunction = new CustomFunctionExpression("test");

        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(externalFunction);

        FunctionExpression result = expression.getFunction("test");

        assertEquals(externalFunction, result);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        FunctionExpression externalFunction = new CustomFunctionExpression("test");

        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(externalFunction);

        boolean isFunction = expression.isFunction("test");

        assertTrue(isFunction);
    }

    @Test
    public void testExpressionConstructor2() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        String c = "test";
        Stack stack = new Stack();
        stack.push("test");

        expression.stack(c, stack);

        assertEquals("testtest", stack.peek());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.expression = "test";

        expression.prepare();

        // TODO: add assertions
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.stack = new ExpressionStack();
        expression.stack.add("test");

        ExpressionStack stackCopy = expression.getStackCopy();

        assertEquals(expression.stack, stackCopy);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Object result = expression.evaluate();

        assertNull(result);
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.stack = new ExpressionStack();
        expression.expression = "test";

        expression.tokenize();

        // TODO: add assertions
    }
}