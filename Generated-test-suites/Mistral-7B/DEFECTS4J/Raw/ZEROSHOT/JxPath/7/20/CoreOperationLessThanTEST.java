package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

/**
 * Test suite for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        // Test with double values
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                new ConstExpression(new Double(5.0)),
                new ConstExpression(new Double(3.0)));

        EqualEvalContext context = new EqualEvalContext();
        context.setInfoSet(new TestInfoSet());

        Object result = lessThan.computeValue(context);
        assertEquals("Expected result to be true.", Boolean.TRUE, result);

        // Test with int values
        lessThan = new CoreOperationLessThan(
                new ConstExpression(new Integer(5)),
                new ConstExpression(new Integer(3)));

        context = new EqualEvalContext();
        context.setInfoSet(new TestInfoSet());

        result = lessThan.computeValue(context);
        assertEquals("Expected result to be true.", Boolean.TRUE, result);

        // Test with node values
        lessThan = new CoreOperationLessThan(
                new ConstExpression(new NodeInfo("5")),
                new ConstExpression(new NodeInfo("3")));

        context = new EqualEvalContext();
        context.setInfoSet(new TestInfoSet());

        result = lessThan.computeValue(context);
        assertEquals("Expected result to be false.", Boolean.FALSE, result);

        // Test with invalid input
        lessThan = new CoreOperationLessThan(null, new ConstExpression(new Double(3.0)));

        context = new EqualEvalContext();
        context.setInfoSet(new TestInfoSet());

        try {
            lessThan.computeValue(context);
            fail("Expected Exception.");
        } catch (NullPointerException e) {
            // Expected
        }

        lessThan = new CoreOperationLessThan(new ConstExpression(new Double(3.0)), null);

        context = new EqualEvalContext();
        context.setInfoSet(new TestInfoSet());

        try {
            lessThan.computeValue(context);
            fail("Expected Exception.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private static class TestInfoSet implements InfoSet {
        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public NodeInfo getNode(int index) {
            return null;
        }

        @Override
        public String getStringValue(int index) {
            return null;
        }

        @Override
        public int getIntValue(int index) {
            return 0;
        }

        @Override
        public double getDoubleValue(int index) {
            return 0;
        }

        @Override
        public boolean getBooleanValue(int index) {
            return false;
        }

        @Override
        public Object getValue(int index) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void appendNode(NodeInfo node) {
            // Not implemented
        }

        @Override
        public void appendStringValue(String value) {
            // Not implemented
        }

        @Override
        public void appendIntValue(int value) {
            // Not implemented
        }

        @Override
        public void appendDoubleValue(double value) {
            // Not implemented
        }

        @Override
        public void appendBooleanValue(boolean value) {
            // Not implemented
        }

        @Override
        public void appendValue(Object value) {
            // Not implemented
        }
    }
}