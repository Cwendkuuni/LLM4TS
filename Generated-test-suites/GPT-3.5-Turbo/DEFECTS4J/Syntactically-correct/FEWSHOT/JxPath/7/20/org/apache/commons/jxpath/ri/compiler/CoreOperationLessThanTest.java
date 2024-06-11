package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationLessThan op = new CoreOperationLessThan(expression1, expression2);
        EvalContext context = new EvalContext();
        
        // Test case 1
        expression1 = new Expression();
        expression1.setValue(5);
        expression2 = new Expression();
        expression2.setValue(10);
        assertEquals(Boolean.TRUE, op.computeValue(context));
        
        // Test case 2
        expression1 = new Expression();
        expression1.setValue(10);
        expression2 = new Expression();
        expression2.setValue(5);
        assertEquals(Boolean.FALSE, op.computeValue(context));
        
        // Test case 3
        expression1 = new Expression();
        expression1.setValue(5.5);
        expression2 = new Expression();
        expression2.setValue(5.5);
        assertEquals(Boolean.FALSE, op.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan op = new CoreOperationLessThan(expression1, expression2);
        
        assertEquals("<", op.getSymbol());
    }
}