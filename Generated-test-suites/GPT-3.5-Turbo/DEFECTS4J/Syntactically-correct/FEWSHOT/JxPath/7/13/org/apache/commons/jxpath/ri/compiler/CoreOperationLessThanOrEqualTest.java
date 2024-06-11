package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContextNode(node);
        
        // Test case 1
        expression.setArgs(new Expression[] {new Expression1(arg1), new Expression2(arg2)});
        assertEquals(expected1, expression.computeValue(context));
        
        // Test case 2
        expression.setArgs(new Expression[] {new Expression1(arg1), new Expression2(arg2)});
        assertEquals(expected2, expression.computeValue(context));
        
        // Test case 3
        expression.setArgs(new Expression[] {new Expression1(arg1), new Expression2(arg2)});
        assertEquals(expected3, expression.computeValue(context));
        
        // Test case 4
        expression.setArgs(new Expression[] {new Expression1(arg1), new Expression2(arg2)});
        assertEquals(expected4, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", expression.getSymbol());
    }
}