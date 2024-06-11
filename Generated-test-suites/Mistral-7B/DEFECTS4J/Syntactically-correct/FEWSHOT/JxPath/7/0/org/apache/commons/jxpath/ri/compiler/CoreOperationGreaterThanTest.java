package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Value;
import org.junit.Test;

/**
 * Test case for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValueWithDoubleValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(new Double(5.0))),
                new LiteralExpression(new Value(new Double(3.0))));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public NodeInfo getNode(int index) {
                return null;
            }
        });

        boolean result = operation.computeValue(context).booleanValue();
        assertTrue(result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public NodeInfo getNode(int index) {
                return null;
            }
        });

        operation = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(new Double(3.0))),
                new LiteralExpression(new Value(new Double(5.0))));

        result = operation.computeValue(context).booleanValue();
        assertFalse(result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public NodeInfo getNode(int index) {
                return null;
            }
        });

        operation = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(new Double(3.0))),
                new LiteralExpression(new Value(new Double(3.0))));

        result = operation.computeValue(context).booleanValue();
        assertFalse(result);
    }

    @Test
    public void testComputeValueWithNodeInfoValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(new NodeInfo(new Object(), 1, "", "", null))),
                new LiteralExpression(new Value(new NodeInfo(new Object(), 2, "", "", null))));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 2;
            }

            @Override
            public NodeInfo getNode(int index) {
                return index == 0 ? new NodeInfo(new Object(), 1, "", "", null)
                        : new NodeInfo(new Object(), 2, "", "", null);
            }
        });

        boolean result = operation.computeValue(context).booleanValue();
        assertFalse(result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 2;
            }

            @Override
            public NodeInfo getNode(int index) {
                return index == 0 ? new NodeInfo(new Object(), 1, "", "", null)
                        : new NodeInfo(new Object(), 2, "", "", null);
            }
        });

        operation = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(new NodeInfo(new Object(), 2, "", "", null))),
                new LiteralExpression(new Value(new NodeInfo(new Object(), 1, "", "", null))));

        result = operation.computeValue(context).booleanValue();
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithInvalidNumberOfArguments() {
        new CoreOperationGreaterThan(new LiteralExpression(new Value(1)), new LiteralExpression(new Value(1)),
                new LiteralExpression(new Value(1)));
    }

    @Test
    public void testSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(5)),
                new LiteralExpression(new Value(3)));

        String result = operation.getSymbol();
        assertEquals(">", result);
    }
}