package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.StringValue;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationLessThanOrEqual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual expression;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setup() {
        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = new LiteralExpression(new Double(10.0));
        expression = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueTrue() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNode(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });

        Object result = expression.computeValue(context);
        Assert.assertEquals("Expected Boolean.TRUE", Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNode(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });

        arg1 = new LiteralExpression(new Double(15.0));

        Object result = expression.computeValue(context);
        Assert.assertEquals("Expected Boolean.FALSE", Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithStringValues() {
        InfoSet infoSet = new InfoSet() {
            @Override
            public NodeInfo getNode(int index) {
                return new NodeInfo(new StringValue("5"), null);
            }

            @Override
            public int getSize() {
                return 1;
            }
        };

        EvalContext context = new EvalContext(infoSet);

        arg1 = new LiteralExpression(new StringValue("5"));
        arg2 = new LiteralExpression(new StringValue("10"));

        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        Object result = expression.computeValue(context);
        Assert.assertFalse("Expected Boolean.FALSE, but got: " + result, result instanceof Boolean);
        Assert.assertFalse("Expected Boolean.FALSE, but got: " + result.toString(), result.toString().equals("true"));

        arg1 = new LiteralExpression(new StringValue("5"));
        arg2 = new LiteralExpression(new StringValue("5"));

        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expression.computeValue(context);
        Assert.assertTrue("Expected Boolean.TRUE, but got: " + result, result instanceof Boolean);
        Assert.assertTrue("Expected Boolean.TRUE, but got: " + result.toString(), result.toString().equals("true"));
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericValues() {
        InfoSet infoSet = new InfoSet() {
            @Override
            public NodeInfo getNode(int index) {
                return new NodeInfo(new StringValue("5"), null);
            }

            @Override
            public int getSize() {
                return 1;
            }
        };

        EvalContext context = new EvalContext(infoSet);

        arg1 = new LiteralExpression(new StringValue("5"));
        arg2 = new LiteralExpression(new StringValue("apples"));

        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = null;

        try {
            expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Expected IllegalArgumentException, but got: " + e.getMessage(), "arg2 cannot be null", e.getMessage());
        }

        arg1 = null;
        arg2 = new LiteralExpression(new Double(5.0));

        try {
            expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Expected IllegalArgumentException, but got: " + e.getMessage(), "arg1 cannot be null", e.getMessage());
        }

        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = new LiteralExpression(new Integer(5));

        try {
            expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        } catch (ClassCastException e) {
            Assert.fail("Expected ClassCastException, but got: " + e.getMessage());
        }

        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = new LiteralExpression(new Boolean(true));

        try {
            expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        } catch (ClassCastException e) {
            Assert.fail("Expected ClassCastException, but got: " + e.getMessage());
        }
    }

    @Test
    public void testGetSymbol() {
        String symbol = expression.getSymbol();
        Assert.assertEquals("Expected <=", "<=", symbol);
    }
}