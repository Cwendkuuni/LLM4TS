package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

/**
 * Test cases for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(2.5)));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return null;
            }

            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }
        });

        assertTrue(op.computeValue(context).equals(Boolean.FALSE));

        context = new EvalContext(new InfoSet() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new SimpleNodeInfo("node", new Double(1.5), null);
            }

            @Override
            public NodeInfo getNodeInfo(int index) {
                return new NodeInfo() {
                    @Override
                    public Number getNumberValue() {
                        return new Double(2.5);
                    }
                };
            }
        });

        assertTrue(op.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext(new InfoSet() {
            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new SimpleNodeInfo("node", new Double(3.5), null);
            }

            @Override
            public NodeInfo getNodeInfo(int index) {
                return new NodeInfo() {
                    @Override
                    public Number getNumberValue() {
                        return new Double(1.5);
                    }
                };
            }
        });

        assertFalse(op.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithDifferentTypes() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Double(2.5)));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return null;
            }

            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }
        });

        assertFalse(op.computeValue(context).equals(Boolean.TRUE));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqualWithMismatchedTypes() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Integer(1)),
                new ConstantExpression(new String("2.5")));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return null;
            }

            @Override
            public NodeInfo getNodeInfo(int index) {
                return null;
            }
        });

        op.computeValue(context);
    }
}