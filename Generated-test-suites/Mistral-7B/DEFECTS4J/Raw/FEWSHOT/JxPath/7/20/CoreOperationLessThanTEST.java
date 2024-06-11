package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtils;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueWithDoubleArguments() {
        CoreOperationLessThan operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1.0),
                TestUtils.createNumberExpression(2.0));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1.0),
                TestUtils.createNumberExpression(1.0));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1.0),
                TestUtils.createNumberExpression(0.5));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueWithIntegerArguments() {
        CoreOperationLessThan operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1),
                TestUtils.createNumberExpression(2));
        assertFalse(operation.computeValue(new EvalContext()).booleanValue());

        operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1),
                TestUtils.createNumberExpression(1));
        assertFalse(operation.computeValue(new EvalContext()).booleanValue());

        operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1),
                TestUtils.createNumberExpression(0));
        assertTrue(operation.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueWithMixedArguments() {
        CoreOperationLessThan operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1.0),
                TestUtils.createNumberExpression(1));
        assertFalse(operation.computeValue(new EvalContext()).booleanValue());

        operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1.0),
                TestUtils.createNumberExpression(2.5));
        assertTrue(operation.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValueWithNullArguments() {
        CoreOperationLessThan operation = new CoreOperationLessThan(TestUtils.createNullExpression(),
                TestUtils.createNumberExpression(1.0));
        assertNull(operation.computeValue(new EvalContext()));

        operation = new CoreOperationLessThan(TestUtils.createNumberExpression(1.0),
                TestUtils.createNullExpression());
        assertNull(operation.computeValue(new EvalContext()));
    }
}