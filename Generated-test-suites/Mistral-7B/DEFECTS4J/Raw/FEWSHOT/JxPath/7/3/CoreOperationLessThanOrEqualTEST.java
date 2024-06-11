package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValueWithDoubleValues() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new LiteralExpression(new Double(2.0)));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Info getChild(int index) {
                return new NodeInfo(this, "");
            }

            @Override
            public int getLength() {
                return 1;
            }
        });

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InfoSet() {
            @Override
            public Info getChild(int index) {
                return new NodeInfo(this, "1.5");
            }

            @Override
            public int getLength() {
                return 1;
            }
        });

        result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext(new InfoSet() {
            @Override
            public Info getChild(int index) {
                return new NodeInfo(this, "2.0");
            }

            @Override
            public int getLength() {
                return 1;
            }
        });

        result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet() {
            @Override
            public Info getChild(int index) {
                return new NodeInfo(this, "1.5");
            }

            @Override
            public int getLength() {
                return 1;
            }
        });

        result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet() {
            @Override
            public Info getChild(int index) {
                return new NodeInfo(this, "1.5");
            }

            @Override
            public int getLength() {
                return 1;
            }
        });

        CoreOperationLessThanOrEqual operationWithNegatedArg2 = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new NegateExpression(new LiteralExpression(new Double(2.0))));

        result = operationWithNegatedArg2.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet() {
            @Override
            public Info getChild(int index) {
                return new NodeInfo(this, "2.0");
            }

            @Override
            public int getLength() {
                return 1;
            }
        });

        result = operationWithNegatedArg2.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithMixedValues() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new VariableExpression("x"));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Info getChild(int index) {
                return new NodeInfo(this, "2.5");
            }

            @Override
            public int getLength() {
                return 1;
            }
        });

        context.setVariable("x", new Double(2.0));

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context.setVariable("x", new Double(1.5));

        result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithMoreThanTwoArguments() {
        new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new LiteralExpression(new Double(2.0)),
                new LiteralExpression(new Double(3.0)));
    }

    @Test
    public void testSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new LiteralExpression(new Double(2.0)));

        assertEquals("<=", operation.getSymbol());
    }
}