package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueWithNumericValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Value(new BigDecimal("10.5")), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(new BigDecimal("5.5")));
        Expression arg2 = new LiteralExpression(new Value(new BigDecimal("10.5")));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertTrue(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value(new BigDecimal("11.5")), null, null));

        assertFalse(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value(new BigDecimal("12.5")), null, null));

        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithMixedNumericAndStringValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Value("10.5"), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(new BigDecimal("5.5")));
        Expression arg2 = new LiteralExpression(new Value("10.5"));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithStringValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Value("10.5"), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value("5.5"));
        Expression arg2 = new LiteralExpression(new Value("10.5"));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value("11.5"), null, null));

        assertFalse(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value("12.5"), null, null));

        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNullValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(null, null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(null));
        Expression arg2 = new LiteralExpression(new Value(null));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNullAndNumericValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Value(new BigDecimal("10.5")), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(null));
        Expression arg2 = new LiteralExpression(new Value(new BigDecimal("10.5")));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNullAndStringValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Value("10.5"), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(null));
        Expression arg2 = new LiteralExpression(new Value("10.5"));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNumericAndStringValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Value(new BigDecimal("10.5")), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value("5.5"));
        Expression arg2 = new LiteralExpression(new Value(new BigDecimal("10.5")));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithStringAndStringValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Value("10.5"), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value("5.5"));
        Expression arg2 = new LiteralExpression(new Value("10.5"));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value("11.5"), null, null));

        assertFalse(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value("12.5"), null, null));

        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithListValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(new Value(new BigDecimal("5.5")), null, null));
                nodes.add(new NodeInfo(new Value(new BigDecimal("10.5")), null, null));
                return new NodeInfo(nodes.toArray(new NodeInfo[0]), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(new BigDecimal("5.5")));
        Expression arg2 = new LiteralExpression(new Value(new BigDecimal("10.5")));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithListAndMixedValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(new Value(new BigDecimal("5.5")), null, null));
                nodes.add(new NodeInfo(new Value("10.5"), null, null));
                return new NodeInfo(nodes.toArray(new NodeInfo[0]), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(new BigDecimal("5.5")));
        Expression arg2 = new LiteralExpression(new Value("10.5"));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithListAndStringValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(new Value(new BigDecimal("5.5")), null, null));
                nodes.add(new NodeInfo(new Value("10.5"), null, null));
                return new NodeInfo(nodes.toArray(new NodeInfo[0]), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(new BigDecimal("5.5")));
        Expression arg2 = new LiteralExpression(new Value("10.5"));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value(new BigDecimal("11.5")), null, null));

        assertFalse(operation.computeValue(context).booleanValue());

        context.setInfoAt(0, new NodeInfo(new Value(new BigDecimal("12.5")), null, null));

        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithListAndNullValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getInfo(int index) {
                List<NodeInfo> nodes = new ArrayList<>();
                nodes.add(new NodeInfo(new Value(new BigDecimal("10.5")), null, null));
                nodes.add(new NodeInfo(null, null, null));
                return new NodeInfo(nodes.toArray(new NodeInfo[0]), null, null);
            }
        });

        Expression arg1 = new LiteralExpression(new Value(new BigDecimal("5.5")));
        Expression arg2 = new LiteralExpression(new Value(new BigDecimal("10.5")));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(operation.computeValue(context).booleanValue());

        Expression arg1Null = new LiteralExpression(new Value(null));
        Expression arg2 = new LiteralExpression(new Value(new BigDecimal("10.5")));
        operation = new CoreOperationGreaterThanOrEqual(arg1Null, arg2);

        assertFalse(operation.computeValue(context).booleanValue());

        Expression arg1 = new LiteralExpression(new Value(new BigDecimal("10.5")));
        Expression arg2Null = new LiteralExpression(new Value(null));
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2Null);

        assertFalse(operation.computeValue(context).booleanValue());
    }
}