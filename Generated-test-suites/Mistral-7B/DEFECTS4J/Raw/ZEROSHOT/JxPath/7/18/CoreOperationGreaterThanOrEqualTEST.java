package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    /**
     * Test method for CoreOperationGreaterThanOrEqual.computeValue(EvalContext)
     * .
     */
    @Test
    public void testComputeValue() {
        // Test with true condition
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) expression.computeValue(mock(EvalContext.class));
        assertTrue("Expected true, but got false", result.booleanValue());

        // Test with false condition
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new Double(6.0));

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) expression.computeValue(mock(EvalContext.class));
        assertFalse("Expected false, but got true", result.booleanValue());

        // Test with null values
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(null);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) expression.computeValue(mock(EvalContext.class));
        assertFalse("Expected false, but got true", result.booleanValue());

        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(null);

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) expression.computeValue(mock(EvalContext.class));
        assertFalse("Expected false, but got true", result.booleanValue());

        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(null);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(null);

        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) expression.computeValue(mock(EvalContext.class));
        assertFalse("Expected false, but got true", result.booleanValue());
    }
}