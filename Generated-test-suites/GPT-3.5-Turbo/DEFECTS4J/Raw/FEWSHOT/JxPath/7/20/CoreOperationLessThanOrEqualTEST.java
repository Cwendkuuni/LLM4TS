package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 is less than arg2
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(10);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when arg1 is equal to arg2
        arg1 = new MockExpression(10);
        arg2 = new MockExpression(10);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when arg1 is greater than arg2
        arg1 = new MockExpression(15);
        arg2 = new MockExpression(10);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", expression.getSymbol());
    }

}