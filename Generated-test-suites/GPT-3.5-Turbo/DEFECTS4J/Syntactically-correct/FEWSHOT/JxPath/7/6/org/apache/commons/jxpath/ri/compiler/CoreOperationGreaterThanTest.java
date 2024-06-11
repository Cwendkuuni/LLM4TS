package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanTest {
    
    private CoreOperationGreaterThan operation;
    
    private EvalContext context;
    
    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValueGreaterThan() {
        expression.setValue(10);
        context.setNodeSet(new NodeSet());
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValueLessThan() {
        expression.setValue(5);
        context.setNodeSet(new NodeSet());
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValueEqual() {
        expression.setValue(10);
        context.setNodeSet(new NodeSet());
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals(">", operation.getSymbol());
    }
}