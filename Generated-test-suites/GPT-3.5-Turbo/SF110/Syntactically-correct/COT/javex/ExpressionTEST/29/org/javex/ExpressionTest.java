
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertNull(expression.getExternalFunctions());
    }

    @Test
    public void testConstructor() {
        Expression expression = new Expression("test");

        Assert.assertEquals("test", expression.expression);
        Assert.assertEquals(false, expression.prepared);
        Assert.assertNotNull(expression.stack);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.stack.push("+");
        expression.stack.push("2");
        expression.stack.push("3");
        expression.doEvaluationStack();
        Assert.assertEquals(expression.stack.get(0), "2");
        Assert.assertEquals(expression.stack.get(1), "3");
        Assert.assertEquals(expression.stack.get(2), "+");
    }

    @Test
    public void testGetReservedFunctions() {
        Assert.assertNull(Expression.getReservedFunctions("test"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertFalse(expression.isReservedFunction("test"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertFalse(expression.isLogicalFunction("test"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertFalse(expression.isMathFunction("test"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertFalse(expression.isDateFunction("test"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertFalse(expression.isStringFunction("test"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertNull(expression.getExternalFunction("test"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertFalse(expression.isExternalFunction("test"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertNull(expression.getFunction("test"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Assert.assertFalse(expression.isFunction("test"));
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.expression = "+ 2 3";
        expression.prepare();
        Assert.assertTrue(expression.prepared);
        Assert.assertEquals(expression.stack.get(0), "2");
        Assert.assertEquals(expression.stack.get(1), "3");
        Assert.assertEquals(expression.stack.get(2), "+");
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add("3");
        ExpressionStack stackCopy = expression.getStackCopy();

        Assert.assertEquals(stackCopy.get(0), expression.stack.get(0));
        Assert.assertEquals(stackCopy.get(1), expression.stack.get(1));
        Assert.assertEquals(stackCopy.get(2), expression.stack.get(2));
    }

    @Test(expected = Exception.class)
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception("Test Exception");
            }
        };

        expression.evaluate();
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.expression = "+ 2 3";
        expression.tokenize();

        Assert.assertEquals(expression.stack.get(0), "+");
        Assert.assertEquals(expression.stack.get(1), "2");
        Assert.assertEquals(expression.stack.get(2), "3");
    }
}

