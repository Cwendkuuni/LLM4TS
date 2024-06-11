
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("2 + 2") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNotNull(expression);
        assertEquals("2 + 2", expression.expression);
        assertNull(expression.externalFunctions);
        assertNotNull(expression.stack);
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
        expression.stack = new ExpressionStack(Arrays.asList("2 +", "1", "+"));
        expression.doEvaluationStack();
        assertEquals("2 1 + +", expression.stack.toString());
    }

    @Test
    public void testGetReservedFunctions() {
        assertNull(Expression.getReservedFunctions("nonexistent"));
        assertNotNull(Expression.getReservedFunctions("and"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("nonexistent"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("plus"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertTrue(expression.isMathFunction("sqrt"));
        assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("substring"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertTrue(expression.isStringFunction("startsWith"));
        assertFalse(expression.isStringFunction("dateAdd"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        FunctionExpression function = new GenericFunctionExpression("substring");
        expression.externalFunctions = Arrays.asList(function);
        assertEquals(function, expression.getExternalFunction("substring"));
        assertNull(expression.getExternalFunction("nonexistent"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        FunctionExpression function = new GenericFunctionExpression("substring");
        expression.externalFunctions = Arrays.asList(function);
        assertTrue(expression.isExternalFunction("substring"));
        assertFalse(expression.isExternalFunction("nonexistent"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNull(expression.getFunction("nonexistent"));
        assertNotNull(expression.getFunction("and"));
        assertNotNull(expression.getFunction("substring"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertTrue(expression.isFunction("and"));
        assertFalse(expression.isFunction("nonexistent"));
    }

    @Test
    public void testExpressionConstructor() {
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
        Stack<String> stack = new Stack<>();
        expression.stack("2", stack);
        assertEquals("2", stack.peek());
        expression.stack("+", stack);
        assertEquals("2+", stack.peek());
        expression.stack("2", stack);
        assertEquals("2+2", stack.peek());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        expression.expression = "2 + 2";
        expression.prepare();
        assertTrue(expression.prepared);
        assertEquals("2 2 +", expression.stack.toString());
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        ExpressionStack stack = new ExpressionStack(Arrays.asList("2", "2 +"));
        expression.stack = stack;
        ExpressionStack copy = expression.getStackCopy();
        assertNotSame(copy, stack);
        assertEquals(stack, copy);
    }

    @Test
    public void testEvaluate() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNull(expression.evaluate());
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        expression.expression = "2 + 2";
        expression.tokenize();
        assertEquals("2 2 +", expression.stack.toString());
    }
}

