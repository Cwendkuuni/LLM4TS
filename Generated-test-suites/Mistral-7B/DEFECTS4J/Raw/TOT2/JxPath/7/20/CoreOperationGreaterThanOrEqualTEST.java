package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.ConstantExpression;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqualConstructor() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        assertEquals(">=", operation.getSymbol());
    }

    @Test
    public void testComputeValueTypicalUseCase() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(4.0));
        assertTrue(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueEqualValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        assertFalse(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueZero() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(0.0), new ConstantExpression(1.0));
        assertFalse(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueNegativeNumbers() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(-5.0), new ConstantExpression(-2.0));
        assertTrue(operation.computeValue(new EvalContext()));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArguments() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, new ConstantExpression(5.0));
    }

    @Test
    public void testComputeValueEmptyArguments() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        assertNull(operation.getSymbol());
        assertNull(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueOneNullArgument() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(1.0), null);
        assertFalse(operation.computeValue(new EvalContext()));
    }
}

//