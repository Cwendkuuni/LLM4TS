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
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(10.0)));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return null;
            }
        });

        Double result = (Double) expression.computeValue(context);

        assertFalse("Expected false", result.booleanValue());
        assertEquals("Expected FALSE", Boolean.FALSE, result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Object(), new Object(), new Object(), 1);
            }
        });

        expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(5.0)));

        result = (Double) expression.computeValue(context);

        assertTrue("Expected true", result.booleanValue());
        assertEquals("Expected TRUE", Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(4.0)));

        result = (Double) expression.computeValue(context);

        assertTrue("Expected true", result.booleanValue());
        assertEquals("Expected TRUE", Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNodeInfoValues() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new NodeInfo(new Object(), new Object(), new Object(), 1)),
                new ConstantExpression(new NodeInfo(new Object(), new Object(), new Object(), 2)));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return null;
            }
        });

        assertNull("Expected null", expression.computeValue(context));

        expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new NodeInfo(new Double(5.0), null, null, 1)),
                new ConstantExpression(new NodeInfo(new Double(5.0), null, null, 2)));

        EvalContext contextWithValues = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return new NodeInfo(new Double(5.0), null, null, 1);
            }
        });

        Double result = (Double) expression.computeValue(contextWithValues);

        assertTrue("Expected true", result.booleanValue());
        assertEquals("Expected TRUE", Boolean.TRUE, result);
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithMismatchedTypes() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)), new StringExpression("five"));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public Info getInfo(int index) {
                return null;
            }
        });

        expression.computeValue(context);
    }
}