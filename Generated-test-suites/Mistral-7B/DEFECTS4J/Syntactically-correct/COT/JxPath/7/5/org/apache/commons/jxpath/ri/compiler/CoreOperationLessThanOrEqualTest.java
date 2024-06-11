package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.functions.FunctionCall;
import org.apache.commons.jxpath.ri.functions.FunctionCallFactory;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.apache.commons.jxpath.ri.parser.JXPathParser;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.Expression;

/**
 * Test class for the CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual op;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = new TestFunctionCall(Functions.DOUBLE_VALUE, new FunctionCallFactory() {
            public FunctionCall createFunctionCall(String name, Object[] args) {
                return new FunctionCall(Functions.DOUBLE_VALUE, new Object[] { Double.valueOf(4.0) });
            }
        });

        arg2 = new TestFunctionCall(Functions.DOUBLE_VALUE, new FunctionCallFactory() {
            public FunctionCall createFunctionCall(String name, Object[] args) {
                return new FunctionCall(Functions.DOUBLE_VALUE, new Object[] { Double.valueOf(5.0) });
            }
        });

        op = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        assertEquals(Boolean.FALSE, op.computeValue(new EvalContext(new InfoSet() {
            {
                setNodeInfo(0, new NodeInfo(new Object(), 0, 0, null));
            }
        })));

        EvalContext context = new EvalContext(new InfoSet() {
            {
                setNodeInfo(0, new NodeInfo(Double.valueOf(4.0), 0, 0, null));
                setNodeInfo(1, new NodeInfo(Double.valueOf(5.0), 0, 0, null));
            }
        });

        assertEquals(Boolean.FALSE, op.computeValue(context));

        context = new EvalContext(new InfoSet() {
            {
                setNodeInfo(0, new NodeInfo(Double.valueOf(4.0), 0, 0, null));
                setNodeInfo(1, new NodeInfo(Double.valueOf(4.0), 0, 0, null));
            }
        });

        assertTrue(op.computeValue(context).booleanValue());

        context = new EvalContext(new InfoSet() {
            {
                setNodeInfo(0, new NodeInfo(Double.valueOf(4.0), 0, 0, null));
                setNodeInfo(1, new NodeInfo(Double.valueOf(Double.NaN), 0, 0, null));
            }
        });

        assertNull(op.computeValue(context));

        context = new EvalContext(new InfoSet() {
            {
                setNodeInfo(0, new NodeInfo(Double.valueOf(Double.NaN), 0, 0, null));
                setNodeInfo(1, new NodeInfo(Double.valueOf(4.0), 0, 0, null));
            }
        });

        assertNull(op.computeValue(context));
    }

    @Test
    public void testSymbol() {
        assertEquals("<=", op.getSymbol());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        new CoreOperationLessThanOrEqual(arg1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments2() {
        new CoreOperationLessThanOrEqual(null, arg2);
    }

    @Test
    public void testToString() {
        String expression = "4 <=" + TestUtils.quote(new JXPathParser().parseExpression("5").toString());
        assertEquals(expression, op.toString());
    }

}

class TestFunctionCall extends Expression {

    private FunctionCall functionCall;

    public TestFunctionCall(String name, FunctionCallFactory functionCallFactory) {
        super(new Object[] { new FunctionCall(Functions. FUNCTION_DOUBLE_VALUE, new Object[] { null }) }, null);
        this.functionCall = functionCallFactory.createFunctionCall(name, new Object[] {});
    }

    @Override
    public Object computeValue(EvalContext context) {
        return functionCall.computeValue(context);
    }

}