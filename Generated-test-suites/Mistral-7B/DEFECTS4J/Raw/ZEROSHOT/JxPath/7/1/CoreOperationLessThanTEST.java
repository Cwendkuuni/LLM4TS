package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.expression.Expression;

import java.util.Arrays;

public class CoreOperationLessThanTest {

    @org.junit.Test
    public void testCoreOperationLessThan() {
        // Test with double values
        CoreOperationLessThan operation = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with int values
        operation = new CoreOperationLessThan(
                new ConstantExpression(new Integer(5)),
                new ConstantExpression(new Integer(3)));
        context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with negative int values
        operation = new CoreOperationLessThan(
                new ConstantExpression(new Integer(-5)),
                new ConstantExpression(new Integer(-3)));
        context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with equal values
        operation = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));
        context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });
        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test with one argument
        operation = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)), null);
        context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });
        try {
            operation.computeValue(context);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with more than two arguments
        Expression[] args = new Expression[3];
        args[0] = new ConstantExpression(new Double(5.0));
        args[1] = new ConstantExpression(new Double(3.0));
        args[2] = new ConstantExpression(new Double(6.0));
        CoreOperationLessThan op = new CoreOperationLessThan(args);
        context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });
        try {
            op.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with non-numeric arguments
        operation = new CoreOperationLessThan(
                new ConstantExpression("5"),
                new ConstantExpression("3"));
        context = new EvalContext(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }

            @Override
            public int getSize() {
                return 0;
            }
        });
        try {
            operation.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }
}