package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        EvaluationContext context = new EvaluationContext();
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Test case 1
        expression.getArguments()[0].setValue(4);
        expression.getArguments()[1].setValue(2);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test case 2
        expression.getArguments()[0].setValue(2);
        expression.getArguments()[1].setValue(4);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test case 3
        expression.getArguments()[0].setValue(4);
        expression.getArguments()[1].setValue(4);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        assertEquals(">", expression.getSymbol());
    }
}